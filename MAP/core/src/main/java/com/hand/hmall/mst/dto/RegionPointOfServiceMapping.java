package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Table;

/**
 * author: zhangzilong
 * name: RegionPointOfServiceMapping.java
 * discription:
 * date: 2017/12/25
 * version: 0.1
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_REGION_POS_MAPPING")
public class RegionPointOfServiceMapping extends BaseDTO {

    private Long mappingId;

    private String regionCode;

    private String pointOfService;

    private String enableFlag;

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
