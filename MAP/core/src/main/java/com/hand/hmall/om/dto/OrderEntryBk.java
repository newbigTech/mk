package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name OrderEntryBk
 * @description 订单行备份Dto
 * @date 2017/8/4 10:33
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ORDER_ENTRY_BK")
public class OrderEntryBk extends BaseDTO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
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
     * 订单行序号（订单下唯一）
     */
    private Long lineNumber;

    /**
     * 关联订单行（套装）
     */
    private Long prarentLine;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private Double basePrice;

    /**
     * 订单行优惠金额
     */
    private Double discountFee;

    /**
     * 订单分摊优惠金额
     */
    private Double discountFeel;

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
    private String sutiCode;

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
     * 接口同步标记
     */
    private String syncflag;

    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 仓库/门店
     */
    private Long pointOfServiceId;


    /**
     * 订单行备注
     */
    private String note;

    /**
     * 工艺审核
     */
    private String bomApproved;

    /**
     * 原运费
     */
    private Double preShippingfee;

    /**
     * 原安装费
     */
    private Double preInstallationfee;

    /**
     * 原因代码
     */
    private String svproReason1;

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
     * 门店/仓库
     */
    private String pointOfService;

    /**
     * 订单行状态
     */
    private String orderEntryStatus;

    /**
     * 采购价格
     */
    private Double internalPrice;

    /**
     * 库存占用标记
     */
    private String invOccupyFlag;

    private Date oriRequirementTime;

    private Date atpCalculateTime;

    @Transient
    private String name;

    @Transient
    private String productCode;

    @Transient
    private String consignmentCode;

    @Transient
    private String lastEventDes;

    @Transient
    private BigInteger baseTotalAmount;

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

    public String getConsignmentCode() {
        return consignmentCode;
    }

    public void setConsignmentCode(String consignmentCode) {
        this.consignmentCode = consignmentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

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

    public Long getPrarentLine() {
        return prarentLine;
    }

    public void setPrarentLine(Long prarentLine) {
        this.prarentLine = prarentLine;
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

    public String getSutiCode() {
        return sutiCode;
    }

    public void setSutiCode(String sutiCode) {
        this.sutiCode = sutiCode;
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

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
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

    public String getLastEventDes() {
        return lastEventDes;
    }

    public void setLastEventDes(String lastEventDes) {
        this.lastEventDes = lastEventDes;
    }

    public BigInteger getBaseTotalAmount() {
        return baseTotalAmount;
    }

    public void setBaseTotalAmount(BigInteger baseTotalAmount) {
        this.baseTotalAmount = baseTotalAmount;
    }
}
