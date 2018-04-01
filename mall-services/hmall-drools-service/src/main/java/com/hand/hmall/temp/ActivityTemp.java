package com.hand.hmall.temp;

/**
 * 促销活动基本信息对应DTO
 * Created by hand on 2017/2/22.
 */
public class ActivityTemp implements java.io.Serializable{
    //id
    private String id;
     //促销活动
     private String activityId;
     //是否可叠加
     private String isOverlay;
     //是否从店面显示中排除
     private String isExcludeShow;
     //分组
     private String group = "DEFAULT";
     //开始时间
     private String startDate;
     //结束时间
     private String endDate;
     //活动名称
     private String activityName;
     //活动描述
     private String activityDes;
     //优先级
     private int priority;
     //前台提示消息
     private String pageShowMes;
     //最大执行数
//     private int maxExecute;
     //创建时间
     private String creationTime;
     //上次修改时间
     private String lastCreationTime;

    //促销级别
    private String type;
    //商品编码
    private String productCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public String getIsExcludeShow() {
        return isExcludeShow;
    }

    public void setIsExcludeShow(String isExcludeShow) {
        this.isExcludeShow = isExcludeShow;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getActivityDes() {
        return activityDes;
    }

    public void setActivityDes(String activityDes) {
        this.activityDes = activityDes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

//    public int getMaxExecute() {
//        return maxExecute;
//    }
//
//    public void setMaxExecute(int maxExecute) {
//        this.maxExecute = maxExecute;
//    }

    public String getPageShowMes() {
        return pageShowMes;
    }

    public void setPageShowMes(String pageShowMes) {
        this.pageShowMes = pageShowMes;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastCreationTime() {
        return lastCreationTime;
    }

    public void setLastCreationTime(String lastCreationTime) {
        this.lastCreationTime = lastCreationTime;
    }

    @Override
    public String toString() {
        return "{\"ActivityTemp\":{"
                + "                        \"id\":\"" + id + "\""
                + ",                         \"activityId\":\"" + activityId + "\""
                + ",                         \"isOverlay\":\"" + isOverlay + "\""
                + ",                         \"isExcludeShow\":\"" + isExcludeShow + "\""
                + ",                         \"group\":\"" + group + "\""
                + ",                         \"startDate\":\"" + startDate + "\""
                + ",                         \"endDate\":\"" + endDate + "\""
                + ",                         \"activityName\":\"" + activityName + "\""
                + ",                         \"activityDes\":\"" + activityDes + "\""
                + ",                         \"priority\":\"" + priority + "\""
                + ",                         \"pageShowMes\":\"" + pageShowMes + "\""
                + ",                         \"creationTime\":\"" + creationTime + "\""
                + ",                         \"lastCreationTime\":\"" + lastCreationTime + "\""
                + ",                         \"type\":\"" + type + "\""
                + ",                         \"productCode\":\"" + productCode + "\""
                + "}}";
    }
}
