package com.hand.promotion.pojo.coupon;

import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 优惠券规则提交pojo
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
@Document(collection = "SaleCoupon")
public class PromotionCouponsPojo implements java.io.Serializable {

    /**
     * 优惠券主键
     */
    @Id
    private String id;

    /**
     * 缓存操作符
     */
    private String cacheOperator;
    /**
     * 优惠券描述信息
     */
    private CouponsPojo coupon;

    /**
     * 分组条件
     */
    private List<GroupPojo> groups;
    private Integer containerFlag;

    /**
     * 基础条件集合
     */
    private List<ConditionPojo> conditions;

    /**
     * 优惠结果集合,size必须为1
     */
    private List<ActionPojo> actions;

    /**
     * 容器集合
     */
    private List<ContainerPojo> containers;
    private String conditionsId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CouponsPojo getCoupon() {
        return coupon;
    }

    public String getCacheOperator() {
        return cacheOperator;
    }

    public void setCacheOperator(String cacheOperator) {
        this.cacheOperator = cacheOperator;
    }

    public void setCoupon(CouponsPojo coupon) {
        this.coupon = coupon;
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

    public String getConditionsId() {
        return conditionsId;
    }

    public void setConditionsId(String conditionsId) {
        this.conditionsId = conditionsId;
    }

    @Override
    public String toString() {
        return "{\"PromotionCouponsPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"coupon\":" + coupon
            + ",                         \"groups\":" + groups
            + ",                         \"containerFlag\":\"" + containerFlag + "\""
            + ",                         \"conditions\":" + conditions
            + ",                         \"actions\":" + actions
            + ",                         \"containers\":" + containers
            + ",                         \"conditionsId\":\"" + conditionsId + "\""
            + "}}";
    }
}
