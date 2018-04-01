package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.dto.ResponseData;
import com.hand.promotion.code.MessageCode;
import com.hand.promotion.dao.PromotionActivityDao;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.ActivityType;
import com.hand.promotion.pojo.enums.CacheOperater;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.service.*;
import com.hand.promotion.util.BeanValidationUtil;
import com.hand.promotion.util.MqMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.Comparator.comparing;

/**
 * @author XinyangMei
 * @Title PromotionActivityService
 * @Description 促销活动相关操作service
 * @date 2017/12/11 19:01
 */
@Service
public class PromotionActivityServiceImpl implements IPromotionActivityService {

    @Value("${application.rocketMq.promotion.orderTag}")
    private String orderTag;

    @Value("${application.rocketMq.promotion.entrytag}")
    private String entrytag;

    @Autowired
    private PromotionActivityDao promotionActivityDao;
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Autowired
    private MqMessageUtil mqMessageUtil;
    @Autowired
    private ICheckConditionActionService checkConditionActionService;
    @Autowired
    private ISalePromotionCodeService salePromotionCodeService;
    @Autowired
    private ISaleOperatorService saleOperatorService;
    @Autowired
    private ICheckedConditionActionService checkedConditionActionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 创建促销活动
     *
     * @param promotionActivitiesPojo 要保存的促销规则pojo
     * @param userId                  操作人账户id
     * @return SimpleMessagePojo
     */
    @Override
    public SimpleMessagePojo createActivity(PromotionActivitiesPojo promotionActivitiesPojo, String userId)  {
        SimpleMessagePojo simpleMessagePojo = new SimpleMessagePojo();

        //校验促销信息
        SimpleMessagePojo promotionResult = checkPromotionInvalid(promotionActivitiesPojo);
        if (!promotionResult.isSuccess()) {
            return promotionResult;
        }

        //判断activityId是否为空，为空则新建。不为空更新旧的促销，保存新的促销（适用于促销重算逻辑）
        if (StringUtils.isEmpty(promotionActivitiesPojo.getActivity().getActivityId())) {
            PromotionActivitiesPojo resultPromotion = appendFieldAndInsert(promotionActivitiesPojo);
            //保存操作记录
            if (!StringUtils.isEmpty(userId)) {
                saleOperatorService.insertActivityOp(resultPromotion.getActivity(), "FIRST", userId);
            }

        } else {
            //查询出修改前最新的促销规则
            final PromotionActivitiesPojo prePromotion = promotionActivityDao.findLatestPromotion(promotionActivitiesPojo.getActivity().getActivityId(), PromotionConstants.Y);
            if (null == prePromotion) {
                return new SimpleMessagePojo(false, MsgMenu.ACTIVITY_CAN_NOT_NULL, null).setCheckMsg("要修改的促销活动不存在");
            }
            //校验最新版本促销活动规则是否可修改
            SimpleMessagePojo updateInvalid = checkUsingActivityInvalid(prePromotion.getActivity());
            //校验异常，返回
            if (!updateInvalid.isSuccess()) {
                return updateInvalid;
            }
            //修改促销规则
            if (Status.ACTIVITY.name().equals(prePromotion.getActivity().getStatus())) {
                //更新其截止时间为当前时间。更新状态为已过期
                ActivityPojo preActivity = prePromotion.getActivity();
                preActivity.setEndDate(System.currentTimeMillis());
                preActivity.setStatus(Status.FAILURE.name());
                preActivity.setIsUsing(PromotionConstants.N);
                //更新修改前促销规则,商品促销关联关系为已过期
                updatePromotionStatus(prePromotion, Status.FAILURE);

                //修改后的促销规则,重新生成主键，插入到数据库
                appendFieldAndInsert(promotionActivitiesPojo);
                if (!StringUtils.isEmpty(userId)) {
                    saleOperatorService.insertActivityOp(promotionActivitiesPojo.getActivity(), prePromotion.getActivity().getActivityDes(), userId);
                }
            } else if (Status.DELAY.name().equals(prePromotion.getActivity().getStatus())) {
                ActivityPojo activityPojo = promotionActivitiesPojo.getActivity();
                ActivityPojo preActivityPojo = prePromotion.getActivity();
                //设置促销活动描述的主键
                activityPojo.setId(preActivityPojo.getId());
                activityPojo.setIsUsing(preActivityPojo.getIsUsing());
                activityPojo.setLastCreationTime(System.currentTimeMillis());
                //设置促销活动编码id
                activityPojo.setActivityId(preActivityPojo.getActivityId());
                activityPojo.setCreationTime(preActivityPojo.getCreationTime());
                //设置促销活动描述状态
                if (activityPojo.getStartDate() < System.currentTimeMillis()) {
                    activityPojo.setStatus(Status.ACTIVITY.name());
                } else {
                    activityPojo.setStatus(Status.DELAY.name());
                }
                //同步复制到促销活动
                promotionActivitiesPojo.setId(activityPojo.getId());
                promotionActivitiesPojo.setPriority(activityPojo.getPriority());
                //更新促销活动
                updatePromotion(promotionActivitiesPojo);
                if (!StringUtils.isEmpty(userId)) {
                    saleOperatorService.insertActivityOp(promotionActivitiesPojo.getActivity(), prePromotion.getActivity().getActivityDes(), userId);
                }
            }

        }
        simpleMessagePojo.setObj(promotionActivitiesPojo);
        return simpleMessagePojo;

    }

