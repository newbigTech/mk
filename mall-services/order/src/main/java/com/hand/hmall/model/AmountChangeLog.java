package com.hand.hmall.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 金额更改记录头表
 * Created by qinzhipeng on 2017/12/18.
 */
@Entity
@Table(name = "HMALL_OM_AMOUNT_LOG")
public class AmountChangeLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_AMOUNT_LOG_S.nextval from dual")
    private Long logId;
    /**
     * 订单号
     */
    private Long orderId;
    /**
     * 平台订单编号
     */
    private String escOrderCode;
    /**
     * 订单编号
     */
    private String code;
    /**
     * 当前物流运费
     */
    private Double postFee;
    /**
     * 当前快递运费
     */
    private Double epostFee;
    /**
     * 安装费
     */
    private Double fixFee;
    /**
     * 优惠券优惠总金额
     */
    private Double couponFee;
    /**
     * 促销优惠总金额
     */
    private Double discountFee;
    /**
     * 总优惠金额
     */
    private Double totalDiscount;
    /**
     * 订单净额
     */
    private Double netAmount;
    /**
     * 插入时间
     */
    private Long insertTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }
}
