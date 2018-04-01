package com.hand.promotion.service;


import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.order.OrderPojo;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public interface IResultService {


    /**
     * 根据订单和促销规则返回结果
     *
     * @param order           订单
     * @param rule            规则
     * @param countUpdateFlag 是否更新计数器
     * @return
     */
    SimpleMessagePojo calResultByOrderAndRules(OrderPojo order, PromotionActivitiesPojo rule, Boolean countUpdateFlag);

    /**
     * 根据订单和优惠券规则返回结果
     *
     * @param order 订单
     * @param rule  规则
     * @return
     */
    SimpleMessagePojo calCouponResultByOrderAndRules(OrderPojo order, PromotionCouponsPojo rule);

}
