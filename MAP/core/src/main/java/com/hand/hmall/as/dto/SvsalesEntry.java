package com.hand.hmall.as.dto;

/**
 * author: zhangzilong
 * name: SvsalesEntry
 * discription: 服务销售单行实体类
 * date: 2017/7/28
 * version: 0.1
 */

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_SVSALES_ENTRY")
public class SvsalesEntry extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long asSvsalesEntryId;

    @Column
    private Long svsalesOrderId;

    @Column
    private Long serviceOrderId;

    @Column
    private Long lineNumber;

    @Column
    private Long parentLine;

    @Column
    private Long quantity;

    @Column
    private String unit;

    @Column
    private Long basePrice;

    @Column
    private BigDecimal unitFee;

    @Column
    private BigDecimal totalFee;

    @Column
    private String isGift;

    @Column
    private Long productId;

    @Column
    private String vproduct;

    @Column
    private String suitCode;

    @Column
    private String pin;

    @Column
    private String shippingType;

    @Column
    private String note;

    @Column
    private String returnReason1;

    @Column
    private String returnReason2;

    @Column
    private String chargeType;

    @Transient
    private String productCode;

    @Transient
    private String productName;
    @Transient
    private String retailProductCode;


    public Long getAsSvsalesEntryId() {
        return asSvsalesEntryId;
    }

    public void setAsSvsalesEntryId(Long asSvsalesEntryId) {
        this.asSvsalesEntryId = asSvsalesEntryId;
    }

    public Long getSvsalesOrderId() {
        return svsalesOrderId;
    }

    public void setSvsalesOrderId(Long svsalesOrderId) {
        this.svsalesOrderId = svsalesOrderId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
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

    public Long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Long basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(BigDecimal unitFee) {
        this.unitFee = unitFee;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String getSuitCode() {
        return suitCode;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReturnReason1() {
        return returnReason1;
    }

    public void setReturnReason1(String returnReason1) {
        this.returnReason1 = returnReason1;
    }

    public String getReturnReason2() {
        return returnReason2;
    }

    public void setReturnReason2(String returnReason2) {
        this.returnReason2 = returnReason2;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRetailProductCode() {
        return retailProductCode;
    }

    public void setRetailProductCode(String retailProductCode) {
        this.retailProductCode = retailProductCode;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }
}
