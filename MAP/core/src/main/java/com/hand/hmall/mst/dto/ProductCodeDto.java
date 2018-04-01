package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @version 0.1
 * @Create By shoupeng.wei@hang-china.com
 * @Create at 2017年7月27日18:47:38
 * @Descrition 商品块码推送相关的Dto
 */
@ExtensionAttribute(disable = true)
@Table(name = "SYS_CODE_B")
public class ProductCodeDto {

    private String type;

    private String code;

    private String description;

    @Transient
    private String value;

    @Transient
    private String meaning;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
