package com.hand.promotion.pojo.enums;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/17
 * @description 优惠券占用释放操作符枚举类
 */

public enum OperatorCouponMenu {
    USED("占用", "1"),
    UN_USED("释放", "2");
    private String name;
    private String value;

    OperatorCouponMenu(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static OperatorCouponMenu getByValue(String value) {
        OperatorCouponMenu[] values = OperatorCouponMenu.values();
        for (OperatorCouponMenu operatorCouponMenu : values) {
            if (operatorCouponMenu.getValue().equals(value)) {
                return operatorCouponMenu;
            }
        }
        return null;
    }
}
