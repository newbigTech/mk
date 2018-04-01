package com.hand.promotion.pojo.coupon;

import java.io.Serializable;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 优惠券支付信息入参
 */
public class PaymentParm implements Serializable {

    /**
     * 支付方式
     */
    private String payModel;

    /**
     * 支付金额
     */
    private String payAmount;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付流水
     */
    private String numberCode;

    public String getPayModel() {
        return payModel;
    }

    public void setPayModel(String payModel) {
        this.payModel = payModel;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    @Override
    public String toString() {
        return "{\"PaymentParm\":{"
            + "                        \"payModel\":\"" + payModel + "\""
            + ",                         \"payAmount\":\"" + payAmount + "\""
            + ",                         \"payTime\":\"" + payTime + "\""
            + ",                         \"numberCode\":\"" + numberCode + "\""
            + "}}";
    }
}
