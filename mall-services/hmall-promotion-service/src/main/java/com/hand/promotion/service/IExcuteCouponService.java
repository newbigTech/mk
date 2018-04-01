package com.hand.promotion.service;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;
import com.hand.promotion.pojo.order.OrderPojo;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description 调用促销引擎，执行优惠券
 */
public interface IExcuteCouponService {

    /**
     * 执行用户所有优惠券
     *
     * @param order
     * @return
     * @throws CloneNotSupportedException
     * @throws InterruptedException
     */
    SimpleMessagePojo optionCoupon(OrderPojo order) throws CloneNotSupportedException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;


    /**
     * 执行一条优惠券
     *
     * @param customerCouponPojo 要执行的用户优惠券数据
     * @param order              要执行优惠券的订单数据
     * @param currentUserId      优惠券所有人
     * @return
     */
    SimpleMessagePojo payByCoupon(CustomerCouponPojo customerCouponPojo, OrderPojo order, String currentUserId);

    /**
     * 执行用户选中的优惠券
     *
     * @param cusCouponId 选中的customerCoupon 主键
     * @param orderPojo   要计算的订单数据
     * @return
     */
    SimpleMessagePojo executeChosenCoupon(String cusCouponId, OrderPojo orderPojo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
