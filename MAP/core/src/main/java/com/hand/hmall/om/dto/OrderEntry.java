package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name OrderEntry
 * @description 订单行dto
 * @date 2017年5月26日10:52:23
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ORDER_ENTRY")
public class OrderEntry extends BaseDTO {
    @Id
    @GeneratedValue
    private Long orderEntryId;

    private Long orderId;

    @Transient
    private Long serviceOrderId;

    private String code;

    private Long parentLine;

    private Integer quantity;

    private String unit;

    private Double basePrice;

    private Double discountFee;

    private Double discountFeel;

    private Double unitFee;

    private Double totalFee;

    private String isGift;

    private Date estimateDeliveryTime;

    private Date estimateConTime;

    private Long productId;

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

    private Double internalPrice;

    private String invOccupyFlag;

    private Double couponFee;

    private Double totalDiscount;

    private String odtype;

    private Date oriRequirementTime;

    private Date atpCalculateTime;

    private String logisticsNumber;

    @Transient
    private Double salesAmount;//原始销售金额

    @Transient
    private Double discountAmount;//折扣金额

    @Transient
    private String pointOfServiceCode;

    @Transient
    private String productCode;

    @Transient
    private String consignmentCode;

    @Transient
    private String parentVproductCode;

    @Transient
    private String spiltQuantity;

    @Transient
    private String status;          //发货单状态

    @Transient
    private String locked;      //订单锁定状态

    @Transient
    private String name;

    @Transient
    private String isSuit;//是否套件
    @Transient
    private String entryType;//行类型

    //用于发货单推送ZMALL接口
    @Transient
    private String orderEntryCode;
    //用于发货单推送ZMALL接口
    @Transient
    private String lineLogisticsNumber;

    private String reason1;

    private String reason1Des;

    private String reason2;

    private String reason2Des;

    private String atpStage;
    @Transient
    private String pointOfService;

    private String customerMsg;

    //取消订单行时传递订单头上的优惠券id
    @Transient
    private String chosenCoupon;

    //取消订单行时传递订单头上的促销id
    @Transient
    private String chosenPromotion;

    private String productSize;

    private String productPackedSize;

    private String bomRejectReason;

    //订单推送retail时，获取父行的shippingFee
    @Transient
    private Double pShippingFee;

    //订单推送retail时，获取父行的installationFee
    @Transient
    private Double pInstallationFee;

    //订单推送retail时，获取父行的basePrice
    @Transient
    private Double pBasePrice;

    //订单推送retail时，获取父行的totalFee
    @Transient
    private Double pTotaFee;

    //订单推送retail时，获取父行的lineNumber
    @Transient
    private Long parentLineNumber;

    //订单推送retail时，获取关联的商品的供应商
    @Transient
    private String supplier;

    private Integer returnedQuantity;

    /**
     * 已退货数量 调用促销保价接口使用
     */
    @Transient
    private Integer returnQuantity;

    @Transient
    private Integer unReturnedQuantity;

    private Integer notReturnQuantity;

    private String escLineNumber;

    @Transient
    private OrderEntry spiltOutEntry;

    @Transient
    private List<OmPromotionrule> activities;

    @Transient
    private List<OrderCouponrule> couponList;

    @Transient
    private String lastEventDes;

    @Transient
    private BigDecimal baseTotalAmount;
    @Transient
    private String customSupportType;
    @Transient
    private Long asReturnId;
    @Transient
    private Long swapOrderId;

    @Transient
    private String changeToReturn;
    @Transient
    private String consignmentStatus;

    @Transient
    private String[] activitie;

    @Transient
    private String vproduct;

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String[] getActivitie() {
        return activitie;
    }

    public void setActivitie(String[] activitie) {
        this.activitie = activitie;
    }

    @Transient
    private PromotionResult promotionResult;

    public PromotionResult getPromotionResult() {
        return promotionResult;
    }

    public void setPromotionResult(PromotionResult promotionResult) {
        this.promotionResult = promotionResult;
    }

    public String getChangeToReturn() {
        return changeToReturn;
    }

    public void setChangeToReturn(String changeToReturn) {
        this.changeToReturn = changeToReturn;
    }

    public Long getSwapOrderId() {
        return swapOrderId;
    }

    public void setSwapOrderId(Long swapOrderId) {
        this.swapOrderId = swapOrderId;
    }

    public Long getAsReturnId() {
        return asReturnId;
    }

    public void setAsReturnId(Long asReturnId) {
        this.asReturnId = asReturnId;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBomRejectReason() {
        return bomRejectReason;
    }

    public void setBomRejectReason(String bomRejectReason) {
        this.bomRejectReason = bomRejectReason;
    }

    public List<OrderCouponrule> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<OrderCouponrule> couponList) {
        this.couponList = couponList;
    }

