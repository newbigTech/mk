package com.hand.promotion.pojo.order;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 * @desp 订单可选优惠券 pojo
 */
public class OptimumCouponPojo implements java.io.Serializable {
    private Long couponId;
    private String benefit;
    @JsonIgnore
    private Double discount;
    private String name;
    private Long startDate;
    private Long endDate;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
