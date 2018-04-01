package com.hand.hmall.menu;


public enum CouponType {
    COUPON_TYPE_01("优惠券兑换"),COUPON_TYPE_02("客户领取"),COUPON_TYPE_03("抽奖获取"),COUPON_TYPE_04("管理员推送"),COUPON_TYPE_05("用户购买");

    public static boolean contains(String type){
        for(CouponType couponType : CouponType.values()){
            if(couponType.name().equals(type)){
                return true;
            }
        }
        return false;
    }

    private String value;

    CouponType() {

    }

    CouponType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
