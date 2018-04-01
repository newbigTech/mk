package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;

/**
 * @version 1.0
 * @name SvsalesOrderEntry
 * @Describe 服务销售单行
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Table(name = "HMALL_AS_SVSALES_ENTRY")
public class SvsalesOrderEntry {


    // 主键
    @Id
    @GeneratedValue
    @Column
    private Long asSvsalesEntryId;

    // 服务销售单号
    @Column
    private String svsalesOrderId;

    // 服务单ID
    @Column
    private Long serviceOrderId;

    // 销售订单ID
    @Column
    private Long orderId;

    // 行序号（订单下唯一）
    @Column
    private String lineNumber;

    // 关联订单行（套装）
    @Column
    private String parentLine;

    // 数量
    @Column
    private Long quantity;

    // 单位
    @Column
    private String unit;

    // 单价（吊牌价）
    @Column
    private Double basePrice;

    // 实际价格
    @Column
    private Double unitFee;

    // 订单行应付金额
    @Column
    private Double totalFee;

    // 收费项目
    @Column
    private String chargeType;

    // 是否赠品
    @Column
    private String isGift;

    // 商品编码（定制商品为平台号、常规品为SKU）
    @Column
    private String productId;

    @Transient
    private String productCode;

    // 变式物料号（V码）
    @Column
    private String vproduct;

    // 套装号
    @Column
    private String suitCode;

    // PIN码
    @Column
    private String pin;

    // 配送方式
    @Column
    private String shippingType;

    // 订单行备注
    @Column
    private String note;

    // 退换货原因代码-1级
    @Column
    private String returnReason1;

    // 退换货原因代码-2级
    @Column
    private String returnReason2;

    // 版本号
    @Column
    private Long objectVersionNumber;

    @Column
    private Date creationDate;

    @Column
    private Long createdBy;

    @Column
    private Long lastUpdatedBy;

    @Column
    private Date lastUpdateDate;

    @Column
    private Long lastUpdateLogin;

    @Column
    private Long programApplicationId;

    @Column
    private Long programId;

    @Column
    private Date programUpdateDate;

    @Column
    private Long requestId;

    @Column
    private String attributeCategory;


    public Long getAsSvsalesEntryId() {
        return asSvsalesEntryId;
    }

    public void setAsSvsalesEntryId(Long asSvsalesEntryId) {
        this.asSvsalesEntryId = asSvsalesEntryId;
    }

    public String getSvsalesOrderId() {
        return svsalesOrderId;
    }

    public void setSvsalesOrderId(String svsalesOrderId) {
        this.svsalesOrderId = svsalesOrderId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getParentLine() {
        return parentLine;
    }

    public void setParentLine(String parentLine) {
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

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }
}
