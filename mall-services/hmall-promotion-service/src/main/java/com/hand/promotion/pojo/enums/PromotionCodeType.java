package com.hand.promotion.pojo.enums;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/26
 * @description 促销商品关联关系类型
 */

public enum PromotionCodeType {
    /**
     * 商品
     */
    CODE("CODE"),
    /**
     * 商品分类
     */
    CATEGORY("CATEGORY"),

    /**
     * 订单层级
     */
    ALL("ALL");
    private String value;

    public String getValue() {
        return value;
    }

    PromotionCodeType(String value) {
        this.value = value;
    }
}
