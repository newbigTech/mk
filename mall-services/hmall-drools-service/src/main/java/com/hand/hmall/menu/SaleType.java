package com.hand.hmall.menu;

/**
 * Created by shanks on 2017/4/6.
 */
public enum  SaleType {
    ACTIVITY("促销","ACTIVITY"),
    COUPON("优惠","COUPON"),
    DRAW("抽奖","DRAW"),
    TEMPLATE("模板","TEMPLATE");
    private String name;
    private String value;

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

    SaleType(String name,String value) {
        this.name = name;
        this.value=value;
    }
}
