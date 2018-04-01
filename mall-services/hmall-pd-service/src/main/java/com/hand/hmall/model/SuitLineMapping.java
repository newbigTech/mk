package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name SuitLineMapping
 * @description 套件
 * @date 2017/6/6 14:25
 */
@Table(name = "HMALL_MST_SUITLINE_MAPPING")
public class SuitLineMapping {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_SUITLINE_MAPPING_S.nextval from dual")
    private Long mappingId;

    /**
     * 组件产品主键
     */
    private Long componentId;

    /**
     * 组件产品数量
     */
    private Long quantity;

    /**
     * 套件产品
     */
    private Long productHeadId;

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getProductHeadId() {
        return productHeadId;
    }

    public void setProductHeadId(Long productHeadId) {
        this.productHeadId = productHeadId;
    }
}


