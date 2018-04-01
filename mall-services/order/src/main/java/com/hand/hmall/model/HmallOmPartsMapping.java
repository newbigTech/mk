package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;
/**
 * @author 梅新养
 * @name:HmallOmPartsMapping
 * @Description:HMALL_OM_PARTS_MAPPING 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "HMALL_OM_PARTS_MAPPING")
public class HmallOmPartsMapping {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_PARTS_MAPPING_S.nextval from dual")
    private Long mappingId;

    /**
     * 订单行
     */
    private Long orderEntryId;

    /**
     * 配件产品
     */
    private Long partsId;

    /**
     * 接口传输标示
     */
    private String syncflag;

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
     * 主键
     *
     * @return MAPPING_ID 主键
     */
    public Long getMappingId() {
        return mappingId;
    }

    /**
     * 主键
     *
     * @param mappingId 主键
     */
    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    /**
     * 订单行
     *
     * @return ORDER_ENTRY_ID 订单行
     */
    public Long getOrderEntryId() {
        return orderEntryId;
    }

    /**
     * 订单行
     *
     * @param orderEntryId 订单行
     */
    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    /**
     * 配件产品
     *
     * @return PARTS_ID 配件产品
     */
    public Long getPartsId() {
        return partsId;
    }

    /**
     * 配件产品
     *
     * @param partsId 配件产品
     */
    public void setPartsId(Long partsId) {
        this.partsId = partsId;
    }

    /**
     * 接口传输标示
     *
     * @return SYNCFLAG 接口传输标示
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口传输标示
     *
     * @param syncflag 接口传输标示
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }

    /**
     * 版本号
     *
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     *
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     *
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     *
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     *
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     *
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     *
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     *
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     *
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     *
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     *
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     *
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     *
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     *
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     *
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     *
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     *
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     *
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     *
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }

}