package com.hand.hmall.mst.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


/**
 * @author zhangyanan
 * @version 0.1
 * @name ProductCategory
 * @description
 * @date 2017/5/26
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_CATEGORY")
public class ProductCategory extends BaseDTO {
    /**
     * 商品类别ID
     */
    @Id
    @GeneratedValue
    private Long categoryId;

    /**
     * 类别编码
     */
    @ExcelVOAttribute(name = "类别编码", column = "A", isExport = true)
    private String categoryCode;

    /**
     * 类别名称
     */
    @ExcelVOAttribute(name = "类别名称", column = "B", isExport = true)
    private String categoryName;

    /**
     * 目录版本
     */
    @ExcelVOAttribute(name = "目录版本", column = "C", isExport = true)
    private Long catalogVersion;

    /**
     * 目录版本Id
     */
    @Transient
    private Long catalogversionId;

    /**
     * 目录版本
     */
    @Transient
    private String catalogversion;
    /**
     * 等级
     */
    @ExcelVOAttribute(name = "等级", column = "D", isExport = true)
    private String categoryLevel;

    /**
     * 创建日期
     */
    private Date creationDate;

    private Date lastUpdateDate;

    /**
     * 创建日期从
     */
    @Transient
    private String creationDateForm;

    /**
     * 创建日期至
     */
    @Transient
    private String creationDateTo;

    @Transient
    private Long productId;

    private String syncflag;

    @Transient
    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogversion() {
        return catalogversion;
    }

    public void setCatalogversion(String catalogversion) {
        this.catalogversion = catalogversion;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCatalogVersion() {
        return catalogVersion;
    }

    public void setCatalogVersion(Long catalogVersion) {
        this.catalogVersion = catalogVersion;
    }

    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getCreationDateForm() {
        return creationDateForm;
    }

    public void setCreationDateForm(String creationDateForm) {
        this.creationDateForm = creationDateForm;
    }

    public String getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(String creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getCatalogversionId() {
        return catalogversionId;
    }

    public void setCatalogversionId(Long catalogversionId) {
        this.catalogversionId = catalogversionId;
    }
}
