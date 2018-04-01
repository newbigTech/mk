package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name ErrorLine
 * @description 错误行
 * @date 2017/6/3 14:12
 */
public class ErrorLine {

    /*
    * 商品编码
    * */
    private String code;

    /*
    * 错误消息
    * */
    private String msg;

    public ErrorLine() {

    }

    public ErrorLine(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
