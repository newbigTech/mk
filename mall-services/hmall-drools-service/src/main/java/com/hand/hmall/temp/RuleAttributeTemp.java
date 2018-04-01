package com.hand.hmall.temp;

/**
 * Created by hand on 2017/2/22.
 */
public class RuleAttributeTemp implements java.io.Serializable{
    //是否可叠加
    private String isOverlay;
    //分组
    private String group;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;
    //优先级
    private int priority=10000;
    //规则类型
    private String ruleType;

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
