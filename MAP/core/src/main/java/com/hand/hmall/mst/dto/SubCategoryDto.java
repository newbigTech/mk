package com.hand.hmall.mst.dto;

/**
 * Created by zhangmeng01 on 2017/6/1.
 */
public class SubCategoryDto {
    private String mappingId;

    private String categoryId;

    private String subCategoryId;

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
