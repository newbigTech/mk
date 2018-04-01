package com.hand.promotion.pojo.enums;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum ConditionActions {
    /**
     * 订单满X元
     */
    o_total_reached("订单满X元", "o_total_reached", "CONDITION"),
    /**
     * 订单满X件
     */
    o_quantity_reached("订单满X件", "o_quantity_reached", "CONDITION"),
    /**
     * 商品范围(件)
     */
    o_product_range("商品范围(件)", "o_product_range", "CONDITION"),
    /**
     * 商品范围（总价）
     */
    o_product_range_number("商品范围（总价）", "o_product_range_number", "CONDITION"),
    /**
     * 类别范围(件)
     */
    o_type_range("类别范围(件)", "o_type_range", "CONDITION"),
    /**
     * 类别范围（总价）
     */
    o_type_range_number("类别范围（总价）", "o_type_range_number", "CONDITION"),
    /**
     * 客户范围
     */
    o_user_range("客户范围", "o_user_range", "CONDITION"),
    /**
     * 地区范围
     */
    o_area_range("地区范围", "o_area_range", "CONDITION"),
    /**
     * 渠道范围
     */
    o_channel_range("渠道范围", "o_channel_range", "CONDITION"),
    /**
     * 组
     */
    GROUP("组", "GROUP", "CONDITION"),
    /**
     * 容器
     */
    CONTAINER("容器", "CONTAINER", "CONDITION"),
    /**
     * 订单减X元
     */
    o_total_discount("订单减X元", "o_total_discount", "ACTION"),
    /**
     * 订单打x折
     */
    o_total_rate("订单打x折", "o_total_rate", "ACTION"),
    /**
     * 运费减免
     */
    o_freight_waiver("运费减免", "o_freight_waiver", "ACTION"),
    /**
     * 赠送优惠券
     */
    o_giver_coupon("赠送优惠券", "o_giver_coupon", "ACTION"),
    /**
     * 赠品
     */
    o_giver_product("赠品", "o_giver_product", "ACTION"),
    /**
     * 免单X件
     */
    o_free_number("免单X件", "o_free_number", "ACTION"),
    /**
     * 商品固定价格
     */
    o_fixed_number("商品固定价格", "o_fixed_number", "ACTION"),
    /**
     * 商品固定折扣
     */
    o_fixed_rate("商品固定折扣", "o_fixed_rate", "ACTION"),
    /**
     * 商品购买前X件减Y元
     */
    o_front_delete("商品购买前X件减Y元", "o_front_delete", "ACTION"),
    /**
     * 商品购买前X件打Y折
     */
    o_front_rate("商品购买前X件打Y折", "o_front_rate", "ACTION"),
    /**
     * 订单每满X元减Y元
     */
    o_meet_delete("订单每满X元减Y元", "o_meet_delete", "ACTION"),
    /**
     * 订单每满X件减Y元
     */
    o_meet_number_delete("订单每满X件减Y元", "o_meet_number_delete", "ACTION"),
    /**
     * 固定价格购买其他商品
     */
    o_fixed_price_buy("固定价格购买其他商品", "o_fixed_price_buy", "ACTION"),
    /**
     * 固定折扣购买其他商品
     */
    o_fixed_rate_buy("固定折扣购买其他商品", "o_fixed_rate_buy", "ACTION"),
    /**
     * 目标包价格
     */
    o_target_price("目标包价格", "o_target_price", "ACTION"),

    oe_total_reached("订单行满X元", "oe_total_reached", "CONDITION"),

    oe_total_discount("订单行减x元", "oe_total_discount", "ACTION"),

    oe_meet_delete("订单行每满x元减y元", "oe_meet_delete", "ACTION"),

    oe_total_rate("订单行打x折", "oe_total_rate", "ACTION"),

    o_discount_ladders("订单阶梯折扣", "o_discount_ladders", "ACTION"),

    p_number_discount("商品前X件商品固定价格", "p_number_discount", "ACTION"),

    p_number_rate("商品前X件固定折扣", "p_number_rate", "ACTION");

    private String name;
    private String value;
    private String type;

    ConditionActions(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static ConditionActions getConditionActionsByValue(String value) {
        for (ConditionActions bean : ConditionActions.values()) {
            if (bean.getValue().equals(value)) {
                return bean;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }


    public String getValue() {
        return value;
    }

}
