package com.hand.hmall.om.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import javax.persistence.*;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_PROMO_RECORD")
public class OmPromoRecord extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long recordId;

    @Column
    private Long promoId;

    @Column
    private Long userId;

    @Column
    private Long orderId;

    @Column
    private String status;

    @Column
    private String logisticsNo;

    @Column
    private String couponId;

    @Transient
    private String flag;   //判断是符合条件用户“1”还是候补用户“2”

    @Transient
    private String customerid; //关联的用户表中的Customerid

    @Transient
    private String code; //关联的订单编号

    @Transient
    private BigDecimal orderAmount; //关联的订单的orderAmount

    @Transient
    private String orderStatus;//关联的订单状态

    @Transient
    private BigDecimal willRefundSum; //待退款金额

    @Transient
    private BigDecimal haveRefundSum; //退款金额

    @Transient
    private String channelCode;//渠道

    @Transient
    private Date orderCreationtime;//订单创建时间

    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getWillRefundSum() {
        return willRefundSum;
    }

    public void setWillRefundSum(BigDecimal willRefundSum) {
        this.willRefundSum = willRefundSum;
    }

    public BigDecimal getHaveRefundSum() {
        return haveRefundSum;
    }

    public void setHaveRefundSum(BigDecimal haveRefundSum) {
        this.haveRefundSum = haveRefundSum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponId() {
        return couponId;
    }

}
