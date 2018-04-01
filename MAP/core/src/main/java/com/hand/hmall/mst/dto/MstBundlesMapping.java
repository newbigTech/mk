package com.hand.hmall.mst.dto;


import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * @descrption 商品套装产品映射类
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_BUNDLES_MAPPING")
public class MstBundlesMapping extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long mappingId;

    @Column
    private Long productId;

    @Column
    private Long bundlesId;

    @Column
    private Long quantity;
    /*拓展字段*/
    /**
     * 商品编码
     */
    @Transient
    private String productCode;
    /**
     * 商品名
     */
    @Transient
    private String productName;
    /**
     * 商品价格
     */
    @Transient
    private Double productPrice;

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public Long getBundlesId() {
        return bundlesId;
    }

    public void setBundlesId(Long bundlesId) {
        this.bundlesId = bundlesId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }


}
