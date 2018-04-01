package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
/**
 * @author 梅新养
 * @name:HmallOmOrderEntry
 * @Description:HMALL_OM_ORDER_ENTRY 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "HMALL_OM_ORDER_ENTRY")
public class HmallOmOrderEntry {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_ORDER_ENTRY_S.nextval from dual")
    private Long orderEntryId;

    /**
     * 订单头号
     */
    private Long orderId;

    /**
     * 配货单头号
     */
    private Long consignmentId;

    /**
     * 订单行号
     */
    private String code;

    /**
     * 关联订单行（套装）
     */
    private Long parentLine;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private Double basePrice;

    /**
     * 实际价格
     */
    private Double unitFee;

    /**
     * 订单行应付金额
     */
    private Double totalFee;

    /**
     * 是否赠品
     */
    private String isGift;

    /**
     * 预计交货时间
     */
    private Date estimateDeliveryTime;

    /**
     * 预计发货时间
     */
    private Date estimateConTime;

    /**
     * 商品编码（定制商品为平台号、常规品为SKU）
     */
    private Long productId;

    /**
     * 变式物料号
     */
    private String vproductCode;

    /**
     * 套装号
     */
    private String suitCode;

    /**
     * 行类型
     */
    private String orderEntryType;

    /**
     * PIN码
     */
    private String pin;

    /**
     * 运费
     */
    private Double shippingFee;

    /**
     * 安装费
     */
    private Double installationFee;

    /**
     * 运费减免
     */
    @Column(name = "PRE_SHIPPINGFEE")
    private Double preShippingFee;

    /**
     * 安装费减免
     */
    @Column(name = "PRE_INSTALLATIONFEE")
    private Double preInstallationFee;

    /**
     * 接口传输标示
     */
    private String syncflag;

    /**
     * 版本号
     */
    private Double objectVersionNumber;

    /**
     * null
     */
    private Date creationDate;

    /**
     * null
     */
    private Long createdBy;

    /**
     * null
     */
    private Long lastUpdatedBy;

    /**
     * null
     */
    private Date lastUpdateDate;

    /**
     * null
     */
    private Long lastUpdateLogin;

    /**
     * null
     */
    private Long programApplicationId;

    /**
     * null
     */
    private Long programId;

    /**
     * null
     */
    private Date programUpdateDate;

    /**
     * null
     */
    private Long requestId;

    /**
     * null
     */
    private String attributeCategory;

    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 仓库/门店
     */
    private Long pointOfServiceId;

    /**
     * 订单行序号（订单下唯一）
     */
    private Long lineNumber;

    /**
     * 订单行优惠金额
     */
    private Double discountFee;

    /**
     * 订单分摊优惠金额
     */
    private Double discountFeel;

    /**
     * 订单行优惠券优惠金额
     */
    private Double couponFee;

    /**
     * 优惠总金额
     */
    private Double totalDiscount;

    /**
     * 产品净尺寸
     */
    private String productSize;

    /**
     * 产品包装尺寸
     */

    private String productPackedSize;


    /**
     * 订单行状态
     */
    private String  orderEntryStatus;

    /**
     * 工艺审核
     */
    private String bomApproved;

    /**
     * 原因代码
     */
    private String reason1;

    /**
     * 原因描述
     */
    private String reason1Des;

    /**
     * 最终原因代码
     */
    private String reason2;

    /**
     * 最终原因描述
     */
    private String reason2Des;

    /**
     * 售后单据的ID
     */
    private Long serviceOrderId;

    /**
     * ATP检查阶段
     */
    private String atpStage;

    /**
     * 采购价格
     */
    private Double internalPrice;

    /**
     * 库存占用标记
     */
    private String invOccupyFlag;

    /**
     * 退货标记
     */
    private String returnFlag;

    /**
     * 换货标记
     */
    private String exchangeFlag;

    /**
     * 定制信息
     */
    private String customerMsg;

    /**
     * 物流/快递单号
     */
    private String logisticsNumber;

    /**
     * 频道
     */
    private String odtype;

    /**
     * 客户原始需求日期
     */
    private Date oriRequirementTime;

    /**
     * ATP答复最早交期
     */
    private Date atpCalculateTime;

    /**
     * 平台订单行号
     */
    private String escLineNumber;
    /**
     * 定制来源
     */
    @Transient
    private String customType;
    /**
     * 商品集合

     */
    @Transient
    private List<HmallMstProduct> productList;

    @Transient
    private String productCode;

    @Transient
    private String pointOfServiceCode;

    /**
     * 行促销
     */

    @Transient
    private HmallOmPromotionRule orderEntryPromotion;

    public HmallOmPromotionRule getOrderEntryPromotion() { return orderEntryPromotion; }

    public void setOrderEntryPromotion(HmallOmPromotionRule orderEntryPromotion) { this.orderEntryPromotion = orderEntryPromotion; }

    public String getPointOfServiceCode() {
        return pointOfServiceCode;
    }

    public void setPointOfServiceCode(String pointOfServiceCode) {
        this.pointOfServiceCode = pointOfServiceCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public List<HmallMstProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<HmallMstProduct> productList) {
        this.productList = productList;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * 主键
     *
     * @return ORDER_ENTRY_ID 主键
     */
    public Long getOrderEntryId() {
        return orderEntryId;
    }

    /**
     * 主键
     *
     * @param orderEntryId 主键
     */
    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    /**
     * 订单头号
     *
     * @return ORDER_ID 订单头号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 订单头号
     *
     * @param orderId 订单头号
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 配货单头号
     * @return
     */

    public Long getConsignmentId() {
        return consignmentId;
    }

    /**
     * 配货单头号
     * @param consignmentId 配货单头号
     */
    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    /**
     * 订单行号
     *
     * @return CODE 订单行号
     */
    public String getCode() {
        return code;
    }

    /**
     * 订单行号
     *
     * @param code 订单行号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 关联订单行（套装）
     *
     * @return PARENT_LINE 关联订单行（套装）
     */
    public Long getParentLine() {
        return parentLine;
    }

    /**
     * 关联订单行（套装）
     *
     * @param parentLine 关联订单行（套装）
     */
    public void setParentLine(Long parentLine) {
        this.parentLine = parentLine;
    }

    /**
     * 数量
     *
     * @return QUANTITY 数量
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * 单位
     *
     * @return UNIT 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 单价
     *
     * @return BASE_PRICE 单价
     */
    public Double getBasePrice() {
        return basePrice;
    }

    /**
     * 单价
     *
     * @param basePrice 单价
     */
    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * 订单行优惠金额
     *
     * @return DISCOUNT_FEE 订单行优惠金额
     */
    public Double getDiscountFee() {
        return discountFee;
    }

    /**
     * 订单行优惠金额
     *
     * @param discountFee 订单行优惠金额
     */
    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    /**
     * 订单分摊优惠金额
     *
     * @return DISCOUNT_FEEL 订单分摊优惠金额
     */
    public Double getDiscountFeel() {
        return discountFeel;
    }

    /**
     * 订单分摊优惠金额
     *
     * @param discountFeel 订单分摊优惠金额
     */
    public void setDiscountFeel(Double discountFeel) {
        this.discountFeel = discountFeel;
    }

    /**
     * 实际价格
     *
     * @return UNIT_FEE 实际价格
     */
    public Double getUnitFee() {
        return unitFee;
    }

    /**
     * 实际价格
     *
     * @param unitFee 实际价格
     */
    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    /**
     * 订单行应付金额
     *
     * @return TOTAL_FEE 订单行应付金额
     */
    public Double getTotalFee() {
        return totalFee;
    }

    /**
     * 订单行应付金额
     *
     * @param totalFee 订单行应付金额
     */
    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * 是否赠品
     *
     * @return IS_GIFT 是否赠品
     */
    public String getIsGift() {
        return isGift;
    }

    /**
     * 是否赠品
     *
     * @param isGift 是否赠品
     */
    public void setIsGift(String isGift) {
        this.isGift = isGift == null ? null : isGift.trim();
    }

    /**
     * 预计交货时间
     *
     * @return ESTIMATE_DELIVERY_TIME 预计交货时间
     */
    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    /**
     * 预计交货时间
     *
     * @param estimateDeliveryTime 预计交货时间
     */
    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    /**
     * 预计发货时间
     *
     * @return ESTIMATE_CON_TIME 预计发货时间
     */
    public Date getEstimateConTime() {
        return estimateConTime;
    }

    /**
     * 预计发货时间
     *
     * @param estimateConTime 预计发货时间
     */
    public void setEstimateConTime(Date estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    /**
     * 商品编码（定制商品为平台号、常规品为SKU）
     *
     * @return PRODUCT_ID 商品编码（定制商品为平台号、常规品为SKU）
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 商品编码（定制商品为平台号、常规品为SKU）
     *
     * @param productId 商品编码（定制商品为平台号、常规品为SKU）
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 变式物料号
     *
     * @return VPRODUCT_CODE 变式物料号
     */
    public String getVproductCode() {
        return vproductCode;
    }

    /**
     * 变式物料号
     *
     * @param vproductCode 变式物料号
     */
    public void setVproductCode(String vproductCode) {
        this.vproductCode = vproductCode == null ? null : vproductCode.trim();
    }

    /**
     * 套装号
     *
     * @return SUTI_CODE 套装号
     */
    public String getSuitCode() {
        return suitCode;
    }

    /**
     * 套装号
     *
     * @param suitCode 套装号
     */
    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode == null ? null : suitCode.trim();
    }

    /**
     * 行类型
     *
     * @return ORDER_ENTRY_TYPE 行类型
     */
    public String getOrderEntryType() {
        return orderEntryType;
    }

    /**
     * 行类型
     *
     * @param orderEntryType 行类型
     */
    public void setOrderEntryType(String orderEntryType) {
        this.orderEntryType = orderEntryType == null ? null : orderEntryType.trim();
    }

    /**
     * PIN码
     *
     * @return PIN PIN码
     */
    public String getPin() {
        return pin;
    }

    /**
     * PIN码
     *
     * @param pin PIN码
     */
    public void setPin(String pin) {
        this.pin = pin == null ? null : pin.trim();
    }

    /**
     * 运费
     *
     * @return SHIPPING_FEE 运费
     */
    public Double getShippingFee() {
        return shippingFee;
    }

    /**
     * 运费
     *
     * @param shippingFee 运费
     */
    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * 安装费
     *
     * @return INSTALLATION_FEE 安装费
     */
    public Double getInstallationFee() {
        return installationFee;
    }

    /**
     * 安装费
     *
     * @param installationFee 安装费
     */

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    /**
     * 运费减免
     *
     * @return PRE_SHIPPING_FEE 运费减免
     */
    public Double getPreShippingFee() {
        return preShippingFee;
    }

    /**
     * 运费减免
     *
     * @param preShippingFee 运费减免
     */
    public void setPreShippingFee(Double preShippingFee) {
        this.preShippingFee = preShippingFee;
    }

    /**
     * 安装费减免
     *
     * @return PRE_INSTALLATION_FEE 安装费减免
     */
    public Double getPreInstallationFee() {
        return preInstallationFee;
    }

    /**
     * 安装费减免
     *
     * @param preInstallationFee 安装费减免
     */
    public void setPreInstallationFee(Double preInstallationFee) {
        this.preInstallationFee = preInstallationFee;
    }


    /**
     * 接口传输标示
     *
     * @return SYNCFLAG 接口传输标示
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口传输标示
     *
     * @param syncflag 接口传输标示
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }

    /**
     * 版本号
     *
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     *
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     *
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     *
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     *
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     *
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     *
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     *
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     *
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     *
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     *
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     *
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     *
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     *
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     *
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     *
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     *
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     *
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     *
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }


    /**
     * 配送方式
     *
     * @return SHIPPING_TYPE 配送方式
     */
    public String getShippingType() {
        return shippingType;
    }

    /**
     * 配送方式
     *
     * @param shippingType 配送方式
     */
    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    /**
     * 仓库/门店
     *
     * @return POINT_OF_SERVICE_ID 仓库/门店
     */
    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    /**
     * 仓库/门店
     *
     * @param pointOfServiceId 仓库/门店
     */
    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
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

    public String getOrderEntryStatus() {
        return orderEntryStatus;
    }

    public void setOrderEntryStatus(String orderEntryStatus) {
        this.orderEntryStatus = orderEntryStatus;
    }

    public String getBomApproved() {
        return bomApproved;
    }

    public void setBomApproved(String bomApproved) {
        this.bomApproved = bomApproved;
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

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getAtpStage() {
        return atpStage;
    }

    public void setAtpStage(String atpStage) {
        this.atpStage = atpStage;
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

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag;
    }

    public String getCustomerMsg() {
        return customerMsg;
    }

    public void setCustomerMsg(String customerMsg) {
        this.customerMsg = customerMsg;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
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

    public String getEscLineNumber() {
        return escLineNumber;
    }

    public void setEscLineNumber(String escLineNumber) {
        this.escLineNumber = escLineNumber;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }
}