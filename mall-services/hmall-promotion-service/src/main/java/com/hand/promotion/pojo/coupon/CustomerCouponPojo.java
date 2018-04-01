package com.hand.promotion.pojo.coupon;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 用户已领取优惠券pojo
 */
@Document(collection = "CustomerCoupon")
public class CustomerCouponPojo {

    /**
     * 用户已发放优惠券id
     */
    private String id;

    /**
     * 用户账号
     */
    private String userId;

    /**
     * 发放的优惠券编码
     */
    private String couponCode;

    /**
     * 已发放优惠券状态
     */
    private String status;

    /**
     * 优惠券面值
     */
    private String benefit;

    /**
     * 是否叠加
     */
    private String isOverlay;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 发放类型
     */
    private String type;

    /**
     * 使用渠道
     */
    private String range;

    /**
     * 优惠券主键
     */
    private String cid;

    /**
     * 发放时间
     */
    private Long sendDate;

    /**
     * 优惠券生效时间
     */
    private Long startDate;

    /**
     * 优惠券失效时间
     */
    private Long endDate;

    /**
     * 券有效时长
     */
    private Long activeTime;

    /**
     * 关联的购券订单编号
     */
    private String orderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;

    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public String toString() {
        return "{\"CustomerCouponPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"userId\":\"" + userId + "\""
            + ",                         \"couponCode\":\"" + couponCode + "\""
            + ",                         \"status\":\"" + status + "\""
            + ",                         \"benefit\":\"" + benefit + "\""
            + ",                         \"isOverlay\":\"" + isOverlay + "\""
            + ",                         \"couponName\":\"" + couponName + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"range\":\"" + range + "\""
            + ",                         \"cid\":\"" + cid + "\""
            + ",                         \"sendDate\":\"" + sendDate + "\""
            + ",                         \"startDate\":\"" + startDate + "\""
            + ",                         \"endDate\":\"" + endDate + "\""
            + ",                         \"activeTime\":\"" + activeTime + "\""
            + ",                         \"orderId\":\"" + orderId + "\""
            + "}}";
    }
}

