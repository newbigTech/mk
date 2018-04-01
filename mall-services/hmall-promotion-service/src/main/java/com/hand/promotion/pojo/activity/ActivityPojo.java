package com.hand.promotion.pojo.activity;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

/**
 * 促销活动描述pojo
 *
 * @author mxy
 * @date 2017/12/11
 */
public class ActivityPojo implements java.io.Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 促销活动编码id
     */
    private String activityId;

    /**
     * 促销活动名称
     */
    @NotNull
    private String activityName;
    /**
     * 促销活动描述
     */
    @NotNull
    private String activityDes;

    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 生效时间
     */
    @NotNull
    private Long startDate;
    /**
     * 失效时间
     */
    @NotNull
    private Long endDate;
    /**
     * 促销活动创建时间
     */
    private Long creationTime;
    /**
     * 最近一次修改时间
     */
    private Long lastCreationTime;
    /**
     * 是否在前台展示
     */
    @NotNull
    private String isExcludeShow;

    /**
     * 前台页面展示信息
     */
    private String pageShowMes;

    /**
     * 促销分组
     */
    @NotNull
    private String group;

    /**
     * 促销活动状态
     */
    private String status;

    /**
     * 是否叠加
     */
    private String isOverlay;
    /**
     * 促销层级,订单层级\商品层级
     */
    @NotNull
    private String type;

    /**
     * 促销活动是否是最新版本
     */
    private String isUsing;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getIsExcludeShow() {
        return isExcludeShow;
    }

    public void setIsExcludeShow(String isExcludeShow) {
        this.isExcludeShow = isExcludeShow;
    }

    public String getPageShowMes() {
        return pageShowMes;
    }

    public void setPageShowMes(String pageShowMes) {
        this.pageShowMes = pageShowMes;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public String getType() {
        return type;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastCreationTime() {
        return lastCreationTime;
    }

    public void setLastCreationTime(Long lastCreationTime) {
        this.lastCreationTime = lastCreationTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    @Override
    public String toString() {
        return "{\"ActivityPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"activityName\":\"" + activityName + "\""
            + ",                         \"activityDes\":\"" + activityDes + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + ",                         \"startDate\":\"" + startDate + "\""
            + ",                         \"endDate\":\"" + endDate + "\""
            + ",                         \"isExcludeShow\":\"" + isExcludeShow + "\""
            + ",                         \"pageShowMes\":\"" + pageShowMes + "\""
            + ",                         \"group\":\"" + group + "\""
            + ",                         \"status\":\"" + status + "\""
            + ",                         \"isOverlay\":\"" + isOverlay + "\""
            + ",                         \"type\":\"" + type + "\""
            + "}}";
    }

    @Override
    public ActivityPojo clone() throws CloneNotSupportedException {
        return (ActivityPojo) super.clone();
    }
}
