package com.hand.hap.cloud.hpay.data;

import javax.validation.constraints.NotNull;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.data
 * @Description 查询信息
 * @date 2017/7/13
 */
public class QueryData {

    /**
     * 支付类型，微信支付WECHAT/支付宝ALIPAY/银联UNIONPAY
     */
    @NotNull
    private String mode;

    @NotNull
    private String tradeType;
    /**
     * 订单类型，付款（PAY）/退款（REFUND）
     */
    private String paymentTypeCode;

    /**
     * 订单号
     */
    @NotNull
    private String outTradeNo;

    private String txnTime;

    /**
     * 退款查询时退款单号
     */
    private String outRefundNo;

    /**
     * 交易流水号
     */
    private String tradeNo;

    public QueryData() {
    }

    public QueryData(String outTradeNo, String tradeNo) {
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }
}
