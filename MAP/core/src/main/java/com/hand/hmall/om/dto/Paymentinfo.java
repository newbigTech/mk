package com.hand.hmall.om.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Paymentinfo
 * @description 支付dto
 * @date 2017年5月26日10:52:23
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_PAYMENTINFO")
public class Paymentinfo extends BaseDTO {
    @Id
    @GeneratedValue
    private Long paymentinfoId;

    private String payMode;

    private BigDecimal payAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private String numberCode;

    private Long orderId;

    private String status;

    private String account;

    private String name;

    private String syncflag;

    private Date settleTime;

    private BigDecimal refundAmount;

    private String outTradeNo;

    @Transient
    private BigDecimal canRefundAmount;

    private String orderType;

    // 第三方平台交易号，用于HPAY接口
    @Transient
    private String tradeNo;
    // 回调类型，用于HPAY接口
    @Transient
    private String mode;

    @Transient
    private String tradePayTime;

    @Transient
    private BigDecimal totalAmount;

    @Transient
    private String payTimeStart;

    @Transient
    private String payTimeTo;

    //商城订单号
    @Transient
    private String escOrderCode;

    //中台订单号
    @Transient
    private String code;

    //是否对应订单明细
    @Transient
    private String isOrderDetails;

    public Paymentinfo() {
    }

    public Paymentinfo(String payMode) {
        this.payMode = payMode;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getCanRefundAmount() {
        return canRefundAmount;
    }

    public void setCanRefundAmount(BigDecimal canRefundAmount) {
        this.canRefundAmount = canRefundAmount;
    }

    public Long getPaymentinfoId() {
        return paymentinfoId;
    }

    public void setPaymentinfoId(Long paymentinfoId) {
        this.paymentinfoId = paymentinfoId;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTradePayTime() {
        return tradePayTime;
    }

    public void setTradePayTime(String tradePayTime) {
        this.tradePayTime = tradePayTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayTimeStart() {
        return payTimeStart;
    }

    public void setPayTimeStart(String payTimeStart) {
        this.payTimeStart = payTimeStart;
    }

    public String getPayTimeTo() {
        return payTimeTo;
    }

    public void setPayTimeTo(String payTimeTo) {
        this.payTimeTo = payTimeTo;
    }

    public String getIsOrderDetails() {
        return isOrderDetails;
    }

    public void setIsOrderDetails(String isOrderDetails) {
        this.isOrderDetails = isOrderDetails;
    }
}
