package com.hand.hmall.mst.dto;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CategoryMapping
 * @description 产品类别映射
 * @date 2017年5月26日10:52:23
 */

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_CATEGORY_MAPPING")
public class CategoryMapping extends BaseDTO {
    @Id
    @GeneratedValue
    private Long mappingId;

    private Long productId;

    private Long categoryId;

    private String syncflag;

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

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }
}
