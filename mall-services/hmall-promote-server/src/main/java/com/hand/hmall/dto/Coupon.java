package com.hand.hmall.dto;


public class Coupon {

    //优惠券id，唯一标识符
    private String id;
    //优惠券主标志-用于版本控制
    private String couponId;
    //优惠券码
    private String couponCode;
    //优惠券名称
    private String couponName;
    //活动范围
    private String range;
    //优惠券发放类型
    private String type;
    //优惠
    private double benefit;
    //状态
    private String status;
    //优惠券最大兑现数
    private int maxRedemption;
    //每个客户最大兑现数
    private int maxRedemptionPerCustomer;
    //是否可以叠加
    private String isOverlay;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;
    //创建时间
    private String creationTime;
    //创建者
    private String creationAt;
    //更新者
    private String updateAt;
    //副标志-用于版本控制
    private String releaseId;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxRedemption() {
        return maxRedemption;
    }

    public void setMaxRedemption(int maxRedemption) {
        this.maxRedemption = maxRedemption;
    }

    public int getMaxRedemptionPerCustomer() {
        return maxRedemptionPerCustomer;
    }

    public void setMaxRedemptionPerCustomer(int maxRedemptionPerCustomer) {
        this.maxRedemptionPerCustomer = maxRedemptionPerCustomer;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreationAt() {
        return creationAt;
    }

    public void setCreationAt(String creationAt) {
        this.creationAt = creationAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }
}
