package com.hand.hmall.mst.dto;

/**
 * @author peng.chen
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/8 0008 上午 8:47
 */

public class CategoryMappingDto {

    private Long mappingId;

    private Long productId;

    private Long categoryId;

    private String code;

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
