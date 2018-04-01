package com.hand.promotion.service;


import com.hand.promotion.pojo.coupon.CouponRedeemCodePojo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description 优惠券兑换码Service
 */
public interface ICouponRedeemCodeService {

    /**
     * 根据兑换码查询兑换码详细信息
     *
     * @param redeemCode
     * @return
     */
    CouponRedeemCodePojo queryByCode(String redeemCode) ;


    /**
     * 根据优惠券编码查询兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    List<CouponRedeemCodePojo> quertyByCouponCode(String couponCode) ;


    /**
     * 根据优惠券编码查询可用的兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    List<CouponRedeemCodePojo> quertyUsefulByCouponCode(String couponCode) ;

    /**
     * 插入兑换码
     *
     * @param couponRedeemCodePojo
     */
    void insert(CouponRedeemCodePojo couponRedeemCodePojo);

    /**
     * 批量插入兑换码
     *
     * @param couponRedeemCodePojos
     */
    void batchInsert(List<CouponRedeemCodePojo> couponRedeemCodePojos);

    /**
     * 占用优惠券兑换码
     *
     * @param id 兑换码主键
     */
    void occupyRedeemCode(String id);
}
