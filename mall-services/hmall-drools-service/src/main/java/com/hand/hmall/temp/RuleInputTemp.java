package com.hand.hmall.temp;

import com.hand.hmall.model.Coupon;

import java.util.List;
import java.util.Map;

/**
 * 创建促销优惠券对应DTO
 */
public class RuleInputTemp {

    private List<DefinitionTemp> conditions;
    private List<DefinitionTemp> actions;
    private List<Map> containers;
    //逻辑连接的组，和规则执行的分组不是一个概念
    private List<Map> groups;
    private Coupon coupon;
    private ActivityTemp activity;

    public List<DefinitionTemp> getConditions() {
        return conditions;
    }

    public void setConditions(List<DefinitionTemp> conditions) {
        this.conditions = conditions;
    }

    public List<DefinitionTemp> getActions() {
        return actions;
    }

    public void setActions(List<DefinitionTemp> actions) {
        this.actions = actions;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public ActivityTemp getActivity() {
        return activity;
    }

    public void setActivity(ActivityTemp activity) {
        this.activity = activity;
    }

    public List<Map> getContainers() {
        return containers;
    }

    public void setContainers(List<Map> containers) {
        this.containers = containers;
    }

    public List<Map> getGroups() {
        return groups;
    }

    public void setGroups(List<Map> groups) {
        this.groups = groups;
    }
}
