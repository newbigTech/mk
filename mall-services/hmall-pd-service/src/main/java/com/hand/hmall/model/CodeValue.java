package com.hand.hmall.model;

import javax.persistence.*;

/**
 * @author 马君
 * @version 0.1
 * @name CodeValue
 * @description 块码
 * @date 2017/6/6 14:25
 */
@Entity
@Table(name = "SYS_CODE_VALUE_B")
public class CodeValue {
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

    public Long getCodeValueId() {
        return codeValueId;
    }

    public void setCodeValueId(Long codeValueId) {
        this.codeValueId = codeValueId;
    }

    public Long getParentCodeValueId() {
        return parentCodeValueId;
    }

    public void setParentCodeValueId(Long parentCodeValueId) {
        this.parentCodeValueId = parentCodeValueId;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }
}
