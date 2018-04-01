package com.hand.promotion.pojo.enums;

/**
 * 优惠券类型
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum CouponType {
    COUPON_TYPE_01("优惠券兑换", "COUPON_TYPE_01"),
    COUPON_TYPE_02("客户领取", "COUPON_TYPE_02"),
    COUPON_TYPE_03("抽奖获取", "COUPON_TYPE_03"),
    COUPON_TYPE_04("管理员推送", "COUPON_TYPE_04"),
    COUPON_TYPE_05("用户购买", "COUPON_TYPE_05");

    private String name;
    private String value;

    CouponType(String name, String value) {
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
