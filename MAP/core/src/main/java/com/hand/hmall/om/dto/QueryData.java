package com.hand.hmall.om.dto;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.data
 * @Description
 * @date 2017/7/13
 */
public class QueryData {

    /**
     * 支付类型，微信支付WECHAT/支付宝ALIPAY/银联UNIONPAY
     */
    private String mode;

    /**
     * 订单类型，付款（PAY）/退款（REFUND）
     */
    private String paymentTypeCode;

    /**
     * 订单号
     */
    private String outTradeNo;

    private String txnTime;

    public QueryData() {
    }

    public QueryData(String mode, String paymentTypeCode, String outTradeNo, String txnTime) {
        this.mode = mode;
        this.paymentTypeCode = paymentTypeCode;
        this.outTradeNo = outTradeNo;
        this.txnTime = txnTime;
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
}
