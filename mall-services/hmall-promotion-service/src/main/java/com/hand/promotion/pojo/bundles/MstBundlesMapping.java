package com.hand.promotion.pojo.bundles;

import java.io.Serializable;

/**
 * Auto Generated By Hap Code Generator
 **/


public class MstBundlesMapping implements Serializable {

    private Long mappingId;

    private Long productId;

    private Long bundlesId;

    private Long quantity;

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getMappingId() {
        return mappingId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setBundlesId(Long bundlesId) {
        this.bundlesId = bundlesId;
    }

    public Long getBundlesId() {
        return bundlesId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
