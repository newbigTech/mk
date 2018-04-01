package com.hand.promotion.service.impl;

import com.hand.dto.ResponseData;
import com.hand.promotion.dao.PromotionCouponDao;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.CacheOperater;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.service.ICheckConditionActionService;
import com.hand.promotion.service.ICouponRedeptionService;
import com.hand.promotion.service.ICouponService;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.service.ISaleOperatorService;
import com.hand.promotion.util.BeanValidationUtil;
import com.hand.promotion.util.MqMessageUtil;
import com.hand.promotion.util.ResponseReturnUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券Service实现类
 */
@Service
public class SaleCouponServiceImpl implements ICouponService {

    @Value("${application.rocketMq.promotion.coupontag}")
    private String coupontag;
    @Autowired
    private ICheckConditionActionService checkConditionActionService;
    @Autowired
    private PromotionCouponDao promotionCouponDao;
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Autowired
    private MqMessageUtil mqMessageUtil;
    @Autowired
    private ICouponRedeptionService iCouponRedeptionService;

    @Autowired
    private ICustomerCouponService customerCouponService;

    @Autowired
    private ISaleOperatorService saleOperatorService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建优惠券
     *
     * @param promotionCouponsPojo 要保存的优惠券数据
     * @param userId               修改人信息
     * @return
     */
    @Override
    public ResponseData submit(PromotionCouponsPojo promotionCouponsPojo, String userId) {
        //数据校验
        SimpleMessagePojo checkResult = checkPromotionCouponInvalid(promotionCouponsPojo);
        if (!checkResult.isSuccess()) {
            return ResponseReturnUtil.transSimpleMessage(checkResult);
        }

        CouponsPojo coupon = promotionCouponsPojo.getCoupon();
        //couponId为空,新建优惠券
        if (StringUtils.isEmpty(coupon.getCouponId())) {
            appendFieldAndInsert(promotionCouponsPojo);
            if (!StringUtils.isEmpty(userId)) {
                saleOperatorService.insertCouponOp(promotionCouponsPojo.getCoupon(), "FIRST", userId);
            }
        } else {
            //couponId不为空,更新优惠券,查询出要更新的优惠券数据(根据优惠券编码id查询最新版本的优惠券数据)
            List<PromotionCouponsPojo> preCouponPromotions = promotionCouponDao.findByCouponIdAndIsUsing(coupon.getCouponId(), PromotionConstants.Y);
            if (CollectionUtils.isEmpty(preCouponPromotions)) {
                return ResponseReturnUtil.returnFalseResponse("根据优惠券编码id查询不到优惠券", "COUPON_IS_NOT_EXIST");
            }
            PromotionCouponsPojo prePromotionCoupon = preCouponPromotions.get(0);
            SimpleMessagePojo updateCheck = checkUpdatInvalid(prePromotionCoupon.getCoupon());
            if (!updateCheck.isSuccess()) {
                return ResponseReturnUtil.transSimpleMessage(checkResult);
            }

            //更新原来的优惠券
            CouponsPojo preCoupon = prePromotionCoupon.getCoupon();
            preCoupon.setIsUsing(PromotionConstants.N);
            updateCoupon(prePromotionCoupon);
            //插入更新后的优惠券
            appendFieldAndInsert(promotionCouponsPojo);
            if (!StringUtils.isEmpty(userId)) {
                saleOperatorService.insertCouponOp(promotionCouponsPojo.getCoupon(), preCoupon.getCouponDes(), userId);
            }
        }
        return ResponseReturnUtil.returnResp(Arrays.asList(promotionCouponsPojo), MsgMenu.SUCCESS, true);
    }

