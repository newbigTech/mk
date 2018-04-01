package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Category
 * @description 推送商城的产品类别对象
 * @date 2017年5月26日10:52:23
 */
@ExtensionAttribute(disable = true)
public class Category{

    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private Long catalogVersion;

    private String categoryLevel;

    private String syncflag;

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

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }
}
