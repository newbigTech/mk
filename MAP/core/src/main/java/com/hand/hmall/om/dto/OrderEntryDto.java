package com.hand.hmall.om.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @describe: 订单同步商城专用 DTO，
 * @Author: shoupeng.wei@hand-china.com
 * @Create at: 2017年9月9日10:33:03
 *
 */
@Table(name = "HMALL_OM_ORDER_ENTRY")
public class OrderEntryDto {
    @JsonIgnoreProperties
    private Long orderEntryId;
    @Transient
    private String productCode;

    private BigDecimal quantity;

    private BigDecimal basePrice;

    private BigDecimal discountFee;

    private BigDecimal discountFeel;

    private String unit;

    private BigDecimal unitFee;

    private BigDecimal totalFee;

    private String vproductCode;

    private String suitCode;

    private String pin;

    private BigDecimal shippingFee;

    private BigDecimal installationFee;

    private BigDecimal preShippingfee;

    private BigDecimal preInstallationfee;

    private Long lineNumber;

    private String shippingType;

    private String orderEntryStatus;

    private String odtype;

    @Transient
    private String pointOfServiceCode;

    private String isGift;

    private String estimateDeliveryTime;

    private String estimateConTime;

    private BigDecimal couponFee;

    private BigDecimal totalDiscount;

    private String productSize;

    private String productPackedSize;

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public String getOrderEntryStatus() {
        return orderEntryStatus;
    }

    public void setOrderEntryStatus(String orderEntryStatus) {
        this.orderEntryStatus = orderEntryStatus;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public BigDecimal getDiscountFeel() {
        return discountFeel;
    }

    public void setDiscountFeel(BigDecimal discountFeel) {
        this.discountFeel = discountFeel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getVproductCode() {
        return vproductCode;
    }

    public void setVproductCode(String vproductCode) {
        this.vproductCode = vproductCode;
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

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(BigDecimal installationFee) {
        this.installationFee = installationFee;
    }

    public BigDecimal getPreShippingfee() {
        return preShippingfee;
    }

    public void setPreShippingfee(BigDecimal preShippingfee) {
        this.preShippingfee = preShippingfee;
    }

    public BigDecimal getPreInstallationfee() {
        return preInstallationfee;
    }

    public void setPreInstallationfee(BigDecimal preInstallationfee) {
        this.preInstallationfee = preInstallationfee;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPointOfServiceCode() {
        return pointOfServiceCode;
    }

    public void setPointOfServiceCode(String pointOfServiceCode) {
        this.pointOfServiceCode = pointOfServiceCode;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public String getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(String estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getEstimateConTime() {
        return estimateConTime;
    }

    public void setEstimateConTime(String estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    public BigDecimal getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(BigDecimal couponFee) {
        this.couponFee = couponFee;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductPackedSize() {
        return productPackedSize;
    }

    public void setProductPackedSize(String productPackedSize) {
        this.productPackedSize = productPackedSize;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }
}
