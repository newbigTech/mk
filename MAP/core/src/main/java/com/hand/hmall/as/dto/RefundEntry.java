package com.hand.hmall.as.dto;

import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * author: zhangzilong
 * name: RefundEntry.java
 * discription: 退款单行DTO
 * date: 2017/8/7
 * version: 0.1
 */
@Table(name = "HMALL_AS_REFUND_ENTRY")
public class RefundEntry extends BaseDTO{

    @Id
    @Column
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long asRefundEntryId;

    @Column
    private String code;

    @Column
    private Long asRefundId;

    @Column
    private String lineNumber;

    @Column
    private String payMode;

    @Column
    private String account;

    @Column
    private Double payAmount;

    @Column
    private String name;

    @Column
    private String payStatus;

    @Column
    private String couldAmount;

    @Column
    private String changeReason;

    @Column
    private Long paymentinfoId;

    @Transient
    private String outTradeNo;

    @Transient
    private String returnReason;

    @Transient
    private String numberCode;

    @Transient
    private String refundHeaderCode;

    @Transient
    private BigDecimal totalAmount;

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getRefundHeaderCode() {
        return refundHeaderCode;
    }

    public void setRefundHeaderCode(String refundHeaderCode) {
        this.refundHeaderCode = refundHeaderCode;
    }

    public String getCouldAmount() {
        return couldAmount;
    }

    public void setCouldAmount(String couldAmount) {
        this.couldAmount = couldAmount;
    }

    public Long getAsRefundEntryId() {
        return asRefundEntryId;
    }

    public void setAsRefundEntryId(Long asRefundEntryId) {
        this.asRefundEntryId = asRefundEntryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getAsRefundId() {
        return asRefundId;
    }

    public void setAsRefundId(Long asRefundId) {
        this.asRefundId = asRefundId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RefundEntryInterface toInterface(){
        RefundEntryInterface dto = new RefundEntryInterface();
        Double amount = BigDecimal.valueOf(this.getPayAmount()).multiply(BigDecimal.valueOf(100)).doubleValue();
        dto.setTotalAmount(this.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue()+"");
        dto.setAmount(amount.intValue()+"");
        dto.setMode(this.getPayMode().toUpperCase());
        dto.setOutRefundNo(this.getCode());
        dto.setPaymentTypeCode("REFUND");
        dto.setOutTradeNo(this.getOutTradeNo());
        dto.setRefundReason(this.getReturnReason());
        dto.setTradeNo(this.getNumberCode());
        return dto;
    }

    public Long getPaymentinfoId() {
        return paymentinfoId;
    }

    public void setPaymentinfoId(Long paymentinfoId) {
        this.paymentinfoId = paymentinfoId;
    }
}
