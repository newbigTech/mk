package com.hand.hmall.om.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name OrderEntryCompare
 * @description 订单行比较
 * @date
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ORDER_ENTRY")
public class OrderEntryCompare extends BaseDTO {
    @Id
    @GeneratedValue
    private Long orderEntryId;

    private Long orderId;
    @ExcelVOAttribute(name = "订单行号", column = "B", isExport = true)
    private String code;

    private Long parentLine;

    private Integer quantity;

    private String unit;
    @ExcelVOAttribute(name = "前台销售价", column = "H", isExport = true)
    private Double basePrice;

    private Double discountFee;

    private Double discountFeel;

    private Double unitFee;

    private Double totalFee;

    private String isGift;

    private Date estimateDeliveryTime;

    private Date estimateConTime;

    private Long productId;
    @ExcelVOAttribute(name = "V码", column = "C", isExport = true)
    private String vproductCode;

    private String suitCode;

    private String orderEntryType;

    private String pin;

    private Double shippingFee;

    private Double installationFee;

    private String syncflag;

    private String note;

    private String shippingType;

    private Long pointOfServiceId;

    private Long consignmentId;

    private Long lineNumber;

    private String bomApproved;

    private String orderEntryStatus;
    @ExcelVOAttribute(name = "采购价格", column = "N", isExport = true)
    private Double internalPrice;

    private String invOccupyFlag;

    private Double couponFee;

    private Double totalDiscount;

    private String odtype;

    private Date oriRequirementTime;

    private Date atpCalculateTime;

    private String logisticsNumber;

    private String reason1;

    private String reason1Des;

    private String reason2;

    private String reason2Des;

    private String atpStage;

    private String customerMsg;
    @ExcelVOAttribute(name = "前台产品外形尺寸", column = "J", isExport = true)
    private String productSize;
    @ExcelVOAttribute(name = "前台包装尺寸", column = "K", isExport = true)
    private String productPackedSize;

    private Integer returnedQuantity;

    private Integer notReturnQuantity;

    private String escLineNumber;
    @ExcelVOAttribute(name = "订单号", column = "A", isExport = true)
    @Transient
    private String orderCode;
    @ExcelVOAttribute(name = "商品编码", column = "D", isExport = true)
    @Transient
    private String productCode;
    @ExcelVOAttribute(name = "商品名称", column = "E", isExport = true)
    @Transient
    private String productName;
    @ExcelVOAttribute(name = "中台销售价", column = "I", isExport = true)
    @Transient
    private Double platformSalesPrice;
    @ExcelVOAttribute(name = "材质（价签）", column = "F", isExport = true)
    @Transient
    private String texture;
    @ExcelVOAttribute(name = "材质（说明书）", column = "G", isExport = true)
    @Transient
    private String textureSpecification;
    @ExcelVOAttribute(name = "中台产品外形尺寸", column = "L", isExport = true)
    @Transient
    private String platformProductSize;
    @ExcelVOAttribute(name = "中台包装尺寸", column = "M", isExport = true)
    @Transient
    private String platformProductPackedSize;
    @Transient
    private String platformCode;

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
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

    public Long getParentLine() {
        return parentLine;
    }

    public void setParentLine(Long parentLine) {
        this.parentLine = parentLine;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        return discountFeel;
    }

    public void setDiscountFeel(Double discountFeel) {
        this.discountFeel = discountFeel;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public Date getEstimateConTime() {
        return estimateConTime;
    }

    public void setEstimateConTime(Date estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getOrderEntryType() {
        return orderEntryType;
    }

    public void setOrderEntryType(String orderEntryType) {
        this.orderEntryType = orderEntryType;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getBomApproved() {
        return bomApproved;
    }

    public void setBomApproved(String bomApproved) {
        this.bomApproved = bomApproved;
    }

    public String getOrderEntryStatus() {
        return orderEntryStatus;
    }

    public void setOrderEntryStatus(String orderEntryStatus) {
        this.orderEntryStatus = orderEntryStatus;
    }

    public Double getInternalPrice() {
        return internalPrice;
    }

    public void setInternalPrice(Double internalPrice) {
        this.internalPrice = internalPrice;
    }

    public String getInvOccupyFlag() {
        return invOccupyFlag;
    }

    public void setInvOccupyFlag(String invOccupyFlag) {
        this.invOccupyFlag = invOccupyFlag;
    }

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public Date getOriRequirementTime() {
        return oriRequirementTime;
    }

    public void setOriRequirementTime(Date oriRequirementTime) {
        this.oriRequirementTime = oriRequirementTime;
    }

    public Date getAtpCalculateTime() {
        return atpCalculateTime;
    }

    public void setAtpCalculateTime(Date atpCalculateTime) {
        this.atpCalculateTime = atpCalculateTime;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getReason1() {
        return reason1;
    }

    public void setReason1(String reason1) {
        this.reason1 = reason1;
    }

    public String getReason1Des() {
        return reason1Des;
    }

    public void setReason1Des(String reason1Des) {
        this.reason1Des = reason1Des;
    }

    public String getReason2() {
        return reason2;
    }

    public void setReason2(String reason2) {
        this.reason2 = reason2;
    }

    public String getReason2Des() {
        return reason2Des;
    }

    public void setReason2Des(String reason2Des) {
        this.reason2Des = reason2Des;
    }

    public String getAtpStage() {
        return atpStage;
    }

    public void setAtpStage(String atpStage) {
        this.atpStage = atpStage;
    }

    public String getCustomerMsg() {
        return customerMsg;
    }

    public void setCustomerMsg(String customerMsg) {
        this.customerMsg = customerMsg;
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

    public Integer getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public Integer getNotReturnQuantity() {
        return notReturnQuantity;
    }

    public void setNotReturnQuantity(Integer notReturnQuantity) {
        this.notReturnQuantity = notReturnQuantity;
    }

    public String getEscLineNumber() {
        return escLineNumber;
    }

    public void setEscLineNumber(String escLineNumber) {
        this.escLineNumber = escLineNumber;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public Double getPlatformSalesPrice() {
        return platformSalesPrice;
    }

    public void setPlatformSalesPrice(Double platformSalesPrice) {
        this.platformSalesPrice = platformSalesPrice;
    }

    public String getPlatformProductSize() {
        return platformProductSize;
    }

    public void setPlatformProductSize(String platformProductSize) {
        this.platformProductSize = platformProductSize;
    }

    public String getPlatformProductPackedSize() {
        return platformProductPackedSize;
    }

    public void setPlatformProductPackedSize(String platformProductPackedSize) {
        this.platformProductPackedSize = platformProductPackedSize;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getTextureSpecification() {
        return textureSpecification;
    }

    public void setTextureSpecification(String textureSpecification) {
        this.textureSpecification = textureSpecification;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderEntryCompare) {
            OrderEntryCompare orderEntry = (OrderEntryCompare) obj;
            if (orderEntry.getOrderEntryId() == null || orderEntryId == null)
                return false;
            else
                return orderEntryId.equals(orderEntry.getOrderEntryId());
        }
        return false;
    }
}
