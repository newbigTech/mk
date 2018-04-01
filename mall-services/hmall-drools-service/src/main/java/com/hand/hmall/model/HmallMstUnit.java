package com.hand.hmall.model;

import java.util.Date;

public class HmallMstUnit {
    /**
     * 主键
     */
    private Long unitId;

    /**
     * 单位编码
     */
    private String code;

    /**
     * 单位名称
     */
    private String description;

    /**
     * 单位类型
     */
    private String type;

    /**
     * 基础单位
     */
    private String baseUnit;

    /**
     * 单位转换率
     */
    private Double rate;

    /**
     * 是否启用
     */
    private String active;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{\"HmallMstUnit\":{"
                + "                        \"unitId\":\"" + unitId + "\""
                + ",                         \"code\":\"" + code + "\""
                + ",                         \"description\":\"" + description + "\""
                + ",                         \"type\":\"" + type + "\""
                + ",                         \"baseUnit\":\"" + baseUnit + "\""
                + ",                         \"rate\":\"" + rate + "\""
                + ",                         \"active\":\"" + active + "\""
                + "}}";
    }
}