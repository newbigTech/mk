package com.hand.promotion.pojo.coupon;

import java.io.Serializable;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 兑换，领取优惠券入参pojo
 */
public class ConvertCouponParm implements Serializable {


    /**
     * 优惠券领取编码
     */
    private String couponCode;

    /**
     * 优惠券兑换码
     */
    private String couponValue;

    /**
     * 要发放的用户账号
     */
    private String customerId;


    /**
     * 要兑换的优惠券数量
     */
    private Integer count;

    /**
     * 购券支付信息
     */
    private PaymentParm payment;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public PaymentParm getPayment() {
        return payment;
    }

    public void setPayment(PaymentParm payment) {
        this.payment = payment;
    }
}
