package com.hand.hmall.menu;

/**
 * Created by shanks on 2017/3/8.
 */
public enum ConditionActions {
    oe_total_reached("订单行满X元","CONDITION"),
    o_total_reached("订单满X元","CONDITION"),
    o_quantity_reached("订单满X件","CONDITION"),
    o_product_range("商品范围","CONDITION"),
    o_type_range("类别范围","CONDITION"),
    o_user_range("客户范围","CONDITION"),
    o_area_range("地区范围","CONDITION"),
    o_channel_range("渠道范围","CONDITION"),
    GROUP("组","CONDITION"),
    CONTAINER("容器","CONDITION"),

    oe_total_discount("订单减X元","ACTION"),
    oe_total_rate("订单行打x折","ACTION"),
    o_total_discount("订单减X元","ACTION"),
    o_total_rate("订单打x折","ACTION"),
    o_front_delete("商品购买前X件减Y元","ACTION"),
    o_front_rate("商品购买前X件打Y折","ACTION"),
    o_meet_delete("订单每满X元减Y元","ACTION"),
    o_freight_waiver("运费减免","ACTION"),
    o_giver_coupon("赠送优惠券","ACTION"),
    o_free_number("免单X件","ACTION"),
    o_fixed_number("商品固定价格","ACTION"),
    o_fixed_rate("商品固定折扣","ACTION"),
    o_fixed_price_buy("固定价格购买其他商品","ACTION"),
    o_fixed_rate_buy("固定折扣购买其他商品","ACTION"),
    o_distributed_gifts("赠品","ACTION"),
    o_target_price("目标包价格","ACTION");

    private String name;
    private String type;

    ConditionActions(String name,String type) {
        this.name = name;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
