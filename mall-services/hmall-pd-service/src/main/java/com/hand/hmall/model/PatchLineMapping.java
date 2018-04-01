package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name PatchLineMapping
 * @description 补件
 * @date 2017/6/6 14:25
 */
@Entity
@Table(name = "HMALL_MST_PATCHLINE_MAPPING")
public class PatchLineMapping {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_PATCHLINE_MAPPING_S.nextval from dual")
    private Long mappingId;

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 补件产品主键
     */
    private Long patchLineId;

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

    public Long getPatchLineId() {
        return patchLineId;
    }

    public void setPatchLineId(Long patchLineId) {
        this.patchLineId = patchLineId;
    }
}