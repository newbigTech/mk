package com.hand.promotion.pojo.activity;


import java.util.List;
import java.util.Map;

/**
 * 订单参与促销的明细
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ActivityDetailPojo implements java.io.Serializable {
    /**
     * 促销活动主键
     */
    private String activityId;

    /**
     * 促销活动折扣总金额
     */
    private Double discount;

    /**
     * 促销生效时间
     */
    private String startDate;
    /**
     * 促销截止时间
     */
    private String endDate;
    /**
     * 促销活动名称
     */
    private String activityName;

    /**
     * 促销活动描述
     */
    private String activityDesp;
    /**
     * 前台展示信息
     */
    private String pageShowMes;

    private List<Map> entryDiscountInfo;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<Map> getEntryDiscountInfo() {
        return entryDiscountInfo;
    }

    public void setEntryDiscountInfo(List<Map> entryDiscountInfo) {
        this.entryDiscountInfo = entryDiscountInfo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesp() {
        return activityDesp;
    }

    public void setActivityDesp(String activityDesp) {
        this.activityDesp = activityDesp;
    }

    public String getPageShowMes() {
        return pageShowMes;
    }

    public void setPageShowMes(String pageShowMes) {
        this.pageShowMes = pageShowMes;
    }

    @Override
    public String toString() {
        return "{\"ActivityDetailPojo\":{"
            + "                        \"activityId\":\"" + activityId + "\""
            + ",                         \"discount\":\"" + discount + "\""
            + ",                         \"startDate\":\"" + startDate + "\""
            + ",                         \"endDate\":\"" + endDate + "\""
            + ",                         \"activityName\":\"" + activityName + "\""
            + ",                         \"activityDesp\":\"" + activityDesp + "\""
            + ",                         \"pageShowMes\":\"" + pageShowMes + "\""
            + ",                         \"entryDiscountInfo\":" + entryDiscountInfo
            + "}}";
    }
}
