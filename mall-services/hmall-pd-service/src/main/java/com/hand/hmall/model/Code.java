package com.hand.hmall.model;

import javax.persistence.*;

/**
 * @author 马君
 * @version 0.1
 * @name Code
 * @description 块码
 * @date 2017/6/6 14:17
 */
@Entity
@Table(name = "SYS_CODE_B")
public class Code {
    /**
     * 代码ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SYS_CODE_B_S.nextval from dual")
    private Long codeId;

    /**
     * 快码类型
     */
    private String code;

    /**
     * 快码类型描述
     */
    private String description;

    /**
     * 代码类型
     */
    private String type;

    /**
     * 是否启用
     */
    private String enabledFlag;

    /**
     * 父级快码
     */
    private Long parentCodeId;

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public Long getParentCodeId() {
        return parentCodeId;
    }

    public void setParentCodeId(Long parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
