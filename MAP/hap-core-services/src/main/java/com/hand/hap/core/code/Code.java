package com.hand.hap.core.code;

import java.io.Serializable;

/**
 * @author zhangwantao
 * @version 0.1
 * @name Code
 * @description 快码
 * @date 2017/12/13
 */
public class Code implements Serializable {

    /**
     * 快码类型.
     */
    private String code;

    /**
     * 表ID，主键，供其他表做外键.
     */
    private Long codeId;

    /**
     * 快码描述.
     */
    private String description;

    private String type;

    private String enabledFlag;

    private Long parentCodeId;

    private String parentCodeDescription;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getParentCodeId() {
        return parentCodeId;
    }

    public void setParentCodeId(Long parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public String getParentCodeDescription() {
        return parentCodeDescription;
    }

    public void setParentCodeDescription(String parentCodeDescription) {
        this.parentCodeDescription = parentCodeDescription;
    }
}