    /**
     * 查询可用促销活动
     */
    @Override
    public void queryUsefulActivity() {

    }


    /**
     * 校验最新版本的促销活动是否可修改促销规则的状态是否可修改,停用
     *
     * @param activity
     * @return
     */
    @Override
    public SimpleMessagePojo checkUsingActivityInvalid(ActivityPojo activity) {
        SimpleMessagePojo respPojo = new SimpleMessagePojo();
        String activityStatus = activity.getStatus();
        //活动中，待生效状态促销可修改、停用
        if (Status.ACTIVITY.getValue().equals(activityStatus) || Status.DELAY.getValue().equals(activityStatus)) {
            return respPojo;
        } else {
            respPojo.setFalse(MsgMenu.ACTIVITY_STATUS_ERR);
        }
        return respPojo;
    }

    /**
     * 校验促销规则是否合法
     */
    @Override
    public SimpleMessagePojo checkPromotionInvalid(PromotionActivitiesPojo pojo) {

        //校验activity
        if (null == pojo) {
            return new SimpleMessagePojo(false, MsgMenu.BEAN_INVALID, null);
        }
        ActivityPojo activityPojo = pojo.getActivity();
        SimpleMessagePojo resultPojo = checkActivityInvalid(activityPojo);
        if (!resultPojo.isSuccess()) {
            return resultPojo;
        }
        List<ConditionPojo> conditions = pojo.getConditions();
        List<ContainerPojo> containers = pojo.getContainers();
        List<GroupPojo> groups = pojo.getGroups();


        if (CollectionUtils.isEmpty(conditions) && CollectionUtils.isEmpty(containers) && CollectionUtils.isEmpty(groups)) {
            return new SimpleMessagePojo(false, MsgMenu.CONDITION_CAN_NOT_NULL, null);
        }

        //校验condition
        if (!CollectionUtils.isEmpty(conditions)) {
            resultPojo = checkConditionActionService.checkConditionInvalid(conditions);
            if (!resultPojo.isSuccess()) {
                return resultPojo;
            }
        }

        //校验group
        if (!CollectionUtils.isEmpty(groups)) {
            resultPojo = checkConditionActionService.checkGroupInvalid(groups);
            if (!resultPojo.isSuccess()) {
                return resultPojo;
            }
        }

        //校验container
        if (!CollectionUtils.isEmpty(containers)) {
            resultPojo = checkConditionActionService.checkContainerInvalid(containers);
            if (!resultPojo.isSuccess()) {
                return resultPojo;
            }
        }

        //校验action
        List<ActionPojo> actions = pojo.getActions();
        if (actions.size() != 1) {
            return new SimpleMessagePojo(false, MsgMenu.ACTIVITY_ACTION_ONLYONE, null);
        }
        resultPojo = checkConditionActionService.checkActionInvalid(actions.get(0));
        return resultPojo;
    }

    @Override
    public ResponseData queryPromotionByCondition(PromotionActivitiesPojo promotionActivitiesPojo, int pageNum, int pageSize) {
        return promotionActivityDao.findByCondition(promotionActivitiesPojo, pageNum, pageSize);
    }