    /**
     * 更新优惠券缓存数据
     *
     * @param couponsPojo
     * @return
     */
    @Override
    public SimpleMessagePojo updateCoupon(PromotionCouponsPojo couponsPojo) {
        promotionCouponDao.updatePojoByPK("id", couponsPojo.getCoupon().getId(), couponsPojo);
        String status = couponsPojo.getCoupon().getStatus();
        //待生效的优惠券不加载到缓存
        if (Status.DELAY.getValue().equals(status)) {
            return new SimpleMessagePojo();
        }
        if (Status.EXPR.getValue().equalsIgnoreCase(status) || Status.FAILURE.getValue().equals(status)) {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getCouponMessage(CacheOperater.delete, coupontag, couponsPojo), couponsPojo.getId());
        } else {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getCouponMessage(CacheOperater.update, coupontag, couponsPojo), couponsPojo.getId());
        }
        return new SimpleMessagePojo();
    }

    /**
     * 更新优惠券状态
     *
     * @param promotionCoupon 要更新的优惠券信息,用于修改优惠券状态线程
     * @return
     */
    @Override
    public SimpleMessagePojo updateCouponStatus(PromotionCouponsPojo promotionCoupon) {
        CouponsPojo coupon = promotionCoupon.getCoupon();
        coupon.setIsSyn("N");
        promotionCouponDao.updatePojoByPK("id", coupon.getId(), promotionCoupon);
        customerCouponService.updateCustomerCouponStatus(coupon);
        String status = promotionCoupon.getCoupon().getStatus();
        //待生效状态的优惠券不加载到缓存
        if (Status.DELAY.getValue().equals(status)) {
            return new SimpleMessagePojo();
        }

        if (Status.EXPR.getValue().equalsIgnoreCase(status) || Status.FAILURE.getValue().equals(status) || Status.INACTIVE.getValue().equals(status)) {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getCouponMessage(CacheOperater.delete, coupontag, promotionCoupon), promotionCoupon.getId());

        } else {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getCouponMessage(CacheOperater.update, coupontag, promotionCoupon), promotionCoupon.getId());

        }
        return new SimpleMessagePojo();
    }

    /**
     * 校验优惠券数据是否合法
     *
     * @param promotionCouponsPojo 要校验的优惠券数据
     * @return
     */
    public SimpleMessagePojo checkPromotionCouponInvalid(PromotionCouponsPojo promotionCouponsPojo) {
        SimpleMessagePojo checkResult = new SimpleMessagePojo();
        if (null == promotionCouponsPojo) {
            checkResult.setFalse(MsgMenu.COUPON_IS_NULL);
            return checkResult;
        }
        checkResult = checkCouponInvalid(promotionCouponsPojo.getCoupon());
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        List<ConditionPojo> conditions = promotionCouponsPojo.getConditions();
        List<ContainerPojo> containers = promotionCouponsPojo.getContainers();
        List<GroupPojo> groups = promotionCouponsPojo.getGroups();


        if (CollectionUtils.isEmpty(conditions) && CollectionUtils.isEmpty(containers) && CollectionUtils.isEmpty(groups)) {
            return new SimpleMessagePojo(false, MsgMenu.CONDITION_CAN_NOT_NULL, null);
        }

        //校验condition
        if (!CollectionUtils.isEmpty(conditions)) {
            checkResult = checkConditionActionService.checkConditionInvalid(conditions);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
        }

        //校验group
        if (!CollectionUtils.isEmpty(groups)) {
            checkResult = checkConditionActionService.checkGroupInvalid(groups);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
        }

        //校验container
        if (!CollectionUtils.isEmpty(containers)) {
            checkResult = checkConditionActionService.checkContainerInvalid(containers);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
        }

        //校验action
        List<ActionPojo> actions = promotionCouponsPojo.getActions();
        if (actions.size() != 1) {
            return new SimpleMessagePojo(false, MsgMenu.ACTIVITY_ACTION_ONLYONE, null);
        }
        checkResult = checkConditionActionService.checkActionInvalid(actions.get(0));
        return checkResult;
    }

    /**
     * 校验优惠券描述信息是否合法
     *
     * @param couponsPojo 要校验的优惠券描述信息
     * @return
     */
    public SimpleMessagePojo checkCouponInvalid(CouponsPojo couponsPojo) {
        SimpleMessagePojo validate = new SimpleMessagePojo();
        Long currentTime = System.currentTimeMillis();
        if (null == couponsPojo) {
            validate.setFalse(MsgMenu.COUPON_IS_NULL);
            return validate;
        }
        //校验基础字段
        validate = BeanValidationUtil.validate(couponsPojo);
        if (!validate.isSuccess()) {
            return validate;
        }
        if (!StringUtils.isEmpty(couponsPojo.getCouponId())) {
            validate.setFalse(MsgMenu.COUPON_IS_NULL);
            validate.setCheckMsg("优惠券不可修改,可停用当前优惠券并新建一张");
            return validate;
        }
        //校验优惠券编码couponCode是否已存在
        List<PromotionCouponsPojo> byCouponCode = promotionCouponDao.findByCouponCode(couponsPojo.getCouponCode());
        if (!CollectionUtils.isEmpty(byCouponCode)) {
            validate.setFalse(MsgMenu.COUPON_CODE_USED);
            return validate;
        }
        if (couponsPojo.getEndDate() < currentTime || couponsPojo.getGetEndDate() < currentTime) {
            validate.setFalse(MsgMenu.COUPON_END_DATE_ERR);
            return validate;
        }
        //校验优惠券起止时间信息
        if (couponsPojo.getStartDate() > couponsPojo.getEndDate()) {
            validate.setFalse(MsgMenu.COUPON_DATE_ERR);
            return validate;
        }

        //校验优惠券允许领取起止时间信息
        if (couponsPojo.getStartDate() > couponsPojo.getEndDate()) {
            validate.setFalse(MsgMenu.SEND_DATE_ERR);
            return validate;
        }
        return validate;
    }

    /**
     * 为优惠券添加额外的字段数据,插入到mongoDB
     *
     * @param promotionCouponsPojo
     * @return
     */
    public PromotionCouponsPojo appendFieldAndInsert(PromotionCouponsPojo promotionCouponsPojo) {
        CouponsPojo coupon = promotionCouponsPojo.getCoupon();
        Long currentTime = System.currentTimeMillis();

        //生成优惠券id(主键)
        coupon.setId(UUID.randomUUID().toString());
        //创建则为couponId,creationTime赋值,更新保留couponId,creationTime不变
        if (StringUtils.isEmpty(coupon.getCouponId())) {
            coupon.setCouponId(UUID.randomUUID().toString());
            coupon.setCreationTime(currentTime);
        }
        //添加更新时间信息
        coupon.setLastCreationTime(currentTime);
        //设置优惠券状态
        if (coupon.getStartDate() < currentTime) {
            coupon.setStatus(Status.ACTIVITY.name());
        } else {
            coupon.setStatus(Status.DELAY.name());
        }
        //设置isUsing为Y
        coupon.setIsUsing(PromotionConstants.Y);
        coupon.setIsSyn(PromotionConstants.N);
        //向SaleCoupon表中插入优惠券数据
        promotionCouponsPojo.setId(coupon.getId());
        //插入优惠券,并将消息发送到缓存
        insertCoupon(promotionCouponsPojo);
        //同时保存优惠券最大兑换量数据
        iCouponRedeptionService.insertFromCoupon(coupon);
        return promotionCouponsPojo;
    }

    /**
     * 将优惠券数据插入到mongoDB,将优惠券数据加载到优惠券缓存中
     *
     * @param promotionCouponsPojo
     * @return
     */
    public SimpleMessagePojo insertCoupon(PromotionCouponsPojo promotionCouponsPojo) {
        //插入到数据库
        promotionCouponDao.insertPojo(promotionCouponsPojo);
        //待生效的优惠券不加载到缓存
        if (Status.DELAY.getValue().equals(promotionCouponsPojo.getCoupon().getStatus())) {
            return new SimpleMessagePojo();
        }
        //插入数据到缓存
        try {
            mqMessageUtil.sendOrderly(defaultMQProducer, mqMessageUtil.getCouponMessage(CacheOperater.insert, coupontag, promotionCouponsPojo), promotionCouponsPojo.getId());
//            defaultMQProducer.send(mqMessageUtil.getCouponMessage(CacheOperater.insert, coupontag, promotionCouponsPojo));
        } catch (Exception e) {
            logger.error("---插入优惠券到缓存异常---", e);
        }
        return new SimpleMessagePojo();

    }

    /**
     * 校验优惠券是否能被更新
     *
     * @param couponsPojo
     * @return
     */
    public SimpleMessagePojo checkUpdatInvalid(CouponsPojo couponsPojo) {
        SimpleMessagePojo respPojo = new SimpleMessagePojo();
        String status = couponsPojo.getStatus();
        //活动中，待生效状态优惠券可修改、停用
        if (Status.ACTIVITY.getValue().equals(status) || Status.DELAY.getValue().equals(status)) {
            return respPojo;
        } else {
            respPojo.setFalse(MsgMenu.COUPON_UPDATE_STATUS_ERR);
        }
        return respPojo;
    }


    /**
     * 根据前台传入的参数查询优惠券信息
     *
     * @return
     */
    @Override
    public ResponseData query(PromotionCouponsPojo pojo, int pageNum, int pageSize) {
        return promotionCouponDao.findByCondition(pojo, pageNum, pageSize);
    }

    /**
     * 根据优惠券主键查询优惠券详细信息
     *
     * @param id 优惠券主键
     * @return
     */
    @Override
    public PromotionCouponsPojo selectCouponDetail(String id) {
        return promotionCouponDao.findByPK(id, PromotionCouponsPojo.class);
    }

    /**
     * 根据优惠券id启用优惠券
     *
     * @param id
     * @return
     */
    @Override
    public SimpleMessagePojo startUsing(String id) {
        Long currentTime = System.currentTimeMillis();
        PromotionCouponsPojo enable = promotionCouponDao.findByPK(id, PromotionCouponsPojo.class);

        CouponsPojo coupon = enable.getCoupon();
        //设置启用后优惠券状态
        //通过促销起始时间更新促销活动状态
        if (coupon.getStartDate() < currentTime && coupon.getEndDate() > currentTime) {
            coupon.setStatus(Status.ACTIVITY.name());
        } else if (coupon.getStartDate() > currentTime) {
            coupon.setStatus(Status.DELAY.name());
        } else if (coupon.getEndDate() < currentTime) {
            coupon.setStatus(Status.FAILURE.getValue());
        }
        updateCouponStatus(enable);
        return new SimpleMessagePojo();
    }

    /**
     * 根据优惠券id停用优惠券
     *
     * @param id 优惠券主键
     * @return
     */
    @Override
    public SimpleMessagePojo endUsing(String id) {
        PromotionCouponsPojo inactive = promotionCouponDao.findByPK(id, PromotionCouponsPojo.class);
        if (null == inactive) {
            return new SimpleMessagePojo(false, MsgMenu.CAN_NOT_FIND_COUPON, null);
        }
        //校验优惠券状态是否可停用
        SimpleMessagePojo updateCheck = checkUpdatInvalid(inactive.getCoupon());
        if (!updateCheck.isSuccess()) {
            return updateCheck;
        }
        CouponsPojo coupon = inactive.getCoupon();
        coupon.setStatus(Status.INACTIVE.getValue());
        updateCouponStatus(inactive);
        return new SimpleMessagePojo();
    }

    /**
     * 根据优惠券编码isUsing标识的优惠券(最新版本)
     *
     * @param couponCode 优惠券编码
     * @param isUsing    是否最新版本
     * @return
     */
    @Override
    public PromotionCouponsPojo queryByCouponCode(String couponCode, String isUsing) {
        return promotionCouponDao.findByCouponCodeAndUsing(couponCode, isUsing);
    }

    /**
     * 根据优惠券状态查询优惠券
     *
     * @param status 状态集合
     * @return
     */
    @Override
    public List<PromotionCouponsPojo> queryByStatus(List<String> status) {
        return promotionCouponDao.findByStatus(status);
    }

    @Override
    public ResponseData queryAll() {
        return null;
    }

    @Override
    public ResponseData queryActivity(Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData queryByNotIn(Map<String, Object> map) {
        return null;
    }

    /**
     * 根据优惠券主键删除单个优惠券
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData delete(String id) {
        //根据优惠券主键id查询优惠券详情
        PromotionCouponsPojo byPK = promotionCouponDao.findByPK(id, PromotionCouponsPojo.class);
        if (null == byPK) {
            return ResponseReturnUtil.returnFalseResponse(id + "对应的优惠券不存在", MsgMenu.COUPON_IS_NULL.getCode());
        }
        CouponsPojo coupon = byPK.getCoupon();
        //校验优惠券是否可删除，未发放、或已失效的优惠券可以删除
        if (Status.FAILURE.getValue().equalsIgnoreCase(coupon.getStatus())) {
            coupon.setStatus(Status.EXPR.getValue());
            promotionCouponDao.updatePojoByPK("id", id, byPK);
        } else {
            return ResponseReturnUtil.returnFalseResponse(coupon.getCouponName() + "状态不是已失效，不能删除", MsgMenu.COUPON_UPDATE_STATUS_ERR.getCode());

        }
        PromotionCouponsPojo condition = new PromotionCouponsPojo();
        condition.setId(id);
        promotionCouponDao.removePojo(condition);
        return null;
    }

    /**
     * 根据优惠券主键集合批量删除优惠券
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseData delete(List<String> ids) {
        List<String> errList = new ArrayList<>();
        for (String id : ids) {
            ResponseData delete = delete(id);
            if (!delete.isSuccess()) {
                errList.add(delete.getMsg());
            }
        }

        if (!CollectionUtils.isEmpty(errList)) {
            StringBuffer sb = new StringBuffer();
            errList.forEach(str -> {
                sb.append("<p>【" + str + "】</p>");
            });
            return ResponseReturnUtil.returnFalseResponse(sb.toString(), null);
        }
        return ResponseReturnUtil.returnTrueResp(null);
    }

    @Override
    public void deleteReal(List<Map<String, Object>> maps) {

    }


    @Override
    public ResponseData selectByCode(String couponCode) {
        return null;
    }

    @Override
    public ResponseData selectByCodeCanUse(String couponCode) {
        return null;
    }

    @Override
    public ResponseData selectById(String id) {
        return null;
    }

    @Override
    public ResponseData selectCouponIdById(String id) {
        return null;
    }

    @Override
    public ResponseData selectByCouponId(String couponId) {
        return null;
    }

    @Override
    public boolean checkExclusiveCoupon(String couponId) {
        return false;
    }

    @Override
    public List<String> checkedCoupon(Map<String, Object> map) {
        return null;
    }

    /**
     * 优惠券占用释放接口
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData operateCustomerCoupon(Map map) {
        return null;
    }

    /**
     * 获取需同步的coupon
     *
     * @return
     */
    @Override
    public ResponseData getSynCoupon() {
        List<CouponsPojo> promotionCouponsPojos = promotionCouponDao.queryCouponBySyn();
        ResponseData responseData = new ResponseData();
        responseData.setResp(promotionCouponsPojos);
        return responseData;
    }

    /**
     * 设置优惠券同步标识
     *
     * @param couponCodes 优惠卷码
     * @return
     */
    @Override
    public void setCouponSyn(List<String> couponCodes) {
        promotionCouponDao.setCouponSyn(couponCodes);
    }
}
