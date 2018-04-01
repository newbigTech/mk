package com.hand.promotion.service;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.order.OrderPojo;

/**
 * @Describe 调用促销引擎，执行促销活动
 * @Author xinyang.Mei
 * @Date 2017/6/28 17:09
 */
public interface IExcutePromoteService {

    /**
     * 用订单匹配促销活动,计算且只计算折扣金额,但是不对订单的其它金额(orderAmount,netAmount)进行汇总
     *
     * @param promotion
     * @param order
     * @return
     */
    SimpleMessagePojo payByActivitySingle(PromotionActivitiesPojo promotion, OrderPojo order);

    /**
     * 执行单条的促销活动,并计算订单金额
     *
     * @param promotion
     * @param order
     * @return
     */
    SimpleMessagePojo executePromotion(PromotionActivitiesPojo promotion, OrderPojo order);

    /**
     * 执行订单层级促销活动
     *
     * @param order
     * @return
     */
    SimpleMessagePojo payByActivity(OrderPojo order);


    /**
     * 执行商品层级促销活动
     *
     * @param orderPojo
     */
    void orderEntryPromote(OrderPojo orderPojo);

}
