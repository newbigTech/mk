package com.hand.promotion.pojo.coupon;


import javax.validation.constraints.NotNull;

/**
 * 优惠券
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class CouponsPojo implements java.io.Serializable{
    /**
     * 主键
     */
    private String Id;

    /**
     * 优惠券编码id,用于跟踪优惠券
     */
    private String couponId;

    /**
     * 优惠券编码,全局唯一,用于优惠券领取,发放
     */
    @NotNull
    private String couponCode;

    /**
     * 优惠券名称
     */
    @NotNull
    private String couponName;

    /**
     * 优惠券描述
     */
    @NotNull
    private String couponDes;

    /**
     * 优惠券面值
     */
    @NotNull
    private String benefit;

    /**
     * 优惠券购买金额
     */
    private Double payFee;

    /**
     * 优惠券发放类型,取值自枚举类CouponType
     */
    @NotNull
    private String type;

    /**
     * 客户最大兑换数
     */
    @NotNull
    private Integer maxRedemptionPerCustomer;

    /**
     * 优惠券最大兑换次数
     */
    @NotNull
    private Integer maxRedemption;

    /**
     * 允许领取时间
     */
    @NotNull
    private Long getStartDate;

    /**
     * 允许领取结束时间
     */
    @NotNull
    private Long getEndDate;

    /**
     * 优惠券结束时间
     */
    @NotNull
    private Long endDate;

    /**
     * 优惠券生效时间
     */
    @NotNull
    private Long startDate;

    /**
     * 优惠券创建时间
     */
    private Long creationTime;

    /**
     * 优惠券最后更新时间
     */
    private Long lastCreationTime;

    /**
     * 优惠券状态
     */
    private String status;

    /**
     * 优惠券使用渠道
     */
    private String range;

    /**
     * 优惠券是否可叠加
     */
    private String isOverlay;

    /**
     * 是否是最新版本促销,N不是,Y是
     */
    private String isUsing;

    /**
     * 是否排他(霸王券Y为排他,N为不排他)
     */
    private String isExclusive;

    /**
     * 领券后有效时间,为空则生效时间与startDate,endDate一致
     */
    private Double activeTime;

    /**
     * 优惠券折扣类型
     */
    private String discountType;

    /**
     * 优惠券推送标识
     */
    private String isSyn;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

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

    public String getCouponDes() {
        return couponDes;
    }

    public void setCouponDes(String couponDes) {
        this.couponDes = couponDes;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public Double getPayFee() {
        return payFee;
    }

    public void setPayFee(Double payFee) {
        this.payFee = payFee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMaxRedemptionPerCustomer() {
        return maxRedemptionPerCustomer;
    }

    public void setMaxRedemptionPerCustomer(Integer maxRedemptionPerCustomer) {
        this.maxRedemptionPerCustomer = maxRedemptionPerCustomer;
    }

    public Integer getMaxRedemption() {
        return maxRedemption;
    }

    public void setMaxRedemption(Integer maxRedemption) {
        this.maxRedemption = maxRedemption;
    }

    public Long getGetStartDate() {
        return getStartDate;
    }

    public void setGetStartDate(Long getStartDate) {
        this.getStartDate = getStartDate;
    }

    public Long getGetEndDate() {
        return getEndDate;
    }

    public void setGetEndDate(Long getEndDate) {
        this.getEndDate = getEndDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastCreationTime() {
        return lastCreationTime;
    }

    public void setLastCreationTime(Long lastCreationTime) {
        this.lastCreationTime = lastCreationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getIsExclusive() {
        return isExclusive;
    }

    public void setIsExclusive(String isExclusive) {
        this.isExclusive = isExclusive;
    }

    public Double getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Double activeTime) {
        this.activeTime = activeTime;
    }

    public String getIsSyn() {
        return isSyn;
    }

    public void setIsSyn(String isSyn) {
        this.isSyn = isSyn;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    @Override
    public String toString() {
        return "{\"CouponsPojo\":{"
            + "                        \"Id\":\"" + Id + "\""
            + ",                         \"couponId\":\"" + couponId + "\""
            + ",                         \"couponCode\":\"" + couponCode + "\""
            + ",                         \"couponName\":\"" + couponName + "\""
            + ",                         \"couponDes\":\"" + couponDes + "\""
            + ",                         \"benefit\":\"" + benefit + "\""
            + ",                         \"payFee\":\"" + payFee + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"maxRedemptionPerCustomer\":\"" + maxRedemptionPerCustomer + "\""
            + ",                         \"maxRedemption\":\"" + maxRedemption + "\""
            + ",                         \"getStartDate\":\"" + getStartDate + "\""
            + ",                         \"getEndDate\":\"" + getEndDate + "\""
            + ",                         \"endDate\":\"" + endDate + "\""
            + ",                         \"startDate\":\"" + startDate + "\""
            + ",                         \"creationTime\":\"" + creationTime + "\""
            + ",                         \"lastCreationTime\":\"" + lastCreationTime + "\""
            + ",                         \"status\":\"" + status + "\""
            + ",                         \"range\":\"" + range + "\""
            + ",                         \"isOverlay\":\"" + isOverlay + "\""
            + ",                         \"isUsing\":\"" + isUsing + "\""
            + ",                         \"isExclusive\":\"" + isExclusive + "\""
            + ",                         \"activeTime\":\"" + activeTime + "\""
            + ",                         \"discountType\":\"" + discountType + "\""
            + ",                         \"isSyn\":\"" + isSyn + "\""
            + "}}";
    }
}
