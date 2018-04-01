package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;
/**
 * @author 梅新养
 * @name:HmallFndRegionsB
 * @Description:HMALL_FND_REGIONS_B 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "HMALL_FND_REGIONS_B")
public class HmallFndRegionsB {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_FND_REGIONS_B_S.nextval from dual")
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
     * 版本号
     */
    private Double objectVersionNumber;

    /**
     * null
     */
    private Date creationDate;

    /**
     * null
     */
    private Long createdBy;

    /**
     * null
     */
    private Long lastUpdatedBy;

    /**
     * null
     */
    private Date lastUpdateDate;

    /**
     * null
     */
    private Long lastUpdateLogin;

    /**
     * null
     */
    private Long programApplicationId;

    /**
     * null
     */
    private Long programId;

    /**
     * null
     */
    private Date programUpdateDate;

    /**
     * null
     */
    private Long requestId;

    /**
     * null
     */
    private String attributeCategory;

    /**
     * null
     */
    private Object syncflag;

    /**
     * 主键ID
     * @return REGION_ID 主键ID
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * 主键ID
     * @param regionId 主键ID
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * 地区类型（国家/省/市/县/镇）
     * @return REGION_TYPE 地区类型（国家/省/市/县/镇）
     */
    public String getRegionType() {
        return regionType;
    }

    /**
     * 地区类型（国家/省/市/县/镇）
     * @param regionType 地区类型（国家/省/市/县/镇）
     */
    public void setRegionType(String regionType) {
        this.regionType = regionType == null ? null : regionType.trim();
    }

    /**
     * 地区代码
     * @return REGION_CODE 地区代码
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 地区代码
     * @param regionCode 地区代码
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    /**
     * 上级地区ID，用于联动
     * @return PARENT_ID 上级地区ID，用于联动
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 上级地区ID，用于联动
     * @param parentId 上级地区ID，用于联动
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 是否启用
     * @return ENABLED_FLAG 是否启用
     */
    public String getEnabledFlag() {
        return enabledFlag;
    }

    /**
     * 是否启用
     * @param enabledFlag 是否启用
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag == null ? null : enabledFlag.trim();
    }

    /**
     * 版本号
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }

    /**
     * null
     * @return SYNCFLAG null
     */
    public Object getSyncflag() {
        return syncflag;
    }

    /**
     * null
     * @param syncflag null
     */
    public void setSyncflag(Object syncflag) {
        this.syncflag = syncflag;
    }
}