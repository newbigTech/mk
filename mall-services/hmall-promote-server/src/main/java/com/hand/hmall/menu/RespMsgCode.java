package com.hand.hmall.menu;


public enum RespMsgCode {
    PROMOTE_COUPON_001("兑换码错误"),PROMOTE_COUPON_002("优惠券被抢光了"),PROMOTE_COUPON_003("兑换次数达到上限");

    public static boolean contains(String type){
        for(RespMsgCode respMsgCode : RespMsgCode.values()){
            if(respMsgCode.value.equals(type)){
                return true;
            }
        }
        return false;
    }

    private String value;

    RespMsgCode() {
    }

    RespMsgCode(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }
}
