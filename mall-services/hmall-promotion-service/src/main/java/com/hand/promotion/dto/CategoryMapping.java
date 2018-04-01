package com.hand.promotion.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HMALL_MST_CATEGORY_MAPPING")
public class CategoryMapping {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_CATEGORY_MAPPING_S.nextval from dual")
    private Long mappingId;

    /**
     * 产品
     */
    private Long productId;

    /**
     * 类别
     */
    private Long categoryId;

    /**
     * 接口传输标示
     */
    private String syncflag;

    /**
     * 版本号
     */
    private int objectVersionNumber;

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public int getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(int objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @Override
    public String toString() {
        return "{\"CategoryMapping\":{"
                + "                        \"mappingId\":\"" + mappingId + "\""
                + ",                         \"productId\":\"" + productId + "\""
                + ",                         \"categoryId\":\"" + categoryId + "\""
                + ",                         \"syncflag\":\"" + syncflag + "\""
                + ",                         \"objectVersionNumber\":\"" + objectVersionNumber + "\""
                + "}}";
    }
}