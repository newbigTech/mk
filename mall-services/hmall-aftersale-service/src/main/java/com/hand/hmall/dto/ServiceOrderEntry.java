package com.hand.hmall.dto;


import javax.persistence.*;
import java.util.Date;

/**
 * @version 1.0
 * @name ServiceOrderEntry
 * @Describe 服务与售后单行实体类
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Entity
@Table(name = "HMALL_AS_SERVICEORDER_ENTRY")
public class ServiceOrderEntry   {


    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_AS_SERVICEORDER_ENTRY_S.nextval from dual")
    private Long serviceOrderEntryId;

    // 服务单头号
    @Column
    private Long serviceOrderId;

    // 配货单头号
    @Column
    private Long consignmentId;

    // 服务单行号
    @Column
    private String code;

    // 订单行序号（订单下唯一）
    @Column
    private Long lineNumber;

    // 关联订单行（套装）
    @Column
    private Long parentLine;

    // 数量
    @Column
    private Long quantity;

    // 单位
    @Column
    private String unit;

    // 单价
    @Column
    private Double basePrice;

    // 订单行优惠金额
    @Column
    private Double discountFee;

    // 订单分摊优惠金额
    @Column
    private Double discountFeel;

    // 实际价格
    @Column
    private Double unitFee;

    // 订单行应付金额
    @Column
    private Double totalFee;

    // 是否赠品
    @Column
    private String isGift;

    // 预计交货时间
    @Column
    private Date estimateDeliveryTime;

    // 预计发货时间
    @Column
    private Date estimateConTime;

    // 商品编码（定制商品为平台号、常规品为SKU）
    @Column
    private Long productId;

    @Transient
    private String productCode;


    @Transient
    private String productName;

    // 变式物料号
    @Column
    private String vproductCode;

    // 套装号
    @Column
    private String sutiCode;

    // 行类型
    @Column
    private String orderEntryType;

    // 订单行ID
    @Column
    private Long orderEntryId;

    // PIN码
    @Column
    private String pin;

    // 运费
    @Column
    private Double shippingFee;

    // 安装费
    @Column
    private Double installationFee;

    // 接口同步标记
    @Column
    private String syncflag;

    // 配送方式
    @Column
    private String shippingType;

    // 仓库/门店
    @Column
    private Long pointOfServiceId;

    // 版本号
    @Column
    private Long objectVersionNumber;

    // 订单行备注
    @Column
    private String note;

    // 工艺审核
    @Column
    private String bomApproved;

    // 原运费
    @Column
    private Double preShippingfee;

    // 原安装费
    @Column
    private Double preInstallationfee;

    // 客服初步原因判定-1级
    @Column
    private String svproReason1;

    // 客服初步原因判定-2级
    @Column
    private String svproReason2;

    // 服务人员判定原因-1级
    @Column
    private String reproReason1;

    // 服务人员判定原因-2级
    @Column
    private String reproReason2;

    // 退款金额
    @Column
    private Double refundFee;

    // 退款总金额
    @Column
    private Double refundSum;

    // 退换货原因代码-1级
    @Column
    private String returnReason1;

    // 退换货原因代码-2级
    @Column
    private String returnReason2;

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

    public Long getServiceOrderEntryId() {
        return serviceOrderEntryId;
    }

    public void setServiceOrderEntryId(Long serviceOrderEntryId) {
        this.serviceOrderEntryId = serviceOrderEntryId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getVproductCode() {
        return vproductCode;
    }

    public void setVproductCode(String vproductCode) {
        this.vproductCode = vproductCode;
    }

    public String getSutiCode() {
        return sutiCode;
    }

    public void setSutiCode(String sutiCode) {
        this.sutiCode = sutiCode;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBomApproved() {
        return bomApproved;
    }

    public void setBomApproved(String bomApproved) {
        this.bomApproved = bomApproved;
    }

    public Double getPreShippingfee() {
        return preShippingfee;
    }

    public void setPreShippingfee(Double preShippingfee) {
        this.preShippingfee = preShippingfee;
    }

    public Double getPreInstallationfee() {
        return preInstallationfee;
    }

    public void setPreInstallationfee(Double preInstallationfee) {
        this.preInstallationfee = preInstallationfee;
    }

    public String getSvproReason1() {
        return svproReason1;
    }

    public void setSvproReason1(String svproReason1) {
        this.svproReason1 = svproReason1;
    }

    public String getSvproReason2() {
        return svproReason2;
    }

    public void setSvproReason2(String svproReason2) {
        this.svproReason2 = svproReason2;
    }

    public String getReproReason1() {
        return reproReason1;
    }

    public void setReproReason1(String reproReason1) {
        this.reproReason1 = reproReason1;
    }

    public String getReproReason2() {
        return reproReason2;
    }

    public void setReproReason2(String reproReason2) {
        this.reproReason2 = reproReason2;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public Double getRefundSum() {
        return refundSum;
    }

    public void setRefundSum(Double refundSum) {
        this.refundSum = refundSum;
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
