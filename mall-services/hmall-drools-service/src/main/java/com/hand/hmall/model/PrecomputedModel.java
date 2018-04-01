package com.hand.hmall.model;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/9.
 */
public class PrecomputedModel {

    private String tempId;
    private String distributionId;
    private String distribution;
    private String userId;
    private Map optimumCoupon;
    private List<Map> coupons;
    private Price price;
    private Price activityPrice;
    private List<Product> products;
    private Address address;
    private List<Map> gifts;
    private List<Map> activities;

    public Map getOptimumCoupon() {
        return optimumCoupon;
    }

    public void setOptimumCoupon(Map optimumCoupon) {
        this.optimumCoupon = optimumCoupon;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public List<Coupon> getCoupons() {
//        return coupons;
//    }
//
//    public void setCoupons(List<Coupon> coupons) {
//        this.coupons = coupons;
//    }


    public List<Map> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Map> coupons) {
        this.coupons = coupons;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Map> getGifts() {
        return gifts;
    }

    public void setGifts(List<Map> gifts) {
        this.gifts = gifts;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Price getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(Price activityPrice) {
        this.activityPrice = activityPrice;
    }

    public List<Map> getActivities() {
        return activities;
    }

    public void setActivities(List<Map> activities) {
        this.activities = activities;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
}
