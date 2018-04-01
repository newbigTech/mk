package com.hand.hmall.mst.dto;

/**
 * @descrption 商品套装类
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_BUNDLES")
public class MstBundles extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long bundlesId;

    @Column
    @NotNull
    @Condition(
            operator="LIKE"
    )
    private String code;

    @Column
    @NotNull
    private String status;

    @Column
    @NotNull
    @Condition(
            operator="LIKE"
    )
    private String name;

    @Column
    private String description;

    @Column
    @NotNull
    private BigDecimal price;

    @Column
    private String syncFlag;

    @Column
    private String promotionCode;

    @Column
    private Integer priority;
    @Column
    private String isOverlay;
    /*拓展字段*/
    /**
     * 原价
     */
    @Transient
    private double originalPrice;
    /**
     * 固定价格
     */
    @Transient
    private double fixedPrice;
    /**
     * 折扣率
     */
    @Transient
    private double discountRate;

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(double fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public String getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Long getBundlesId() {
        return bundlesId;
    }

    public void setBundlesId(Long bundlesId) {
        this.bundlesId = bundlesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }
}
