package com.hand.hmall.menu;

/**
 * Created by shanks on 2017/4/11.
 */
public enum  Gift {
    GIFT("赠品编码","gift");
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

    Gift(String name,String value) {
        this.name = name;
        this.value=value;
    }
}
