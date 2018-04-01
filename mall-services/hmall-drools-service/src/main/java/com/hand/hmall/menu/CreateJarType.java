package com.hand.hmall.menu;

/**
 * Created by shanks on 2017/4/10.
 */
public enum  CreateJarType {
    ORIGINAL("默认分组","ORIGINAL"),
    FREE_FREIGHT("免邮费分组","FREE_FREIGHT");


    private String name;
    private String value;
    CreateJarType(String name,String value){
        this.name=name;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
