package com.hand.hmall.dto;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

/**
 * @author XinyangMei
 * @Title CtaegoryInfo
 * @Description desp
 * @date 2017/9/20 11:39
 */
public class CategoryInfo {
    private Long uid;
    private String code;
    private String name;
    private String parentCode;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
