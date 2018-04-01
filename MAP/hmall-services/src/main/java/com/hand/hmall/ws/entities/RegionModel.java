package com.hand.hmall.ws.entities;

import java.io.Serializable;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: RegionModel
 * @description:地址推送接收数据实体类
 * @Date: 2017/6/21
 * \
 */
public class RegionModel implements Serializable {
    /**
     * 区域代码
     */
    private String ZADDR;
    /**
     * 国家键值
     */
    private String LAND1;
    /**
     * 名称
     */
    private String BEZEI;
    /**
     * 删除标记
     */
    private String ZZEDL;
    /**
     * 父节点代码
     */
    private String ADDR_UP;
    /**
     * 地址全称
     */
    private String ZZDZQC;

    public String getZADDR() {
        return ZADDR;
    }

    public void setZADDR(String ZADDR) {
        this.ZADDR = ZADDR;
    }

    public String getLAND1() {
        return LAND1;
    }

    public void setLAND1(String LAND1) {
        this.LAND1 = LAND1;
    }

    public String getBEZEI() {
        return BEZEI;
    }

    public void setBEZEI(String BEZEI) {
        this.BEZEI = BEZEI;
    }

    public String getZZEDL() {
        return ZZEDL;
    }

    public void setZZEDL(String ZZEDL) {
        this.ZZEDL = ZZEDL;
    }

    public String getADDR_UP() {
        return ADDR_UP;
    }

    public void setADDR_UP(String ADDR_UP) {
        this.ADDR_UP = ADDR_UP;
    }

    public String getZZDZQC() {
        return ZZDZQC;
    }

    public void setZZDZQC(String ZZDZQC) {
        this.ZZDZQC = ZZDZQC;
    }
}
