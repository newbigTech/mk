package com.hand.hmall.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 金额更改记录行表
 * Created by qinzhipeng on 2017/12/18.
 */
@Entity
@Table(name = "HMALL_OM_AMOUNT_LOGENTRY")
public class AmountChangeLogEntry {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_AMOUNT_LOGENTRY_S.nextval from dual")
    private Long logEntryId;
    /**
     * 订单行号
     */
    private Long orderEntryId;
    /**
     * 订单头号
     */
    private Long orderId;
    /**
     * 订单code
     */
    private String code;
    /**
     * 实际价格
     */
    private Double unitFee;
    /**
     * 订单行应付总金额
     */
    private Double totalFee;
    /**
     * 运费
     */
    private Double shippingFee;
    /**
     * 安装费
     */
    private Double installationFee;
    /**
     * 订单行促销优惠金额
     */
    private Double discountFee;
    /**
     * 订单行促销分摊优惠金额
     */
    private Double discountFeel;
    /**
     * 订单行优惠券优惠金额
     */
    private Double couponFee;
    /**
     * 优惠总金额
     */
    private Double totalDiscount;
    /**
     * 插入时间
     */
    private Long insertTime;

    public Long getLogEntryId() {
        return logEntryId;
    }

    public void setLogEntryId(Long logEntryId) {
        this.logEntryId = logEntryId;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        return discountFeel;
    }

    public void setDiscountFeel(Double discountFeel) {
        this.discountFeel = discountFeel;
    }

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }
}
