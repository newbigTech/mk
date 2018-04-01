package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name LovData
 * @description LovData 具有编码和名称或值的结构体
 * @date 2017/6/6 14:00
 */
public class LovData {
    /*
    * 编码
    * */
    private String code;
    /*
    * 名称
    * */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
