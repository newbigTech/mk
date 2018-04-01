package com.hand.promotion.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 事后促销记录
 */
@Table(name = "HMALL_OM_PROMO_RECORD")
@Entity
public class OmPromoteRecord {



    public OmPromoteRecord(){

    }

    public OmPromoteRecord(Long promoId,Long userId,Long orderId,String status,String logisticsNo,String couponId){
        this.promoId = promoId;
        this.userId = userId;
        this.status = status;
        this.orderId = orderId;
        this.logisticsNo = logisticsNo;
        this.couponId = couponId;
    }

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_PROMO_RECORD_S.nextval from dual")
     @Column
      private Long recordId;

    /**
     * 事后促销ID
     */
    @Column
      private Long promoId;

    /**
     * 用户ID
     */
     @Column
      private Long userId;

    /**
     * 订单ID
     */
     @Column
      private Long orderId;

    /**
     * 状态
     */
    @Column
      private String status;

    /**
     * 物流号
     */
     @Column
      private String logisticsNo;

    /**
     * 优惠券ID
     */
    @Column
      private String couponId;


     public void setRecordId(Long recordId){
         this.recordId = recordId;
     }

     public Long getRecordId(){
         return recordId;
     }

     public void setPromoId(Long promoId){
         this.promoId = promoId;
     }

     public Long getPromoId(){
         return promoId;
     }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setLogisticsNo(String logisticsNo){
         this.logisticsNo = logisticsNo;
     }

     public String getLogisticsNo(){
         return logisticsNo;
     }

     public void setCouponId(String couponId){
         this.couponId = couponId;
     }

     public String getCouponId(){
         return couponId;
     }

     }
