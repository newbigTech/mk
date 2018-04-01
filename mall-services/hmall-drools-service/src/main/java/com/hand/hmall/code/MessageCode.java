package com.hand.hmall.code;

/**
 * @Describe d
 * @Author noob
 * @Date 2017/6/5 20:21
 */
public enum MessageCode {
    ACTIVITY_QUERY_01("促销活动获取成功"),
    ACTIVITY_PROMOTE_SUCCESS("订单促销完成"),
    NO_ACTIVITY("促销条数为0");
    private String key;

    private String value;

    private MessageCode(String value) {

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
