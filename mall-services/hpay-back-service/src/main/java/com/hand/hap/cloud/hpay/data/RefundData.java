package com.hand.hap.cloud.hpay.data;

import javax.validation.constraints.NotNull;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.data
 * @Description 退款信息
 * @date 2017/7/18
 */
public class RefundData {
    /**
     * 支付类型，微信支付WECHAT/支付宝ALIPAY/银联UNIONPAY
     */
    @NotNull
    private String mode;

    /**
     * 订单类型，付款（PAY）/退款（REFUND）
     */
    @NotNull
    private String paymentTypeCode;

    /**
     * 订单号
     */
    @NotNull
    private String outTradeNo;

    /**
     * 第三方订单号
     */
    @NotNull
    private String tradeNo;

    /**
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * 微信退款的时候需要
     */
    @NotNull
    private String outRefundNo;
    /**
     * 退款总额
     */
    @NotNull
    private String amount;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 退款原因
     */
    @NotNull
    private String refundReason;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
