package com.hand.hap.im.dto;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name ImAtpCapacityInfaceWs
 * @description ATP基础数据dto  用于接收soap接口的数据 对应ImAtpCapacityInface  dto
 * @date 2017/6/21
 **/


public class ImAtpCapacityInfaceWs {

    /**
     * 组id
     */
    private String groupId;

    /**
     * 物料号
     */
    private String matnr;

    /**
     * 产能
     */
    private String capacity;

    /**
     * 周产能
     */
    private String weeklyCapacity;

    /**
     * 工厂
     */
    private String werks;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getWeeklyCapacity() {
        return weeklyCapacity;
    }

    public void setWeeklyCapacity(String weeklyCapacity) {
        this.weeklyCapacity = weeklyCapacity;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }
}
