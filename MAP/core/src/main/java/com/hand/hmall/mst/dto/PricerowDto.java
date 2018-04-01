package com.hand.hmall.mst.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送至商城和M3D的价格行对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_PRICEROW")
public class PricerowDto {

    /**
     * 推送至商城的价格行dto属性============== begin
     */
    private Long pricerowId;

    private Long productId;

    private String code;

    private BigDecimal basePrice;

    private BigDecimal bottomPrice;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date priceStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date priceEndTime;

    private String priceType;

    /**
     * 推送至商城的价格行dto属性============== end
     */

    /**
     * 推送至M3D的价格行dto属性============== start
     */
    private String number;

    private BigDecimal base_price;

    private BigDecimal activefy_price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date activefy_start_time;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date activefy_end_time;

    /**
     * 推送至M3D的价格行dto属性============== end
     */

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBase_price() {
        return base_price;
    }

    public void setBase_price(BigDecimal base_price) {
        this.base_price = base_price;
    }

    public BigDecimal getActivefy_price() {
        return activefy_price;
    }

    public void setActivefy_price(BigDecimal activefy_price) {
        this.activefy_price = activefy_price;
    }

    public Date getActivefy_start_time() {
        return activefy_start_time;
    }

    public void setActivefy_start_time(Date activefy_start_time) {
        this.activefy_start_time = activefy_start_time;
    }

    public Date getActivefy_end_time() {
        return activefy_end_time;
    }

    public void setActivefy_end_time(Date activefy_end_time) {
        this.activefy_end_time = activefy_end_time;
    }

    public Long getPricerowId() {
        return pricerowId;
    }

    public void setPricerowId(Long pricerowId) {
        this.pricerowId = pricerowId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(BigDecimal bottomPrice) {
        this.bottomPrice = bottomPrice;
    }

    public Date getPriceStartTime() {
        return priceStartTime;
    }

    public void setPriceStartTime(Date priceStartTime) {
        this.priceStartTime = priceStartTime;
    }

    public Date getPriceEndTime() {
        return priceEndTime;
    }

    public void setPriceEndTime(Date priceEndTime) {
        this.priceEndTime = priceEndTime;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
}