    public List<OmPromotionrule> getActivities() {
        return activities;
    }

    public void setActivities(List<OmPromotionrule> activities) {
        this.activities = activities;
    }

    public Integer getNotReturnQuantity() {
        return notReturnQuantity;
    }

    public void setNotReturnQuantity(Integer notReturnQuantity) {
        this.notReturnQuantity = notReturnQuantity;
    }

    public Integer getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public Integer getUnReturnedQuantity() {
        return unReturnedQuantity;
    }

    public void setUnReturnedQuantity(Integer unReturnedQuantity) {
        this.unReturnedQuantity = unReturnedQuantity;
    }

    public String getParentVproductCode() {
        return parentVproductCode;
    }

    public void setParentVproductCode(String parentVproductCode) {
        this.parentVproductCode = parentVproductCode;
    }

    public Long getParentLineNumber() {
        return parentLineNumber;
    }

    public void setParentLineNumber(Long parentLineNumber) {
        this.parentLineNumber = parentLineNumber;
    }

    public Double getpShippingFee() {
        return pShippingFee;
    }

    public void setpShippingFee(Double pShippingFee) {
        this.pShippingFee = pShippingFee;
    }

    public Double getpInstallationFee() {
        return pInstallationFee;
    }

    public void setpInstallationFee(Double pInstallationFee) {
        this.pInstallationFee = pInstallationFee;
    }

    public Double getpBasePrice() {
        return pBasePrice;
    }

    public void setpBasePrice(Double pBasePrice) {
        this.pBasePrice = pBasePrice;
    }

    public Double getpTotaFee() {
        return pTotaFee;
    }

    public void setpTotaFee(Double pTotaFee) {
        this.pTotaFee = pTotaFee;
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

    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public String getChosenPromotion() {
        return chosenPromotion;
    }

    public void setChosenPromotion(String chosenPromotion) {
        this.chosenPromotion = chosenPromotion;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
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

    private Double preShippingfee;

    private Double preInstallationfee;

    public String getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(String isSuit) {
        this.isSuit = isSuit;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
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

    public String getSpiltQuantity() {
        return spiltQuantity;
    }

    public void setSpiltQuantity(String spiltQuantity) {
        this.spiltQuantity = spiltQuantity;
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

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderEntry getSpiltOutEntry() {
        return spiltOutEntry;
    }

    public void setSpiltOutEntry(OrderEntry spiltOutEntry) {
        this.spiltOutEntry = spiltOutEntry;
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

    public String getConsignmentCode() {
        return consignmentCode;
    }

    public void setConsignmentCode(String consignmentCode) {
        this.consignmentCode = consignmentCode;
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

    public String getPointOfServiceCode() {
        return pointOfServiceCode;
    }

    public void setPointOfServiceCode(String pointOfServiceCode) {
        this.pointOfServiceCode = pointOfServiceCode;
    }

    public String getOrderEntryCode() {
        return orderEntryCode;
    }

    public void setOrderEntryCode(String orderEntryCode) {
        this.orderEntryCode = orderEntryCode;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getCustomerMsg() {
        return customerMsg;
    }

    public void setCustomerMsg(String customerMsg) {
        this.customerMsg = customerMsg;
    }

    public String getEscLineNumber() {
        return escLineNumber;
    }

    public void setEscLineNumber(String escLineNumber) {
        this.escLineNumber = escLineNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderEntry) {
            OrderEntry orderEntry = (OrderEntry) obj;
            if (orderEntry.getOrderEntryId() == null || orderEntryId == null)
                return false;
            else
                return orderEntryId.equals(orderEntry.getOrderEntryId());
        }
        return false;
    }

    public String getLineLogisticsNumber() {
        return lineLogisticsNumber;
    }

    public void setLineLogisticsNumber(String lineLogisticsNumber) {
        this.lineLogisticsNumber = lineLogisticsNumber;
    }

    public String getLastEventDes() {
        return lastEventDes;
    }

    public void setLastEventDes(String lastEventDes) {
        this.lastEventDes = lastEventDes;
    }

    public BigDecimal getBaseTotalAmount() {
        return baseTotalAmount;
    }

    public void setBaseTotalAmount(BigDecimal baseTotalAmount) {
        this.baseTotalAmount = baseTotalAmount;
    }

    public String getCustomSupportType() {
        return customSupportType;
    }

    public void setCustomSupportType(String customSupportType) {
        this.customSupportType = customSupportType;
    }

    public String getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(String consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }
}
