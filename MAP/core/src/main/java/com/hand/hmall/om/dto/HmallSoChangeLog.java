package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 书面记录表
 * Created by qinzhipeng on 2017/12/18.
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_SO_CHANGE_LOG")
public class HmallSoChangeLog extends BaseDTO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long zsmId;
    /**
     * 订单号
     */
    private Long orderId;
    /**
     * 订单行号
     */
    private Long orderEntryId;
    /**
     * pin
     */
    private String pin;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单行类型
     */
    private String orderEntryType;

    /**
     * 订单创建日期
     */
    private Date orderCreateDate;
    /**
     * 商品Id
     */
    private Long productId;
    /**
     * 商品类型
     */
    private String productType;
    /**
     * 最终行数量
     */
    private Integer quantity;
    /**
     * 最终行金额
     */
    private BigDecimal totalFee;
    /**
     * 变更行数量
     */
    private Integer changeQuantity;
    /**
     * 变更金额
     */
    private BigDecimal changeFee;
    /**
     * 原销售订单号
     */
    private Long parentOrderId;
    /**
     * 套件商品ID
     */
    private Long parentProductId;
    /**
     * 套件订单行ID
     */
    private Long parentOrderEntryId;

    public Long getZsmId() {
        return zsmId;
    }

    public void setZsmId(Long zsmId) {
        this.zsmId = zsmId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderEntryType() {
        return orderEntryType;
    }

    public void setOrderEntryType(String orderEntryType) {
        this.orderEntryType = orderEntryType;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Integer changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public BigDecimal getChangeFee() {
        return changeFee;
    }

    public void setChangeFee(BigDecimal changeFee) {
        this.changeFee = changeFee;
    }

    public Long getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(Long parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public Long getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(Long parentProductId) {
        this.parentProductId = parentProductId;
    }

    public Long getParentOrderEntryId() {
        return parentOrderEntryId;
    }

    public void setParentOrderEntryId(Long parentOrderEntryId) {
        this.parentOrderEntryId = parentOrderEntryId;
    }
}
