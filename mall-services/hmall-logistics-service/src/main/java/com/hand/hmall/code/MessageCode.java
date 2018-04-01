package com.hand.hmall.code;

/**
 * @author 唐磊
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/8/10 14:50
 */
public enum MessageCode {


    UR_LOGIN_ERROR_01("请求体获取失败"),

    UR_LOGIN_ERROR_02("签名类型异常"),

    UR_LOGIN_ERROR_03("签名为空"),

    UR_LOGIN_ERROR_04("签名异常"),

    UR_LOGIN_ERROR_05("数据转换异常"),

    UR_LOGIN_ERROR_06("日期转换异常"),

    UR_LOGIN_ERROR_07("发货单数据不存在"),

    UR_LOGIN_SUCCESS_200("数据传输成功");

    private String key;

    private String value;

    private MessageCode(String value) {

        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;

    }
}
