package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;

import com.hand.dto.ResponseData;
import com.hand.promotion.dao.PromotionActivityDao;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.OperatorPojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.activity.RangeOperatorPojo;
import com.hand.promotion.pojo.activity.RangeValuePojo;
import com.hand.promotion.pojo.activity.TargetValuePojo;
import com.hand.promotion.pojo.activity.ValuePojo;
import com.hand.promotion.pojo.bundles.MstBundles;
import com.hand.promotion.pojo.bundles.MstBundlesMapping;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.service.IMstBundlesService;
import com.hand.promotion.service.IPromotionActivityService;
import com.hand.promotion.util.ResponseReturnUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MstBundlesServiceImpl implements IMstBundlesService {

    @Autowired
    private PromotionActivityDao promotionActivityDao;

    @Autowired
    private IPromotionActivityService promotionActivityService;




    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public ResponseData checkBundles(MstBundles bundles) {
        return null;
    }

    /**
     * 根据套装DTO生成对应的促销规则
     *
     * @param bundles
     * @return
     */
    @Override
    public ResponseData createActivity(MstBundles bundles) {
        Long sysDate = System.currentTimeMillis();
        List<MstBundlesMapping> bundlesMappings = bundles.getBundlesMappings();
        //拼接促销规则参数，调用促销规则生成接口
        PromotionActivitiesPojo promotionActivitiesPojo = new PromotionActivitiesPojo();
        //促销活动相关信息
        ActivityPojo activityPojo = new ActivityPojo();
        //默认可叠加
        if (StringUtils.isEmpty(bundles.getIsOverlay())) {
            activityPojo.setIsOverlay(PromotionConstants.Y);
        } else {
            activityPojo.setIsOverlay(bundles.getIsOverlay());
        }

        //默认在前台展示
        activityPojo.setIsExcludeShow(PromotionConstants.N);
        //活动分组为DEFAULT
        activityPojo.setGroup(PromotionConstants.DEFAULT);
        //生效时间为系统当前时间
        activityPojo.setStartDate(sysDate);
        logger.info("startDate{}", sysDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2099, 01, 01, 12, 12, 12);
        activityPojo.setEndDate(Long.valueOf(calendar.getTime().getTime()));
        activityPojo.setActivityName(bundles.getName());
        activityPojo.setActivityDes(bundles.getDescription());
        activityPojo.setType("2");
        if (bundles.getPriority() == null) {
            activityPojo.setPriority(1);
        } else {
            activityPojo.setPriority(bundles.getPriority());
        }
        activityPojo.setPageShowMes(bundles.getDescription());
        activityPojo.setCreationTime(sysDate);
        activityPojo.setLastCreationTime(sysDate);

        if (StringUtils.isNotEmpty(bundles.getPromotionCode())) {
            /*根据促销代码查询activityId*/
            PromotionActivitiesPojo selectActivity = promotionActivityDao.findByPK(bundles.getPromotionCode(),
                    PromotionActivitiesPojo.class);
            activityPojo.setActivityId(selectActivity.getId());
        }

        //促销活动条件
        List<ConditionPojo> conditionPojos = new ArrayList<ConditionPojo>();
        //促销活动容器
        List<ContainerPojo> containerPojos = new ArrayList<ContainerPojo>();
        //目标容器
        List<TargetValuePojo> targetValuePojos = new ArrayList<TargetValuePojo>();

        for (int i = 0; i < bundlesMappings.size(); i++) {
            MstBundlesMapping mstBundlesMapping = bundlesMappings.get(i);
            ContainerPojo containerPojo = new ContainerPojo();
            TargetValuePojo targetValuePojo = new TargetValuePojo();
            containerPojo.setId(UUID.randomUUID().toString());
            targetValuePojo.setId(containerPojo.getId());
            containerPojo.setDefinitionId("CONTAINER");
            containerPojo.setMeaning("容器" + i);
            targetValuePojo.setMeaning("容器" + i);
            targetValuePojo.setCountNumber(mstBundlesMapping.getQuantity().toString());
            targetValuePojos.add(targetValuePojo);
            containerPojo.setFlag(1);
            List<ChildPojo> childPojos = new ArrayList<ChildPojo>();
            ChildPojo childPojo = new ChildPojo();
            childPojo.setId(UUID.randomUUID().toString());
            childPojo.setDefinitionId("o_product_range");
            childPojo.setParentId(containerPojo.getId());
            childPojo.setMeaning("商品范围");
            ParameterPojo parameterPojo = new ParameterPojo();
            RangeValuePojo rangeValuePojo = new RangeValuePojo();
            List<String> values = new ArrayList<String>();
            values.add(mstBundlesMapping.getProductId().toString());
            rangeValuePojo.setValue(values);
            ValuePojo valuePojo = new ValuePojo();
            if (mstBundlesMapping.getQuantity() != null) {
                valuePojo.setValue(mstBundlesMapping.getQuantity().toString());
            }
            OperatorPojo operatorPojo = new OperatorPojo();

            operatorPojo.setValue("GEATER_THAN_OR_EQUAL");
            RangeOperatorPojo rangeOperatorPojo = new RangeOperatorPojo();
            rangeOperatorPojo.setValue("MEMBER_OF");
            parameterPojo.setRangeValue(rangeValuePojo);
            parameterPojo.setValue(valuePojo.getValue());
            parameterPojo.setOperator(operatorPojo.getValue());
            parameterPojo.setRangeOperator(rangeOperatorPojo);
            childPojo.setParameters(parameterPojo);
            childPojos.add(childPojo);
            containerPojo.setChild(childPojos);
            containerPojos.add(containerPojo);
            logger.info("---container--{}", JSON.toJSONString(containerPojo));
        }
        //促销活动操作
        List<ActionPojo> actionPojos = new ArrayList<ActionPojo>();
        ActionPojo action = new ActionPojo();
        action.setId(UUID.randomUUID().toString());
        action.setDefinitionId("o_target_price");
        action.setMeaning("目标包价格");
        //目标容器参数
        ParameterPojo parameterPojo = new ParameterPojo();
        parameterPojo.setTargetValue(targetValuePojos);
        parameterPojo.setValue(bundles.getPrice().toString());
        parameterPojo.setOperator("MAX_THAN");
        action.setParameters(parameterPojo);
        actionPojos.add(action);
        //促销组关联
        List<GroupPojo> groupPojos = new ArrayList<GroupPojo>();
        promotionActivitiesPojo.setActivity(activityPojo);
        promotionActivitiesPojo.setConditions(conditionPojos);
        promotionActivitiesPojo.setContainers(containerPojos);
        promotionActivitiesPojo.setActions(actionPojos);
        promotionActivitiesPojo.setGroups(groupPojos);
        promotionActivitiesPojo.setContainerFlag(1);
        GroupPojo groupPojo = new GroupPojo();

        logger.info("----------result of Activity---\n{}", JSON.toJSONString(promotionActivitiesPojo));
        //调用促销规则生成接口，生成促销规则
        promotionActivitiesPojo.setActivity(activityPojo);
        SimpleMessagePojo messagePojo = promotionActivityService.createActivity(promotionActivitiesPojo, null);
        ResponseData responseData = new ResponseData();
        if (messagePojo.isSuccess()) {
            Object resp = messagePojo.getObj();
            if (resp instanceof PromotionActivitiesPojo) {
                PromotionActivitiesPojo promotion = (PromotionActivitiesPojo) resp;
                Map respMap = new HashMap();
                //返回生成的促销规则的主键
                respMap.put("promotionCode", promotion.getId());
                responseData.setResp(Arrays.asList(respMap));
            }
        }
        return responseData;
    }

    /**
     * 停用套装的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData stopBundleActivity(List<String> promotionIds) {
        ResponseData responseData = new ResponseData();
        try {

            List<Map<String, Object>> mesList = new ArrayList<Map<String, Object>>();
            //记录停用失败的数据量
            for (String id : promotionIds) {
                Map<String, Object> mes = new HashMap<String, Object>();
                SimpleMessagePojo messagePojo = promotionActivityService.inactiveActivity(id);
                mes.put("success", messagePojo.isSuccess());
                mes.put("id", id);
                if (messagePojo.isSuccess()) {
                    mes.put("promotionCode", ((PromotionActivitiesPojo) messagePojo.getObj()).getId());
                }
                mesList.add(mes);
            }
            responseData.setResp(mesList);
            return responseData;
        } catch (Exception e) {
            logger.error("停用失败", e);
            List<Map<String, Object>> mesList = new ArrayList<Map<String, Object>>();
            Map<String, Object> mes = new HashMap<String, Object>();
            mes.put("success", false);
            responseData.setResp(mesList);
            return responseData;
        }
    }

    /**
     * 启用套装的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData startBundleActivity(List<String> promotionIds) {
        ResponseData responseData = new ResponseData();
        try {
            List<Map<String, Object>> mesList = new ArrayList<Map<String, Object>>();
            for (String id : promotionIds) {
                SimpleMessagePojo messagePojo = promotionActivityService.enableActivity(id);
                Map<String, Object> mes = new HashMap<String, Object>();
                mes.put("success", messagePojo.isSuccess());
                mesList.add(mes);
                /* logger.debug(responseData.getMsg());*/
            }
            responseData.setResp(mesList);
           /* if (!errorList.isEmpty()) {
                responseData.setSuccess(false);
                responseData.setResp(errorList);
            }*/
            return responseData;
        } catch (Exception e) {
            logger.error("启用失败", e);
            List<Map<String, Object>> mesList = new ArrayList<Map<String, Object>>();
            Map<String, Object> mes = new HashMap<String, Object>();
            mes.put("success", false);
            responseData.setResp(mesList);
            return responseData;
        }

    }

    /**
     * 删除套装对应促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData deleteBundleActivity(List<String> promotionIds) {
        SimpleMessagePojo messagePojo = promotionActivityService.deleteBatchActivity(promotionIds);
        return ResponseReturnUtil.transSimpleMessage(messagePojo);
    }


}