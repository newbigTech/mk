package com.hand.promotion.service;

import com.hand.promotion.pojo.coupon.CouponRedemptionPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 优惠券最大兑换量Service
 */
public interface ICouponRedeptionService {

    /**
     * maxRedemption
     *
     * @param couponsPojo
     * @return
     */
    void insertFromCoupon(CouponsPojo couponsPojo);

    /**
     * 根据redemptionId优惠券主键查询优惠券剩余兑换量.
     * @param redemptionId
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    CouponRedemptionPojo queryByCid(String redemptionId) ;

    /**
     * 优惠券最大兑换数记录减去count
     * @param couponRedemptionPojo
     * @param count
     */
    void subCount(CouponRedemptionPojo couponRedemptionPojo, int count);
}
