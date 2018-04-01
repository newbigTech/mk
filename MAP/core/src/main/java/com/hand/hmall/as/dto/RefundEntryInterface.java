package com.hand.hmall.as.dto;

import com.hand.hap.system.dto.BaseDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * author: zhangzilong
 * name: RefundEntry.java
 * discription: 退款单行DTO，用于发送给HPAY
 * date: 2017/8/7
 * version: 0.1
 */
@Table(name = "HMALL_AS_REFUND_ENTRY")
public class RefundEntryInterface extends BaseDTO{

    @Id
    @Column
    @GeneratedValue(generator = GENERATOR_TYPE)
    private String outRefundNo;

    @Column
    private String mode;

    @Column
    private String amount;

    @Transient
    private String outTradeNo;
    
    @Transient
    private String paymentTypeCode;

    @Transient
    private String refundReason;
    
    @Transient
    private String tradeNo;

    @Transient
    private String totalAmount;

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
