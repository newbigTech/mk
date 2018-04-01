package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 促销规则详细信息pojo包括促销描述、促销条件、结果、容器、分组
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
@Document(collection = "SaleActivity")
public class PromotionActivitiesPojo implements java.io.Serializable{
    @Id
    private String id;
    private Integer priority;
    //促销活动描述
    private ActivityPojo activity;
    @Transient
    private String cacheOperator;
    //促销组条件
    private List<GroupPojo> groups;
    private Integer containerFlag;
    //促销活动条件
    private List<ConditionPojo> conditions;
    //促销结果
    private List<ActionPojo> actions;
    //促销活动容器
    private List<ContainerPojo> containers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ActivityPojo getActivity() {
        return activity;
    }

    public void setActivity(ActivityPojo activity) {
        this.activity = activity;
    }

    public String getCacheOperator() {
        return cacheOperator;
    }

    public void setCacheOperator(String cacheOperator) {
        this.cacheOperator = cacheOperator;
    }

    public List<GroupPojo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupPojo> groups) {
        this.groups = groups;
    }

    public Integer getContainerFlag() {
        return containerFlag;
    }

    public void setContainerFlag(Integer containerFlag) {
        this.containerFlag = containerFlag;
    }

    public List<ConditionPojo> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionPojo> conditions) {
        this.conditions = conditions;
    }

    public List<ActionPojo> getActions() {
        return actions;
    }

    public void setActions(List<ActionPojo> actions) {
        this.actions = actions;
    }

    public List<ContainerPojo> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerPojo> containers) {
        this.containers = containers;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{\"PromotionActivitiesPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + ",                         \"activity\":" + activity
            + ",                         \"cacheOperator\":\"" + cacheOperator + "\""
            + ",                         \"groups\":" + groups
            + ",                         \"containerFlag\":\"" + containerFlag + "\""
            + ",                         \"conditions\":" + conditions
            + ",                         \"actions\":" + actions
            + ",                         \"containers\":" + containers
            + "}}";
    }
}
