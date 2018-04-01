package com.hand.promotion.pojo.enums;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/18
 * @description 促销层级，商品层级、订单层级
 */

public enum ActivityType {
    order("1"),
    product("2");
    private String value;

    ActivityType(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }
}
