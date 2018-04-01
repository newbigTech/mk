package com.hand.promotion.service;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.order.OrderPojo;

import java.util.List;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public interface IConditionService {


    /**
     * 检查促销活动是否满足条件
     *
     * @param order 订单
     * @param rule  规则
     * @return
     */
    SimpleMessagePojo checkIsMatchCondition(OrderPojo order, PromotionActivitiesPojo rule);

    /**
     * 检查订单是否满足使用条件
     *
     * @param order 订单
     * @param rule  规则
     * @return
     */
    SimpleMessagePojo checkCouponIsMatchCondition(OrderPojo order, PromotionCouponsPojo rule);


    /**
     * 基本条件判定
     *
     * @param order         参与促销计算的订单数据
     * @param conditions
     * @param containerFlag
     * @return
     */
    SimpleMessagePojo checkInBaseConditions(OrderPojo order, List<ConditionPojo> conditions, Boolean containerFlag);


}