    /**
     * 根据pojo非空字段查询促销活动
     *
     * @param promotionActivitiesPojo
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public List<PromotionActivitiesPojo> queryPromotionByCondition(PromotionActivitiesPojo promotionActivitiesPojo) {
        return promotionActivityDao.findByPojo(promotionActivitiesPojo);
    }

    /**
     * 调用DAO，插入促销数据到数据库中,生成促销商品关联关系，通知消息队列，发送缓存
     *
     * @param promotionActivitiesPojo
     */
    @Override
    public SimpleMessagePojo insertPromotion(PromotionActivitiesPojo promotionActivitiesPojo) {
        promotionActivityDao.insertPojo(promotionActivitiesPojo);
        //保存促销商品关联关系
        salePromotionCodeService.saveProductRelevance(promotionActivitiesPojo);
        //发送消息将新建促销同步到缓存
        try {
            String tag;
            //待生效的促销活动不加载到缓存
            if (Status.DELAY.getValue().equals(promotionActivitiesPojo.getActivity().getStatus())) {
                return new SimpleMessagePojo();
            }
            if (promotionActivitiesPojo.getActivity().getType().equalsIgnoreCase(ActivityType.product.getValue())) {
                tag = entrytag;
            } else {
                tag = orderTag;
            }
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getOrderPromotionMessage(CacheOperater.insert, tag, promotionActivitiesPojo), promotionActivitiesPojo.getId());
        } catch (Exception e) {
            logger.error("------发送促销创建消息时异常", e);
            return new SimpleMessagePojo(false, MsgMenu.SEND_MQ_ERR, null);
        }
        return new SimpleMessagePojo();
    }

    /**
     * 更新促销活动，删除并重建促销商品关联关系，更新消息到缓存.用于更新待生效的促销活动.
     * 入参中没有传入要修改数据的主键,通过activityId和isUsing定位要更新的数据
     *
     * @param promotionActivitiesPojo
     * @return
     */
    @Override
    public SimpleMessagePojo updatePromotion(PromotionActivitiesPojo promotionActivitiesPojo) {
        //更新促销活动
        promotionActivityDao.updateByActivityIdAndIsUsing(promotionActivitiesPojo.getActivity().getActivityId(), PromotionConstants.Y, promotionActivitiesPojo);
        //删除促销商品关联关系
        salePromotionCodeService.removeByActivityId(promotionActivitiesPojo.getActivity().getActivityId());
        //重新生成商品促销关联关系
        salePromotionCodeService.saveProductRelevance(promotionActivitiesPojo);
        //待生效的促销活动不加载到缓存
        if (Status.DELAY.getValue().equals(promotionActivitiesPojo.getActivity().getStatus())) {
            return new SimpleMessagePojo();
        }
        //同步修改后促销规则到缓存
        mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getOrderPromotionMessage(CacheOperater.update, orderTag, promotionActivitiesPojo), promotionActivitiesPojo.getId());
        return new SimpleMessagePojo();
    }

    /**
     * 调用DAO，更新促销活动状态。更新促销商品关联关系，同步促销活动到缓存
     *
     * @param promotionActivitiesPojo 要更新的促销活动
     * @param prCodeStatus            促销商品关联关系状态
     * @return
     */
    @Override
    public SimpleMessagePojo updatePromotionStatus(PromotionActivitiesPojo promotionActivitiesPojo, Status prCodeStatus) {
        promotionActivityDao.updatePojoByPK("id", promotionActivitiesPojo.getActivity().getId(), promotionActivitiesPojo);

        //更新促销商品关联关系为
        salePromotionCodeService.changeStatus(promotionActivitiesPojo.getActivity().getId(), prCodeStatus);
        ActivityPojo activity = promotionActivitiesPojo.getActivity();
        String tag;
        if (ActivityType.product.getValue().equals(activity.getType())) {
            tag = entrytag;
        } else {
            tag = orderTag;
        }
        //待生效的促销活动不加载到缓存
        if (Status.DELAY.getValue().equals(promotionActivitiesPojo.getActivity().getStatus())) {
            return new SimpleMessagePojo();
        }
        //同步修改后促销规则到缓存
        if (Status.INACTIVE.getValue().equals(promotionActivitiesPojo.getActivity().getStatus()) || Status.EXPR.getValue().equals(promotionActivitiesPojo.getActivity().getStatus())) {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getOrderPromotionMessage(CacheOperater.delete, tag, promotionActivitiesPojo), promotionActivitiesPojo.getId());
        } else if (Status.ACTIVITY.getValue().equals(promotionActivitiesPojo.getActivity().getStatus())) {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getOrderPromotionMessage(CacheOperater.insert, tag, promotionActivitiesPojo), promotionActivitiesPojo.getId());
        } else {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getOrderPromotionMessage(CacheOperater.update, tag, promotionActivitiesPojo), promotionActivitiesPojo.getId());
        }
        return new SimpleMessagePojo();
    }

    /**
     * 校验activityPojo字段是否合法
     *
     * @param activityPojo
     * @return
     */
    public SimpleMessagePojo checkActivityInvalid(ActivityPojo activityPojo) {
        long currentTime = System.currentTimeMillis();
        if (null == activityPojo) {
            return new SimpleMessagePojo(false, MsgMenu.ACTIVITY_CAN_NOT_NULL, null);
        }
        //校验必输字段
        SimpleMessagePojo validate = BeanValidationUtil.validate(activityPojo);

        if (!validate.isSuccess()) {
            return validate;
        }

        //校验修改的促销活动数据
        if (!StringUtils.isEmpty(activityPojo.getActivityId())) {
            if (!Status.DELAY.getValue().equals(activityPojo.getStatus()) && !Status.ACTIVITY.getValue().equals(activityPojo.getStatus())) {
                validate.setFalse(MsgMenu.PROMOTE_DATA_ERR);
                validate.setCheckMsg("只有活动中,待生效的促销可修改");
                return validate;
            }
        }
        //商品层级，校验优先级是否为空
        if (ActivityType.product.getValue().equals(activityPojo.getType())) {
            if (null == activityPojo.getPriority()) {
                validate.setFalse(MsgMenu.PRIORITY_CAN_NOT_NULL);
                return validate;
            }


            if (null == activityPojo.getIsOverlay()) {
                validate.setFalse(MsgMenu.ISOVERLAY_CAN_NOT_NULL);
                return validate;
            }
            //在前台展示，展示信息不能为空
            if ("N".equals(activityPojo.getIsExcludeShow())) {
                if (StringUtils.isEmpty(activityPojo.getPageShowMes())) {
                    validate.setFalse(MsgMenu.PAGE_SHOW_MESSAGE_CAN_NOT_NULL);
                    return validate;
                }
            }

        }


        //促销起始时间校验
        if (currentTime > activityPojo.getStartDate()) {
            logger.info("---------促销生效时间应大于当前时间，自动更正---");
            activityPojo.setStartDate(currentTime);
        }
        if (activityPojo.getStartDate() >= activityPojo.getEndDate()) {
            validate.setFalse(MsgMenu.ACTIVITY_START_END_DATE_ERR);
            return validate;
        }
        return validate;
    }


    /**
     * 添加促销信息，插入到mongoDB。同时生成促销商品关联关系数据
     *
     * @param promotionActivitiesPojo
     */
    PromotionActivitiesPojo appendFieldAndInsert(PromotionActivitiesPojo promotionActivitiesPojo) {
        ActivityPojo activityPojo = promotionActivitiesPojo.getActivity();
        //设置促销活动描述的主键
        activityPojo.setId(UUID.randomUUID().toString());
        activityPojo.setIsUsing(PromotionConstants.Y);
        activityPojo.setLastCreationTime(System.currentTimeMillis());
        //设置促销活动编码id
        if (StringUtils.isEmpty(activityPojo.getActivityId())) {
            activityPojo.setActivityId(UUID.randomUUID().toString());
            activityPojo.setCreationTime(System.currentTimeMillis());
        }
        //设置促销活动描述状态
        if (activityPojo.getStartDate() < System.currentTimeMillis()) {
            activityPojo.setStatus(Status.ACTIVITY.name());
        } else {
            activityPojo.setStatus(Status.DELAY.name());
        }
        //同步复制到促销活动
        promotionActivitiesPojo.setId(activityPojo.getId());
        promotionActivitiesPojo.setPriority(activityPojo.getPriority());
        //保存促销活动,更新缓存
        insertPromotion(promotionActivitiesPojo);
        return promotionActivitiesPojo;

    }

    /**
     * 创建促销状态为停用状态的促销活动
     *
     * @param promotionActivitiesPojo
     * @return
     */
    PromotionActivitiesPojo createInactivePromotion(PromotionActivitiesPojo promotionActivitiesPojo) {
        ActivityPojo activityPojo = promotionActivitiesPojo.getActivity();
        activityPojo.setId(UUID.randomUUID().toString());
        activityPojo.setStatus(Status.INACTIVE.name());
        promotionActivitiesPojo.setId(activityPojo.getId());
        promotionActivitiesPojo.setPriority(activityPojo.getPriority());
        promotionActivityDao.insertPojo(promotionActivitiesPojo);
        //保存促销商品关联关系
        salePromotionCodeService.saveProductRelevance(promotionActivitiesPojo);
        return promotionActivitiesPojo;
    }

    /**
     * 根据促销活动状态查询促销活动
     *
     * @param status
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> queryPromotionByStatus(List<String> status) {
        return promotionActivityDao.findByStatus(status);
    }

    /**
     * 停用促销活动
     *
     * @param activityId 促销活动主键
     * @return
     */
    @Override
    public SimpleMessagePojo inactiveActivity(String activityId) throws CloneNotSupportedException {
        PromotionActivitiesPojo condition = new PromotionActivitiesPojo();
        condition.setId(activityId);
        List<PromotionActivitiesPojo> pojos = queryPromotionByCondition(condition);
        //校验促销活动是否可停用
        if (CollectionUtils.isEmpty(pojos)) {
            return new SimpleMessagePojo(false, MsgMenu.CAN_NOT_FIND_PROMPTE, null);
        }
        if (pojos.size() != 1) {
            return new SimpleMessagePojo(false, MsgMenu.TO_MANY_RESULT, null);
        }
        PromotionActivitiesPojo prePromotion = pojos.get(0);
        //校验促销是否可停用
        SimpleMessagePojo invalid = checkUsingActivityInvalid(prePromotion.getActivity());
        if (!invalid.isSuccess()) {
            return invalid;
        }

        if (Status.ACTIVITY.name().equals(prePromotion.getActivity().getStatus())) {


            ActivityPojo preActivity = prePromotion.getActivity();

            //新建促销活动，复制停用前促销的条件、结果数据，起始时间为当前时间
            PromotionActivitiesPojo newPromotion = new PromotionActivitiesPojo();
            ActivityPojo newActivity = preActivity.clone();
            newActivity.setId(null);
            newActivity.setStartDate(System.currentTimeMillis());
            newPromotion.setActivity(newActivity);
            newPromotion.setConditions(prePromotion.getConditions());
            newPromotion.setActions(prePromotion.getActions());
            newPromotion.setGroups(prePromotion.getGroups());
            newPromotion.setContainers(prePromotion.getContainers());
            createInactivePromotion(newPromotion);
            //更新之前的促销活动，失效时间设置为当前时间，状态设置为失效

            preActivity.setEndDate(System.currentTimeMillis());
            preActivity.setStatus(Status.FAILURE.getValue());
            preActivity.setIsUsing(PromotionConstants.N);
            updatePromotionStatus(prePromotion, Status.valueOf(preActivity.getStatus()));
        } else if (Status.DELAY.name().equals(prePromotion.getActivity().getStatus())) {
            ActivityPojo activity = prePromotion.getActivity();
            activity.setStatus(Status.INACTIVE.getValue());
            updatePromotionStatus(prePromotion, Status.valueOf(activity.getStatus()));
        }
        return new SimpleMessagePojo();
    }

    /**
     * 启用单条的促销活动
     *
     * @param id 促销活动主键
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public SimpleMessagePojo enableActivity(String id) {
        long currentTime = System.currentTimeMillis();
        PromotionActivitiesPojo condition = new PromotionActivitiesPojo();
        condition.setId(id);
        List<PromotionActivitiesPojo> pojos = queryPromotionByCondition(condition);
        //校验促销活动是否可启用
        if (CollectionUtils.isEmpty(pojos)) {
            return new SimpleMessagePojo(false, MsgMenu.CAN_NOT_FIND_PROMPTE, null);
        }
        if (pojos.size() != 1) {
            return new SimpleMessagePojo(false, MsgMenu.TO_MANY_RESULT, null);
        }
        PromotionActivitiesPojo promotion = pojos.get(0);
        ActivityPojo activityPojo = promotion.getActivity();
        //通过促销起始时间更新促销活动状态
        if (activityPojo.getStartDate() < currentTime && activityPojo.getEndDate() > currentTime) {
            activityPojo.setStatus(Status.ACTIVITY.name());
        } else if (activityPojo.getStartDate() > currentTime) {
            activityPojo.setStatus(Status.DELAY.name());
        } else if (activityPojo.getEndDate() < currentTime) {
            activityPojo.setStatus(Status.FAILURE.getValue());
        }

        updatePromotionStatus(promotion, Status.valueOf(activityPojo.getStatus()));
        return new SimpleMessagePojo();
    }

    /**
     * 删除促销活动
     *
     * @param id 促销活动主键
     * @return
     */
    @Override
    public SimpleMessagePojo deleteActivity(String id) {
        SimpleMessagePojo simpleMessagePojo = new SimpleMessagePojo();
        PromotionActivitiesPojo condition = new PromotionActivitiesPojo();
        condition.setId(id);
        PromotionActivitiesPojo promotion = promotionActivityDao.findByPK(id, PromotionActivitiesPojo.class);
        //只有失效状态的促销活动可删除
        if (Status.FAILURE.getValue().equals(promotion.getActivity().getStatus())) {
            promotion.getActivity().setStatus(Status.EXPR.getValue());
            updatePromotionStatus(promotion, Status.EXPR);
            return simpleMessagePojo;
        } else {
            simpleMessagePojo.setFalse(MsgMenu.DELETE_ACTIVITY_STATUS_ERR);
            return simpleMessagePojo;
        }
    }

    /**
     * 删除促销活动批量删除
     *
     * @param ids 促销活动主键集合
     * @return
     */
    @Override
    public SimpleMessagePojo deleteBatchActivity(List<String> ids) {
        SimpleMessagePojo resultMessagePojo = new SimpleMessagePojo();
        try {
            for (String id : ids) {
                SimpleMessagePojo simpleMessagePojo = this.deleteActivity(id);
                /*删除失败*/
                if (!simpleMessagePojo.isSuccess()) {
                    resultMessagePojo.setCheckMsg(resultMessagePojo.getCheckMsg() + simpleMessagePojo.getCheckMsg());
                    resultMessagePojo.setSuccess(false);
                }
            }
            if (!resultMessagePojo.isSuccess()) {
                resultMessagePojo.setCheckMsg("促销活动删除异常" + resultMessagePojo.getCheckMsg());
            }
        } catch (Exception e) {
            logger.error("促销活动删除异常", e);
            resultMessagePojo.setCheckMsg("促销活动删除异常" + resultMessagePojo.getCheckMsg());
        } finally {
            return resultMessagePojo;
        }
    }

    @Override
    public PromotionActivitiesPojo findByPk(String pk) {
        return promotionActivityDao.findByPK(pk, PromotionActivitiesPojo.class);
    }





    /**
     * 查询activity头信息，详细信息用于商城活动数据同步
     *
     * @return
     */
    @Override
    public ResponseData queryForZmall() {
        ResponseData responseData = new ResponseData();
        logger.info("-----------query-----------\n");
        //获取需要同步的促销活动信息
        List<PromotionActivitiesPojo> activities = this.selectByStatusAndIsUsing(Arrays.asList(Status.FAILURE, Status.ACTIVITY, Status.INACTIVE), "Y");
        List<Map> synList = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(activities)) {
            for (PromotionActivitiesPojo activity : activities) {

                //修改到这个位置
                Map<String, ?> synToZmallActivity = this.getSynToZmallActivity(activity);


                String activityType = (String) synToZmallActivity.get("activityType");
                if (org.apache.commons.lang.StringUtils.isNotEmpty(activityType) && "o_target_price".equals(activityType)) {
                    List<Map> details = (List) synToZmallActivity.get("activityDetail");
                    Map detail = details.get(0);
                    String prodctCodes = (String) detail.get("productCodes");
                    if (org.apache.commons.lang.StringUtils.isEmpty(prodctCodes))
                        continue;
                }
                synList.add(synToZmallActivity);
//
            }
            synList.sort(comparing((Map map) -> {
                Integer priority = (Integer) map.get("activityLevel");
                if (priority == null) {
                    priority = 0;
                }
                return priority;
            }).thenComparing(comparing(map -> {
                Long startTime = (Long) map.get("startTime");
                if (startTime == null) {
                    startTime = 0L;
                }
                return startTime;
            })).thenComparing(comparing(map -> {
                String id = map.get("activityId").toString();
                return id;
            })));
            for (int i = 0; i < synList.size(); i++) {
                Map synActivity = synList.get(i);
                Long endTime = (Long) synActivity.get("endTime");
                endTime = endTime > 2147483647 ? 2147483647 : endTime;
                synActivity.put("endTime", endTime);
                synActivity.put("startTime", (Long) synActivity.get("startTime") / 1000);
                synActivity.put("activityLevel", i + 1);
            }
            responseData.setResp(synList);

        } else {
            responseData.setMsgCode(MessageCode.NO_ACTIVITY.name());
            responseData.setMsg(MessageCode.NO_ACTIVITY.getValue());
        }
        return responseData;
    }

    List<PromotionActivitiesPojo> selectByStatusAndIsUsing(List status, String isUsing){
        return promotionActivityDao.selectByStatusAndIsUsing(status, isUsing);
    }

    Map<String, ?> getSynToZmallActivity(PromotionActivitiesPojo promotionActivitiesPojo){
        Map synActivity = new HashMap();

        synActivity.put("activityId", promotionActivitiesPojo.getActivity().getActivityId());
        //活动名
        synActivity.put("activityName", promotionActivitiesPojo.getActivity().getActivityName());
        //活动描述
        synActivity.put("activityDescription", promotionActivitiesPojo.getActivity().getActivityDes());
        //relaseCode 唯一标识
        synActivity.put("releaseCode", promotionActivitiesPojo.getActivity().getActivityId());
        //促销层级
        String type = promotionActivitiesPojo.getActivity().getType();
        synActivity.put("activityClass", type);
        //商品层级促销
        if ("2".equals(type.trim())) {
            //优先级
            synActivity.put("activityLevel", promotionActivitiesPojo.getActivity().getPriority());
            //是否叠加
            synActivity.put("activityIsExclu", promotionActivitiesPojo.getActivity().getIsOverlay());

        }
        //活动开始时间
        synActivity.put("startTime", promotionActivitiesPojo.getActivity().getStartDate());
        //活动结束时间
        synActivity.put("endTime", promotionActivitiesPojo.getActivity().getEndDate());
        //活动状态
        if (Status.ACTIVITY.name().equals(promotionActivitiesPojo.getActivity().getStatus())) {
            synActivity.put("status", 1);
        } else {
            synActivity.put("status", 0);
        }
        //促销详细信息


        //此处正在修改
        Map activityDetail = getActivityDetail(promotionActivitiesPojo);


        List details = new ArrayList();
        details.add(activityDetail);
        //促销类型
        synActivity.put("activityType", activityDetail.get("activityType"));
        activityDetail.remove("activityType");
        //详细信息
        synActivity.put("activityDetail", details);

        return synActivity;
    }

    /**
     * 获取促销详细信息，包括促销涉及到的的金额，商品code，商品类别，赠品code等
     *
     * @param promotionActivitiesPojo
     * @return
     */

    public Map<String, ?> getActivityDetail(PromotionActivitiesPojo promotionActivitiesPojo) {
        //促销详细信息
        Map detail = new HashMap();
        //条件数据集合
        List<ConditionPojo> conditions = promotionActivitiesPojo.getConditions();
        for (ConditionPojo condition : conditions) {
            checkedConditionActionService.getDetailForCondition(condition, detail);
        }
        //结果数据集合
        List<ActionPojo> actions = promotionActivitiesPojo.getActions();
        for (ActionPojo action : actions) {
            checkedConditionActionService.getDetailForAction(action, detail);
        }
        String activityType = (String) detail.get("activityType");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(activityType) && "o_target_price".equals(activityType)) {
            logger.info("--------activity id:%s ", promotionActivitiesPojo.getActivity().getActivityId());
            String bundleCode = checkedConditionActionService.getBundleCode(promotionActivitiesPojo.getActivity().getActivityId());
            detail.put("productCodes", bundleCode);
        }
        return detail;
    }

}
