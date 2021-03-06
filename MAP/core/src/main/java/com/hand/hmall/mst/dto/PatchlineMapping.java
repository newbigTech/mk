package com.hand.hmall.mst.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 补件映射对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_PATCHLINE_MAPPING")
public class PatchlineMapping extends BaseDTO {
    /**
     *
     */
    private static final long serialVersionUID = 206927354301067986L;

    @Id
    @GeneratedValue
    private Long mappingId;

    private Long productId;

    private Long patchLineId;

    private String syncflag;


    @Transient
    private Long syncProductId;

    //补件编码
    @Transient
    private String code;
    //补件名称
    @Transient
    private String name;


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

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSyncProductId() {
        return syncProductId;
    }

    public void setSyncProductId(Long syncProductId) {
        this.syncProductId = syncProductId;
    }
}
