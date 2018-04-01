package com.hand.promotion.pojo.enums;

/**
 * 活动状态
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum Status {
    /**
     * 活动中
     */
    ACTIVITY("活动中", "ACTIVITY"),
    /**
     * 已停用
     */
    INACTIVE("已停用", "INACTIVE"),
    /**
     * 已失效
     */
    INVALID("已失效", "INVALID"),
    /**
     * 已过期
     */
    FAILURE("已过期", "FAILURE"),
    /**
     * 删除
     */
    EXPR("删除", "EXPR"),
    /**
     * 已启用
     */
    ENABLED("已启用", "ENABLED"),
    /**
     * 已停用
     */
    DISABLED("已停用", "DISABLED"),
    /**
     * 待生效
     */
    INITIAL("待生效", "INITIAL"),
    /**
     * 全部
     */
    ALL("全部", "ALL"),
    /**
     * 待生效
     */
    DELAY("待生效", "DELAY");


    private String name;
    private String value;

    Status(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public String getName() {
        return name;
    }

}
