package com.hand.hap.cloud.hpay.data;

import javax.validation.constraints.NotNull;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description 订单信息
 * @date 2017/7/6
 */
public class OrderData {

    /**
     * 订单号
     */
    @NotNull
    private String outTradeNo;

    /**
     * 产品名称
     */
    @NotNull
    private String productName;

    @NotNull
    private String returnUrl;
    /**
     * 用户编号
     */
    private String customerNumber;

    /**
     * 币种
     */
    @NotNull
    private String currency;

    /**
     * 支付类型，微信支付WECHAT/支付宝ALIPAY/银联UNIONPAY
     */
    @NotNull
    private String mode;

    /**
     * 总额
     */
    @NotNull
    private String amount;

    /**
     * 当mode为支付宝时，该字段传pc，当mode为微信时，该字段传NATIVE，当mode为unionpay时，该字段传空
     */
    @NotNull
    private String tradeType;

    /**
     * 订单类型，付款（PAY）/退款（REFUND）
     */
    @NotNull
    private String paymentTypeCode;

    /**
     * 订单描述
     */
    @NotNull
    private String description;

    /**
     * 用户微信H5支付的外网ip，由zmall传入，微信H5支付必要字段
     */
    private String ip;

    /**
     * 返回方式
     */
    private String qrPayMode;

    /**
     * openId,供微信公众号支付(微信内打开浏览器)模式使用
     */
    private String openId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getQrPayMode() {
        return qrPayMode;
    }

    public void setQrPayMode(String qrPayMode) {
        this.qrPayMode = qrPayMode;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
