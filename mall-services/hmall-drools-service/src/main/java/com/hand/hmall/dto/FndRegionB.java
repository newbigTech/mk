package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Hmall_FND_REGIONS_B")
public class FndRegionB {
    /**
     * 主键ID
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select Hmall_FND_REGIONS_B_S.nextval from dual")
    private Long regionId;

    /**
     * 地区类型（国家/省/市/县/镇）
     */
    private String regionType;

    /**
     * 地区代码
     */
    private String regionCode;

    /**
     * 上级地区ID，用于联动
     */
    private Long parentId;

    /**
     * 是否启用
     */
    private String enabledFlag;


    /**
     * 主键ID
     *
     * @return REGION_ID 主键ID
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * 主键ID
     *
     * @param regionId 主键ID
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * 地区类型（国家/省/市/县/镇）
     *
     * @return REGION_TYPE 地区类型（国家/省/市/县/镇）
     */
    public String getRegionType() {
        return regionType;
    }

    /**
     * 地区类型（国家/省/市/县/镇）
     *
     * @param regionType 地区类型（国家/省/市/县/镇）
     */
    public void setRegionType(String regionType) {
        this.regionType = regionType == null ? null : regionType.trim();
    }

    /**
     * 地区代码
     *
     * @return REGION_CODE 地区代码
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 地区代码
     *
     * @param regionCode 地区代码
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    /**
     * 上级地区ID，用于联动
     *
     * @return PARENT_ID 上级地区ID，用于联动
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 上级地区ID，用于联动
     *
     * @param parentId 上级地区ID，用于联动
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 是否启用
     *
     * @return ENABLED_FLAG 是否启用
     */
    public String getEnabledFlag() {
        return enabledFlag;
    }

    /**
     * 是否启用
     *
     * @param enabledFlag 是否启用
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag == null ? null : enabledFlag.trim();
    }

}