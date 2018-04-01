package com.hand.promotion.pojo.bundles;

/**
 * create by changbing.quan
 **/

import java.io.Serializable;
import java.util.List;

/**
 * 捆绑套餐界面传递数据
 * 对应的数据结构
 */
public class MstBundles implements Serializable {
    private Long bundlesId;


    private String code;


    private String status;


    private String name;


    private String description;


    private Double price;


    private String syncFlag;


    private String promotionCode;


    private Integer priority;

    private String isOverlay;
    /*捆绑套餐行数据*/
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
