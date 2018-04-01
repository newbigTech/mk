package com.hand.promotion.dto;

/**
 * create by Xinyang.Mei
 **/

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "HMALL_MST_BUNDLES")
public class MstBundles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MSTBUNDLES_S.nextval from dual")
    @Column
    private Long bundlesId;

    @Column
    private String code;

    @Column
    private String status;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private String syncFlag;

    @Column
    private String promotionCode;

    @Column
    private Integer priority;

    @Column
    private String isOverlay;

    @Transient
    private List<MstBundlesMapping> bundlesMappings;


    public void setBundlesId(Long bundlesId) {
        this.bundlesId = bundlesId;
    }

    public Long getBundlesId() {
        return bundlesId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public List<MstBundlesMapping> getBundlesMappings() {
        return bundlesMappings;
    }

    public void setBundlesMappings(List<MstBundlesMapping> bundlesMappings) {
        this.bundlesMappings = bundlesMappings;
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
