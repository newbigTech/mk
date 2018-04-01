package com.hand.hmall.menu;

/**
 * Created by hand on 2016/12/20.
 */
public enum Status {
    STATUS_01("可使用"),STATUS_02("已使用"),STATUS_03("失效");

    private String value;

    Status() {}

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String type){
        for(Status status : Status.values()){
            if(status.name().equals(type)){
                return true;
            }
        }
        return false;
    }
}
