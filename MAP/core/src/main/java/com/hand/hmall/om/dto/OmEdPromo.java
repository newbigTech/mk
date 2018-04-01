package com.hand.hmall.om.dto;

/**
 * @author xiaoli.yu@hand-china.com
 * @version 0.1
 * @name OmEdPromo
 * @description
 * @date 2017年10月13日10:02:23
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ED_PROMO")
public class OmEdPromo extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long promoId;

    @Column
    private String code;

    @Column
    private String description;

    @Column
    private String info;

    @Column
    private String name;

    @Column
    private String channel;

    @Column
    private String website;

    @Column
    private String store;

    @Column
    private BigDecimal orderAmount;

    @Column
    private Date orderStartTime;

    @Column
    private Date orderEndTime;

    @Column
    private Date payStartTime;

    @Column
    private Date payEndTime;

    @Column
    private Double space;

    @Column
    private Double priority;

    @Column
    private String coupon;

    @Column
    private String gift;

    @Column
    private String status;

    @Column
    private Double max;

    @Transient
    private String websiteName;

    @Transient
    private String storeName;

    @Transient
    private String channelDesc;

    @Transient
    private String statusDesc;

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderStartTime() {
        return orderStartTime;
    }

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")public void setOrderStartTime(Date orderStartTime){
         this.orderStartTime = orderStartTime;
     }

    public Date getOrderEndTime() {
        return orderEndTime;
    }

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")public void setOrderEndTime(Date orderEndTime){
         this.orderEndTime = orderEndTime;
     }

    public Date getPayStartTime() {
        return payStartTime;
    }

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")public void setPayStartTime(Date payStartTime){
         this.payStartTime = payStartTime;
     }

    public Date getPayEndTime() {
        return payEndTime;
    }

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")public void setPayEndTime(Date payEndTime){
         this.payEndTime = payEndTime;
     }

    public Double getSpace() {
        return space;
    }

    public void setSpace(Double space) {
        this.space = space;
    }

    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMax() {
        return max;
    }

     public void setMax(Double max) {
        this.max = max;
    }}
