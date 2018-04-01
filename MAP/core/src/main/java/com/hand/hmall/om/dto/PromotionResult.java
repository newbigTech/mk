package com.hand.hmall.om.dto;

import javax.persistence.Transient;
import java.util.List;

/**促销返回结果实体
 * Created by qinzhipeng on 2017/12/19.
 */
public class PromotionResult {

    private Double couponFee;
    private Double discountFee;
    private String districtCode;
    private Double epostFee;
    private String cityCode;
    private String shippingType;
    private String orderStatus;
    private Double paymentAmount;
    private Double orderAmount;
    private String customerId;
    private String websiteCode;
    private String isCaculate;
    private Boolean checkedCoupon;
    private Boolean checkedActivity;
    private String isPay;
    private Integer quantity;
    private String chosenCoupon;
    private Double netAmount;
    private Double postFee;
    private Double currentAmount;
    private String chosenPromotion;
    private Double totalDiscount;
    private Double fixFee;
    private List<OrderEntryResult> orderEntryList;
    private List<OmPromotionrule> activities;
    private List<OrderCouponrule> couponList;

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Double getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(Double epostFee) {
        this.epostFee = epostFee;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getIsCaculate() {
        return isCaculate;
    }

    public void setIsCaculate(String isCaculate) {
        this.isCaculate = isCaculate;
    }

    public Boolean getCheckedCoupon() {
        return checkedCoupon;
    }

    public void setCheckedCoupon(Boolean checkedCoupon) {
        this.checkedCoupon = checkedCoupon;
    }

    public Boolean getCheckedActivity() {
        return checkedActivity;
    }

    public void setCheckedActivity(Boolean checkedActivity) {
        this.checkedActivity = checkedActivity;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getChosenPromotion() {
        return chosenPromotion;
    }

    public void setChosenPromotion(String chosenPromotion) {
        this.chosenPromotion = chosenPromotion;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getFixFee() {
        return fixFee;
    }

    public void setFixFee(Double fixFee) {
        this.fixFee = fixFee;
    }

    public List<OrderEntryResult> getOrderEntryList() {
        return orderEntryList;
    }

    public void setOrderEntryList(List<OrderEntryResult> orderEntryList) {
        this.orderEntryList = orderEntryList;
    }

    public List<OmPromotionrule> getActivities() {
        return activities;
    }

    public void setActivities(List<OmPromotionrule> activities) {
        this.activities = activities;
    }

    public List<OrderCouponrule> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<OrderCouponrule> couponList) {
        this.couponList = couponList;
    }

    public static class OrderEntryResult {
        private Double couponFee;
        private Double discountFee;
        private String pointOfServiceCode;
        private String customSupportType;
        private String shippingType;
        private Double unitFee;
        private String isGift;
        private Double basePrice;
        private String vproduct;
        private String product;
        private Integer quantity;
        private Double installationFee;
        private Integer refundQuantity;
        private Double shippingFee;
        private Double totalFee;
        private Double totalDiscount;
        private Double discountFeel;
        private String lineNumber;

        public Double getCouponFee() {
            return couponFee;
        }

        public void setCouponFee(Double couponFee) {
            this.couponFee = couponFee;
        }

        public Double getDiscountFee() {
            return discountFee;
        }

        public void setDiscountFee(Double discountFee) {
            this.discountFee = discountFee;
        }

        public String getPointOfServiceCode() {
            return pointOfServiceCode;
        }

        public void setPointOfServiceCode(String pointOfServiceCode) {
            this.pointOfServiceCode = pointOfServiceCode;
        }

        public String getCustomSupportType() {
            return customSupportType;
        }

        public void setCustomSupportType(String customSupportType) {
            this.customSupportType = customSupportType;
        }

        public String getShippingType() {
            return shippingType;
        }

        public void setShippingType(String shippingType) {
            this.shippingType = shippingType;
        }

        public Double getUnitFee() {
            return unitFee;
        }

        public void setUnitFee(Double unitFee) {
            this.unitFee = unitFee;
        }

        public String getIsGift() {
            return isGift;
        }

        public void setIsGift(String isGift) {
            this.isGift = isGift;
        }

        public Double getBasePrice() {
            return basePrice;
        }

        public void setBasePrice(Double basePrice) {
            this.basePrice = basePrice;
        }

        public String getVproduct() {
            return vproduct;
        }

        public void setVproduct(String vproduct) {
            this.vproduct = vproduct;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Double getInstallationFee() {
            return installationFee;
        }

        public void setInstallationFee(Double installationFee) {
            this.installationFee = installationFee;
        }

        public Integer getRefundQuantity() {
            return refundQuantity;
        }

        public void setRefundQuantity(Integer refundQuantity) {
            this.refundQuantity = refundQuantity;
        }

        public Double getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(Double shippingFee) {
            this.shippingFee = shippingFee;
        }

        public Double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(Double totalFee) {
            this.totalFee = totalFee;
        }

        public Double getTotalDiscount() {
            return totalDiscount;
        }

        public void setTotalDiscount(Double totalDiscount) {
            this.totalDiscount = totalDiscount;
        }

        public Double getDiscountFeel() {
            return discountFeel;
        }

        public void setDiscountFeel(Double discountFeel) {
            this.discountFeel = discountFeel;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
