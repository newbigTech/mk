package com.hand.hmall.mst.dto;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SubCategory
 * @description 子类映射
 * @date 2017年5月26日10:52:23
 */

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_SUBCATEGORY")
public class SubCategory extends BaseDTO {
    @Id
    @GeneratedValue
    private Long mappingId;

    private Long categoryId;

    private Long subCategoryId;


    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
