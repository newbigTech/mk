package com.hand.hmall.code;

/**
 * @Describe 订单下载返回状态码枚举类
 * @Author noob
 * @Date 2017/6/5 20:21
 * @version 1.0
 */
public enum MessageCode {
    OR_ADD_01("订单下载成功"),
    OR_ADD_02("订单下载失败"),
    OR_UPDATE_01("订单更新成功"),
    OR_UPDATE_02("订单不存在,无法更新"),
    OR_MSG_01("订单必填字段不能为空"),
    OR_MSG_02("必须传入规定字段"),
    OR_MSG_03("支付信息已存在"),
    OR_MSG_04("订单行和支付信息不能为空"),
    OR_MSG_05("数据存储失败"),
    OR_PRODUCT_01("订单行商品不存在");
    private String key;

    private String value;

    private MessageCode( String value) {

        this.value = value;
    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;

    }

}
