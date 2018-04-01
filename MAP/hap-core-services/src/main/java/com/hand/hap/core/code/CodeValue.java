package com.hand.hap.core.code;

import java.io.Serializable;

/**
 * @author alaowan
 * Created at 2017/12/13 18:35
 * @description
 */
public class CodeValue implements Serializable {

    private Long codeId;

    private Long codeValueId;

    private String description;

    private String meaning;

    private String value;

    private Long orderSeq;

    private String tag;

    private String enabledFlag;

    private Long parentCodeValueId;

    private String parentCodeValueMeaning;

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public Long getCodeValueId() {
        return codeValueId;
    }

    public void setCodeValueId(Long codeValueId) {
        this.codeValueId = codeValueId;
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

    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getParentCodeValueId() {
        return parentCodeValueId;
    }

    public void setParentCodeValueId(Long parentCodeValueId) {
        this.parentCodeValueId = parentCodeValueId;
    }

    public String getParentCodeValueMeaning() {
        return parentCodeValueMeaning;
    }

    public void setParentCodeValueMeaning(String parentCodeValueMeaning) {
        this.parentCodeValueMeaning = parentCodeValueMeaning;
    }
}
