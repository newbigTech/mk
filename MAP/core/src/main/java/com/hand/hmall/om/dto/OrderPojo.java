package com.hand.hmall.om.dto;

import java.util.List;
import java.util.Map;

/**
 * @Describe 调用促销微服务接口的订单头对象
 * @Author xiaoli.yu
 * @Date 2017/8/7 19:29
 */
public class OrderPojo implements java.io.Serializable, Cloneable {
    private String escOrderCode;
    private String orderStatus;
    private String customerId;
    private String currencyCode;
    private String websiteCode;
    private String channelCode;
    private String storeCode;
    private String shippingType;
    private String created;
    private String modified;
    private String receiverName;
    private String countryCode;
    private String stateCode;
    private String cityCode;
    private String districtCode;
    private Double paymentAmount = 0.0;
    private Double orderAmount = 0.0;
    private Double netAmount = 0.0;
    private Double discountFee = 0.0;
    private Double postFee = 0.0;
    private Double epostFee = 0.0;
    private Double fixFee = 0.0;
    private Double postReduce = 0.0;
    private Double epostReduce = 0.0;
    private Double fixReduce = 0.0;
    private Double couponFee = 0.0;
    private Double totalDiscount = 0.0;
    //是否计算促销结果，Y为根据所选促销信息计算订单金额，N为获取所有可用促销
    private String isCaculate;
    //所选优惠券ID
    private String chosenCoupon;
    //所选促销Id
    private String chosenPromotion;
    //所有促销
    private List<?> couponList;
    private List<OrderEntryPojo> orderEntryList;
    private List<Map> activities;
    private boolean checkedCoupon = false;
    private boolean checkedActivity = false;
    private String isPay = "N";
    private String distributionId = "";
    private String distribution;


    private String usedCoupon;

    public String getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(String usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
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

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public Double getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(Double epostFee) {
        this.epostFee = epostFee;
    }

    public Double getFixFee() {
        return fixFee;
    }

    public void setFixFee(Double fixFee) {
        this.fixFee = fixFee;
    }

    public Double getPostReduce() {
        return postReduce;
    }

    public void setPostReduce(Double postReduce) {
        this.postReduce = postReduce;
    }

    public Double getEpostReduce() {
        return epostReduce;
    }

    public void setEpostReduce(Double epostReduce) {
        this.epostReduce = epostReduce;
    }

    public Double getFixReduce() {
        return fixReduce;
    }

    public void setFixReduce(Double fixReduce) {
        this.fixReduce = fixReduce;
    }

    public List<OrderEntryPojo> getOrderEntryList() {
        return orderEntryList;
    }

    public void setOrderEntryList(List<OrderEntryPojo> orderEntryList) {
        this.orderEntryList = orderEntryList;
    }

    public List<Map> getActivities() {
        return activities;
    }

    public void setActivities(List<Map> activities) {
        this.activities = activities;
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

    public List<?> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<?> couponList) {
        this.couponList = couponList;
    }


    public boolean isCheckedCoupon() {
        return checkedCoupon;
    }

    public void setCheckedCoupon(boolean checkedCoupon) {
        this.checkedCoupon = checkedCoupon;
    }

    public boolean isCheckedActivity() {
        return checkedActivity;
    }

    public void setCheckedActivity(boolean checkedActivity) {
        this.checkedActivity = checkedActivity;
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


    public String getIsCaculate() {
        return isCaculate;
    }

    public void setIsCaculate(String isCaculate) {
        this.isCaculate = isCaculate;
    }

}
