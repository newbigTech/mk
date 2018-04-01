package com.hand.hmall.menu;/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

/**
 * 促销活动的促销类型
 */
public enum PromotionType {
    o_total_discount("订单减x元"),
    o_total_rate("订单打x折"),
    o_meet_delete("订单每满X元减Y元"),
    oe_meet_delete("订单行每满X元减Y元"),

    oe_total_discount("订单行减X元"),
    oe_total_rate("订单行X折"),
    o_fixed_rate("商品固定折扣"),
    o_fixed_number("商品固定金额"),
    o_target_price("目标包价格"),
    o_giver_product("赠品"),
    o_freight_waiver("免邮免安装费");

    private String mean;

    PromotionType(String mean) {
        this.mean = mean;
    }

    public String getMean() {
        return mean;
    }
}
