package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;

/**
 * 事后促销信息
 */
@Table(name = "HMALL_OM_ED_PROMO")
@Entity
public class OmEdPromote {
    /**
     * 主键
     */
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_ED_PROMO_S.nextval from dual")
     @Column
      private Long promoId;

    /**
     * 促销编码
     */
    @Column
      private String code;

    /**
     * 描述
     */
     @Column
      private String description;

    /**
     * 获得资格提示信息
     */
    @Column
      private String info;

    /**
     * 名称
     */
     @Column
      private String name;

    /**
     * 渠道
     */
    @Column
      private String channel;

    /**
     * 网站
     */
     @Column
      private String website;

    /**
     * 店铺
     */
    @Column
      private String store;

    /**
     * 订单金额
     */
     @Column
      private Double orderAmount;

    /**
     * 订单开始时间
     */
    @Column
      private Date orderStartTime;

    /**
     * 订单结束时间
     */
     @Column
      private Date orderEndTime;

    /**
     * 订单支付开始时间
     */
    @Column
      private Date payStartTime;

    /**
     * 订单支付结束时间
     */
     @Column
      private Date payEndTime;

    /**
     * 名额
     */
    @Column
      private Long space;

    /**
     * 优先级
     */
     @Column
      private Long priority;

    /**
     * 关联优惠券
     */
    @Column
      private String coupon;

    /**
     * 赠品信息
     */
     @Column
      private String gift;

    /**
     * 状态
     */
    @Column
      private String status;

    /**
     * 单人最高名额
     */
     @Column
      private Long max;


    @Transient
     private String orderCreateTime;

    /**
     * 返回事后促销Id 使用
     */
    @Transient
     private Long recordId;

    /**
     * 返用户回事后促销资格状态
     */
    @Transient
    private String userStatus;

     public void setPromoId(Long promoId){
         this.promoId = promoId;
     }

     public Long getPromoId(){
         return promoId;
     }

     public void setCode(String code){
         this.code = code;
     }

     public String getCode(){
         return code;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setInfo(String info){
         this.info = info;
     }

     public String getInfo(){
         return info;
     }

     public void setName(String name){
         this.name = name;
     }

     public String getName(){
         return name;
     }

     public void setChannel(String channel){
         this.channel = channel;
     }

     public String getChannel(){
         return channel;
     }

     public void setWebsite(String website){
         this.website = website;
     }

     public String getWebsite(){
         return website;
     }

     public void setStore(String store){
         this.store = store;
     }

     public String getStore(){
         return store;
     }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setOrderStartTime(Date orderStartTime){
         this.orderStartTime = orderStartTime;
     }

     public Date getOrderStartTime(){
         return orderStartTime;
     }

     public void setOrderEndTime(Date orderEndTime){
         this.orderEndTime = orderEndTime;
     }

     public Date getOrderEndTime(){
         return orderEndTime;
     }

     public void setPayStartTime(Date payStartTime){
         this.payStartTime = payStartTime;
     }

     public Date getPayStartTime(){
         return payStartTime;
     }

     public void setPayEndTime(Date payEndTime){
         this.payEndTime = payEndTime;
     }

     public Date getPayEndTime(){
         return payEndTime;
     }

     public void setSpace(Long space){
         this.space = space;
     }

     public Long getSpace(){
         return space;
     }

     public void setPriority(Long priority){
         this.priority = priority;
     }

     public Long getPriority(){
         return priority;
     }

     public void setCoupon(String coupon){
         this.coupon = coupon;
     }

     public String getCoupon(){
         return coupon;
     }

     public void setGift(String gift){
         this.gift = gift;
     }

     public String getGift(){
         return gift;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setMax(Long max){
         this.max = max;
     }

     public Long getMax(){
         return max;
     }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
