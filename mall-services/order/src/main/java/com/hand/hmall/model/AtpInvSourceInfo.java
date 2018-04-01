package com.hand.hmall.model;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name AtpInvSourceInfo
 * @description   atp寻源路径实体类
 * @date 2017/11/16
 */

import javax.persistence.*;

@Entity
@Table(name = "HAP_ATP_INV_SOURCE_INFO")
public class AtpInvSourceInfo {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HAP_ATP_INV_SOURCE_INFO.nextval from dual")
    private Long infoId;


    private Long itemId;


    private String itemCode;


    private String cityCode;


    private String areaCode;


    private String storageCode;


    private Integer logisticsLeadTime;


    private String priorityLevels;


    private String effactFlag;


    private String deleteFlag;


    private Integer expressLeadTime;


    public Integer getExpressLeadTime() {
        return expressLeadTime;
    }

    public void setExpressLeadTime(Integer expressLeadTime) {
        this.expressLeadTime = expressLeadTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public Integer getLogisticsLeadTime() {
        return logisticsLeadTime;
    }

    public void setLogisticsLeadTime(Integer logisticsLeadTime) {
        this.logisticsLeadTime = logisticsLeadTime;
    }

    public void setPriorityLevels(String priorityLevels) {
        this.priorityLevels = priorityLevels;
    }

    public String getPriorityLevels() {
        return priorityLevels;
    }

    public void setEffactFlag(String effactFlag) {
        this.effactFlag = effactFlag;
    }

    public String getEffactFlag() {
        return effactFlag;
    }

}
