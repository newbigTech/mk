package com.hand.hmall.om.dto;

import java.util.Map;

/**
 * @Describe 调用促销微服务接口的订单行对象
 * @Author xiaoli.yu
 * @Date 2017/8/7 19:29
 */
public class OrderEntryPojo implements java.io.Serializable {
    private Long order;
    private Long orderEntryId;
    private String lineNumber;
    private String estimateDeliveryTime;
    private String estimateConTime;
    //产品编码
    private String product;
    //变体产品编码
    private String vproduct;
    private String shippingType;
    //门店编码
    private String pointOfService;
    private String suitCode;
    private Double basePrice;
    private Integer quantity;
    private Double discountFee;
    private Double discountFeel;
    private Double totalDiscount;
    private Double unitFee;
    private Double totalFee;
    private Double couponFee;
    private String isGift = "N";
    private Double shippingFee;
    private Double installationFee;
    private Double preShippingFee;
    private Double preInstallationFee;
    private String parts;
    private String productPackageSize;
    private Integer returnedQuantity;
    private Map activitie;

    private String activityId;

    public Integer getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Map getActivitie() {
        return activitie;
    }

    public void setActivitie(Map activitie) {
        this.activitie = activitie;
    }

    public String getProductPackageSize() {
        return productPackageSize;
    }

    public void setProductPackageSize(String productPackageSize) {
        this.productPackageSize = productPackageSize;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }


    public String getSuitCode() {
        return suitCode;
    }

    public Double getCouponFee() {
        if (couponFee == null) {
            couponFee = 0D;
        }
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public Double getBasePrice() {
        if (basePrice == null) {
            basePrice = 0D;
        }
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        if (basePrice == null) {
            basePrice = 0.0;
        }
        this.basePrice = basePrice;
    }

    public Integer getQuantity() {
        if (quantity == null) {
            quantity = 0;
        }
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountFee() {
        if (discountFee == null) {
            discountFee = 0D;
        }
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {

        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        if (discountFeel == null) {
            discountFeel = 0D;
        }
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

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
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

    public Double getPreShippingFee() {
        return preShippingFee;
    }

    public void setPreShippingFee(Double preShippingFee) {
        this.preShippingFee = preShippingFee;
    }

    public Double getPreInstallationFee() {
        return preInstallationFee;
    }

    public void setPreInstallationFee(Double preInstallationFee) {
        this.preInstallationFee = preInstallationFee;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }


}
