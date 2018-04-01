package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;

import java.util.Set;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description 促销商品关联关系pojo
 */
public class SalePromotionCodePojo {


    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 关联的促销活动编码
     */
    private String activityId;

    /**
     * 促销活动描述
     */
    private String meaning;

    /**
     * 促销活动分组
     */
    private String group;

    /**
     * 是否叠加
     */
    private String isOverlay;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 关联的id集合
     */
    private Set<String> definedIds;

    /**
     * definitionId类型，商品、商品分类、全部
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 开始时间
     */
    private Long startDate;

    /**
     * 结束时间
     */
    private Long endDate;

    /**
     * 比较符
     */
    private String rangeOperator;

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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Set<String> getDefinedIds() {
        return definedIds;
    }

    public void setDefinedIds(Set<String> definedIds) {
        this.definedIds = definedIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRangeOperator() {
        return rangeOperator;
    }

    public void setRangeOperator(String rangeOperator) {
        this.rangeOperator = rangeOperator;
    }

    @Override
    public String toString() {
        return "{\"SalePromotionCodePojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"activityId\":\"" + activityId + "\""
            + ",                         \"meaning\":\"" + meaning + "\""
            + ",                         \"group\":\"" + group + "\""
            + ",                         \"isOverlay\":\"" + isOverlay + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + ",                         \"definedIds\":" + definedIds
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"status\":\"" + status + "\""
            + ",                         \"startDate\":\"" + startDate + "\""
            + ",                         \"endDate\":\"" + endDate + "\""
            + ",                         \"rangeOperator\":\"" + rangeOperator + "\""
            + "}}";
    }
}
