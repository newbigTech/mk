package com.hand.hmall.mst.dto;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SyncData
 * @description 同步配置
 * @date 2017年5月26日10:52:23
 */
public class SyncData {

    private List<Product> list;

    private List<ProductCategory> categoryList;

    private String catalogTo;

    private String versionTo;

    private String catalogFrom;

    private String versionFrom;

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public List<ProductCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ProductCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCatalogTo() {
        return catalogTo;
    }

    public void setCatalogTo(String catalogTo) {
        this.catalogTo = catalogTo;
    }

    public String getVersionTo() {
        return versionTo;
    }

    public void setVersionTo(String versionTo) {
        this.versionTo = versionTo;
    }

    public String getCatalogFrom() {
        return catalogFrom;
    }

    public void setCatalogFrom(String catalogFrom) {
        this.catalogFrom = catalogFrom;
    }

    public String getVersionFrom() {
        return versionFrom;
    }

    public void setVersionFrom(String versionFrom) {
        this.versionFrom = versionFrom;
    }
}
