package com.hand.hmall.code;

/**
 * author: zhangzilong
 * name: MQConstants
 * discription:
 * date: 2017/10/16
 * version: 0.1
 */
public class MQConstants {

    // 业务消息交换机名称
    public static final String EXCHANGE_NAME = "map.business";

    // 消息内部流转的routing key
    public static final String INTERNAL_ROUTING_KEY = "map-internal";

    // 系统间消息流转的routing key
    public static final String EXTERNAL_ROUTING_KEY = "public";

    // 消息头：业务消息标识码key
    public static final String HEADER_BUSINESS_CODE = "business_code";

}
