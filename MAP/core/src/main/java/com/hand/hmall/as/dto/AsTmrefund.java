package com.hand.hmall.as.dto;


import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_TMREFUND")
/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsTmrefund
 * @description HMALL_AS_TMREFUND DTO类
 * @date 2017/9/16
 */
public class AsTmrefund extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long tmrefundId;

    @Column
    private Long orderId;

    @Column
    private String code;

    @Column
    private String alipayNo;

    @Column
    private Date orderPaymentTime;


    @Column
    private Long orderentryId;

    @Column
    private Long productId;

    @Column
    private Date refundFinishTime;

    @Column
    private String buyerNick;

    @Column
    private BigDecimal actualPaidAmount;


    @Column
    private String title;

    @Column
    private BigDecimal refundFee;

    @Column
    private String manualOrAuto;

    @Column
    private String hasGoodReturn;

    @Column
    private Date created;


    @Column
    private Date timeout;

    @Column
    private String status;

    @Column
    private String goodStatus;

    @Column
    private String returnLogistics;

    @Column
    private String consignmentLogistics;

    @Column
    private String csStatus;


    @Column
    private String sellerName;

    @Column
    private String sellerAddress;

    @Column
    private String sellerZip;

    @Column
    private String sellerPhone;

    @Column
    private String sellerMobile;


    @Column
    private String sid;

    @Column
    private String companyName;

    @Column
    private String reason;

    @Column
    private String refundDesc;

    @Column
    private Date goodReturnTime;

    @Column
    private String responsibilitySide;

    @Column
    private String refundPhase;


    @Column
    private String sellerNote;

    @Column
    private Date finishTime;

    @Column
    private String refundScope;

    @Column
    private String auditPerson;

    @Column
    private String burdenTimeout;

    @Column
    private String auditAuto;

    @Column
    private String refundPerson;

    @Column
    private String creatreturn;

    @Column
    private Long returnId;

    @Transient
    private String productCode;

    @Transient
    private String productName;

    @Transient
    private String escOrderCode;

    @Transient
    // 退款申请时间从
    private String creationStartTime;

    @Transient
    //退款申请时间至
    private String creationEndTime;

    @Transient
    // 退款完结时间从
    private String finishStartTime;
    @Transient
    // 退款完结时间至
    private String finishEndTime;

    @Transient
    private String receiverName; //关联的订单上的收件人

    @Transient
    private String receiverMobile; //关联的订单上的收件人手机号码

    @Transient
    private String address; //关联的订单上的收件人联合地址

    @Transient
    private String lineNumber; //关联的订单行上的行号

    @Transient
    private Long parentLine;//关联的订单行上的父行号

    @Transient
    private Long quantity;//关联的订单行上的数量

    @Transient
    private String unit;//关联的订单行上的单位

    @Transient
    private Double basePrice;//关联的订单行上的销售价格

    @Transient
    private String isGift;//关联的订单行上的是否赠品

    @Transient
    private String vProductCode;//关联的订单行上的v码

    @Transient
    private String suitCode;//关联的订单行上的套装号

    @Transient
    private Double internalPrice;//关联的订单行上的采购价格

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getParentLine() {
        return parentLine;
    }

    public void setParentLine(Long parentLine) {
        this.parentLine = parentLine;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public String getvProductCode() {
        return vProductCode;
    }

    public void setvProductCode(String vProductCode) {
        this.vProductCode = vProductCode;
    }

    public String getSuitCode() {
        return suitCode;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public Double getInternalPrice() {
        return internalPrice;
    }

    public void setInternalPrice(Double internalPrice) {
        this.internalPrice = internalPrice;
    }

    public String getCreationStartTime() {
        return creationStartTime;
    }

    public void setCreationStartTime(String creationStartTime) {
        this.creationStartTime = creationStartTime;
    }

    public String getCreationEndTime() {
        return creationEndTime;
    }

    public void setCreationEndTime(String creationEndTime) {
        this.creationEndTime = creationEndTime;
    }

    public String getFinishStartTime() {
        return finishStartTime;
    }

    public void setFinishStartTime(String finishStartTime) {
        this.finishStartTime = finishStartTime;
    }

    public String getFinishEndTime() {
        return finishEndTime;
    }

    public void setFinishEndTime(String finishEndTime) {
        this.finishEndTime = finishEndTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public Long getTmrefundId() {
        return tmrefundId;
    }

    public void setTmrefundId(Long tmrefundId) {
        this.tmrefundId = tmrefundId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public Date getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Date orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public Long getOrderentryId() {
        return orderentryId;
    }

    public void setOrderentryId(Long orderentryId) {
        this.orderentryId = orderentryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getRefundFinishTime() {
        return refundFinishTime;
    }

    public void setRefundFinishTime(Date refundFinishTime) {
        this.refundFinishTime = refundFinishTime;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManualOrAuto() {
        return manualOrAuto;
    }

    public void setManualOrAuto(String manualOrAuto) {
        this.manualOrAuto = manualOrAuto;
    }

    public String getHasGoodReturn() {
        return hasGoodReturn;
    }

    public void setHasGoodReturn(String hasGoodReturn) {
        this.hasGoodReturn = hasGoodReturn;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(String goodStatus) {
        this.goodStatus = goodStatus;
    }

    public String getReturnLogistics() {
        return returnLogistics;
    }

    public void setReturnLogistics(String returnLogistics) {
        this.returnLogistics = returnLogistics;
    }

    public String getConsignmentLogistics() {
        return consignmentLogistics;
    }

    public void setConsignmentLogistics(String consignmentLogistics) {
        this.consignmentLogistics = consignmentLogistics;
    }

    public String getCsStatus() {
        return csStatus;
    }

    public void setCsStatus(String csStatus) {
        this.csStatus = csStatus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerZip() {
        return sellerZip;
    }

    public void setSellerZip(String sellerZip) {
        this.sellerZip = sellerZip;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public Date getGoodReturnTime() {
        return goodReturnTime;
    }

    public void setGoodReturnTime(Date goodReturnTime) {
        this.goodReturnTime = goodReturnTime;
    }

    public String getResponsibilitySide() {
        return responsibilitySide;
    }

    public void setResponsibilitySide(String responsibilitySide) {
        this.responsibilitySide = responsibilitySide;
    }

    public String getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(String refundPhase) {
        this.refundPhase = refundPhase;
    }

    public String getSellerNote() {
        return sellerNote;
    }

    public void setSellerNote(String sellerNote) {
        this.sellerNote = sellerNote;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRefundScope() {
        return refundScope;
    }

    public void setRefundScope(String refundScope) {
        this.refundScope = refundScope;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getBurdenTimeout() {
        return burdenTimeout;
    }

    public void setBurdenTimeout(String burdenTimeout) {
        this.burdenTimeout = burdenTimeout;
    }

    public String getAuditAuto() {
        return auditAuto;
    }

    public void setAuditAuto(String auditAuto) {
        this.auditAuto = auditAuto;
    }

    public String getRefundPerson() {
        return refundPerson;
    }

    public void setRefundPerson(String refundPerson) {
        this.refundPerson = refundPerson;
    }

    public String getCreatreturn() {
        return creatreturn;
    }

    public void setCreatreturn(String creatreturn) {
        this.creatreturn = creatreturn;
    }

    public Long getReturnId() {
        return returnId;
    }

    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(BigDecimal actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }
}
