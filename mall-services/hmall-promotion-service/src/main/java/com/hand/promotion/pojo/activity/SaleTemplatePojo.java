package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销模板实体类
 */
@Document(collection = "SaleTemplate")
public class SaleTemplatePojo {
    @Id
    private String id;

    /**
     * 促销活动描述信息
     */
    SaleTemplateDesp template;

    /**
     * 组条件
     */
    private List<GroupPojo> groups;


    /**
     * 促销基础条件
     */
    private List<ConditionPojo> conditions;


    /**
     * 促销结果
     */
    private List<ActionPojo> actions;


    /**
     * 容器条件
     */
    private List<ContainerPojo> containers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SaleTemplateDesp getTemplate() {
        return template;
    }

    public void setTemplate(SaleTemplateDesp template) {
        this.template = template;
    }

    public List<GroupPojo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupPojo> groups) {
        this.groups = groups;
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

    @Override
    public String toString() {
        return "{\"SaleTemplatePojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"template\":" + template
            + ",                         \"groups\":" + groups
            + ",                         \"conditions\":" + conditions
            + ",                         \"actions\":" + actions
            + ",                         \"containers\":" + containers
            + "}}";
    }
}
