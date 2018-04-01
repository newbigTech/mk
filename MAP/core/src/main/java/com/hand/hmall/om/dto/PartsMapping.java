package com.hand.hmall.om.dto;


import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name PartsMapping
 * @description 配件dto
 * @date 2017年5月26日10:52:23
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_PARTS_MAPPING")
public class PartsMapping extends BaseDTO {
    @Id
    @GeneratedValue
    private Long mappingId;

    private Long orderEntryId;

    private Long partsId;

    private String syncflag;

    /**
     * 配件编码
     */
    @Transient
    private String code;

    /**
     * 配件名称
     */
    @Transient
    private String name;


    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public Long getPartsId() {
        return partsId;
    }

    public void setPartsId(Long partsId) {
        this.partsId = partsId;
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
}
