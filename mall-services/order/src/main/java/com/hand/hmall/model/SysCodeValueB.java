package com.hand.hmall.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author 梅新养
 * @name:SysCodeValueB
 * @Description:SYS_CODE_VALUE_B 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "SYS_CODE_VALUE_B")
public class SysCodeValueB {
    /**
     * 表ID，主键，供其他表做外键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SYS_CODE_VALUE_B_S.nextval from dual")
    private Long codeValueId;

    /**
     * null
     */
    private Long codeId;

    /**
     * 快码值
     */
    private String value;

    /**
     * 快码意思
     */
    private String meaning;

    /**
     * 快码描述
     */
    private String description;

    /**
     * null
     */
    private Double objectVersionNumber;

    /**
     * null
     */
    private Long requestId;

    /**
     * null
     */
    private Long programId;

    /**
     * null
     */
    private Long createdBy;

    /**
     * null
     */
    private Date creationDate;

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
    private String attributeCategory;


    /**
     * null
     */
    private Long orderSeq;

    /**
     * 标记
     */
    private String tag;

    /**
     * 是否启用
     */
    private String enabledFlag;

    /**
     * 父级快码值
     */
    private Long parentCodeValueId;

    /**
     * 表ID，主键，供其他表做外键
     * @return CODE_VALUE_ID 表ID，主键，供其他表做外键
     */
    public Long getCodeValueId() {
        return codeValueId;
    }

    /**
     * 表ID，主键，供其他表做外键
     * @param codeValueId 表ID，主键，供其他表做外键
     */
    public void setCodeValueId(Long codeValueId) {
        this.codeValueId = codeValueId;
    }

    /**
     * null
     * @return CODE_ID null
     */
    public Long getCodeId() {
        return codeId;
    }

    /**
     * null
     * @param codeId null
     */
    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    /**
     * 快码值
     * @return VALUE 快码值
     */
    public String getValue() {
        return value;
    }

    /**
     * 快码值
     * @param value 快码值
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    /**
     * 快码意思
     * @return MEANING 快码意思
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * 快码意思
     * @param meaning 快码意思
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning == null ? null : meaning.trim();
    }

    /**
     * 快码描述
     * @return DESCRIPTION 快码描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 快码描述
     * @param description 快码描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * null
     * @return OBJECT_VERSION_NUMBER null
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * null
     * @param objectVersionNumber null
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
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
     * @return ORDER_SEQ null
     */
    public Long getOrderSeq() {
        return orderSeq;
    }

    /**
     * null
     * @param orderSeq null
     */
    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    /**
     * 标记
     * @return TAG 标记
     */
    public String getTag() {
        return tag;
    }

    /**
     * 标记
     * @param tag 标记
     */
    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
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
     * 父级快码值
     * @return PARENT_CODE_VALUE_ID 父级快码值
     */
    public Long getParentCodeValueId() {
        return parentCodeValueId;
    }

    /**
     * 父级快码值
     * @param parentCodeValueId 父级快码值
     */
    public void setParentCodeValueId(Long parentCodeValueId) {
        this.parentCodeValueId = parentCodeValueId;
    }
}