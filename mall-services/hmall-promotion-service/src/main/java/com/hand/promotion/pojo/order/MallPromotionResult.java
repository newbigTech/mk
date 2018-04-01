package com.hand.promotion.pojo.order;

import com.hand.promotion.pojo.activity.ActivityDetailPojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 订单计算促销后的pojo
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class MallPromotionResult implements Serializable {
    private String escOrderCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 用户ID
     */
    private String customerId;

    /**
     * 货币码
     */
    private String currencyCode;

    /**
     * 网站编码
     */
    private String websiteCode;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 下单时间
     */
    private String created;

    /**
     * 修改时间
     */
    private String modified;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 国家编码
     */
    private String countryCode;
    /**
     * 州编码
     */
    private String stateCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 街道编码
     */
    private String districtCode;

    /**
     * 支付金额
     */
    private Double paymentAmount;

    /**
     * 订单应付金额
     */
    private Double orderAmount;

    /**
     * 订单净额 不包括运费，安装费
     */
    private Double netAmount;

    /**
     * 促销活动优惠金额
     */
    private Double discountFee;

    /**
     * 物流运费
     */
    private Double postFee;

    /**
     * 快递运费
     */
    private Double epostFee;

    /**
     * 安装费
     */
    private Double fixFee;

    /**
     * 物流运费减免
     */
    private Double postReduce;

    /**
     * 快递运费减免
     */
    private Double epostReduce;

    /**
     * 安装费减免
     */
    private Double fixReduce;

    /**
     * 优惠券优惠金额
     */
    private Double couponFee;

    /**
     * 优惠券与促销活动优惠总额
     */
    private Double totalDiscount;

    /**
     * 是否计算促销结果，Y为根据所选促销信息计算订单金额，N为获取所有可用促销
     */
    private String isCaculate;

    /**
     * 所选优惠券ID
     */
    private String chosenCoupon;

    /**
     * 订单重算字段，保存应用在原订单的优惠券
     */
    private String usedCoupon;

    /**
     * 所选促销Id
     */
    private String chosenPromotion;


    /**
     * 可用优惠券集合
     */
    private List<?> couponList;

    /**
     * 订单行集合
     */
    private List<MallEntryPojo> orderEntryList;

    /**
     * 可选促销促销活动集合
     */
    private List<ActivityDetailPojo> activities;

    /**
     * 使用到的促销活动
     */
    private List<ActivityDetailPojo> usedActivities;

    private Map usedCouponInfo;


    private String isPay = "N";
    private String distributionId = "";
    private String distribution;

    /**
     * 是否执行了霸王券
     */
    private boolean isExclusive;

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

    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public String getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(String usedCoupon) {
        this.usedCoupon = usedCoupon;
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

    public List<MallEntryPojo> getOrderEntryList() {
        return orderEntryList;
    }

    public void setOrderEntryList(List<MallEntryPojo> orderEntryList) {
        this.orderEntryList = orderEntryList;
    }

    public List<ActivityDetailPojo> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDetailPojo> activities) {
        this.activities = activities;
    }

    public List<ActivityDetailPojo> getUsedActivities() {
        return usedActivities;
    }

    public void setUsedActivities(List<ActivityDetailPojo> usedActivities) {
        this.usedActivities = usedActivities;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
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

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    public Map getUsedCouponInfo() {
        return usedCouponInfo;
    }

    public void setUsedCouponInfo(Map usedCouponInfo) {
        this.usedCouponInfo = usedCouponInfo;
    }
}
