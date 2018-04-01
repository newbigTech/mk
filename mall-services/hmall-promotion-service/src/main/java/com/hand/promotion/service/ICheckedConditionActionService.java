package com.hand.promotion.service;

import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;

import java.util.Map;

/**
 * Created by shanks on 2017/2/25.
 */
public interface ICheckedConditionActionService {

    /**
     * 获取促销结果中的优惠数据
     *
     * @param actionPojo
     * @param detail
     */
    void getDetailForAction(ActionPojo actionPojo, Map detail);

    /**
     * 获取促销条件的判断数据
     *
     * @param conditionPojo
     * @param detail
     */
    void getDetailForCondition(ConditionPojo conditionPojo, Map detail);

    /**
     * 获取套装促销对应的套装编码
     *
     * @param activityId
     * @return
     */
    String getBundleCode(String activityId);


}
