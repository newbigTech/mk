package com.hand.promotion.pojo.enums;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 商品配送方式枚举类
 */

public enum ShippingType {

    LOGISTICS("物流", "LOGISTICS"),

    EXPRESS("快递", "EXPRESS");

    /**
     * 名称
     */
    private String name;

    /**
     * 取值
     */
    private String value;


    ShippingType(String name, String value) {
        this.name = name;
        this.value = value;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
