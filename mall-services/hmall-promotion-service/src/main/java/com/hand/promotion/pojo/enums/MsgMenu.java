package com.hand.promotion.pojo.enums;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 返回信息枚举类
 */

public enum MsgMenu {
    /**
     * 创建促销活动异常
     */
    CREATE_ACTIVITY_ERROR("CREATE_ACTIVITY_ERROR", "创建促销活动异常"),
    PROMOTION_PRICR_GREATE_THEN_REAL("PROMOTION_PRICR_GREATE_THEN_REAL", "商品价格<固定价格"),
    SYSTEM_ERR("SYSTEM_ERR", "程序异常"),

    /**
     * 创建促销活动成功
     */
    CREATE_ACTIVITY_SUCCESS("CREATE_ACTIVITY_SUCCESS", "创建促销活动成功"),

    BEAN_INVALID("BEAN_INVALID", "Bean校验异常"),

    COUPON_CODE_USED("COUPON_CODE_USED", "优惠券编码已使用"),

    DATE_FORMATE_ERR("DATE_FORMATE_ERR", "日期格式异常"),

    /**
     * 促销活动不能为空
     */
    ACTIVITY_CAN_NOT_NULL("ACTIVITY_CAN_NOT_NULL", "促销活动不能为空"),

    TO_MANY_RESULT("TO_MANY_RESULT", "返回结果过多"),

    /**
     * 促销更新状态异常提示
     */
    ACTIVITY_STATUS_ERR("ACTIVITY_STATUS_ERR", "该状态下促销活动不可修改"),

    COUPON_UPDATE_STATUS_ERR("COUPON_UPDATE_STATUS_ERR", "该状态下的优惠券不可更新"),

    REDEEM_COUPON_ERR("REDEEM_COUPON_ERR", "优惠券兑换异常"),

    /**
     * 优先级不能为空
     */
    PRIORITY_CAN_NOT_NULL("PRIORITY_CAN_NOT_NULL", "商品层级优先级不能为空"),

    ISOVERLAY_CAN_NOT_NULL("PRIORITY_CAN_NOT_NULL", "商品层级优先级不能为空"),

    CAL_FALSE("DISCOUNT_IS_NEGATIVE", "计算错误"),

    CAN_NOT_FIND_PROMPTE("CAN_NOT_FIND_PROMPTE", "查询不到促销活动"),

    CAN_NOT_FIND_COUPON("CAN_NOT_FIND_PROMPTE", "查询不到优惠券"),

    DELETE_ACTIVITY_STATUS_ERR("DELETE_ACTIVITY_STATUS_ERR", "只有失效状态的促销活动可被删除"),

    /**
     * 前台展示信息不能为空
     */
    PAGE_SHOW_MESSAGE_CAN_NOT_NULL("PAGE_SHOW_MESSAGE_CAN_NOT_NULL", "前台展示信息不能为空"),


    SEND_DATE_ERR("SEND_DATE_ERR", "允许领取时间要小于允许领取截止时间"),

    COUPON_DATE_ERR("COUPON_DATE_ERR", "生效时间要大于当前时间"),

    COUPON_STATUS_ERR("COUPON_STATUS_ERR", "优惠券状态异常"),

    CONVERT_COUNT_ERR("CONVERT_COUNT_ERR", "兑换数异常"),

    CONVER_ERR("CONVER_ERR", "兑换异常"),


    COUPON_END_DATE_ERR("COUPON_END_DATE_ERR", "优惠券结束时间要大于当前时间"),

    ACTIVITY_START_END_DATE_ERR("START_END_DATE_ERR", "失效时间要大于生效时间"),

    /**
     * 促销活动只能且必须有一个结果
     */
    ACTIVITY_ACTION_ONLYONE("ACTIVITY_ACTION_ONLYONE", "促销活动只能且必须有一个结果"),

    /**
     * 促销条件不能为空
     */
    CONDITION_CAN_NOT_NULL("CONDITION_CAN_NOT_NULL", "促销条件不能为空"),

    /**
     * definitionId不能为空
     */
    DEFINITION_ID_CAN_NOT_NULL("DEFINITION_ID_CAN_NOT_NULL", "definitionId不能为空"),

    /**
     * definitionId不能为空 非法
     */
    DEFINITION_ID_INVALIDATED("DEFINITION_ID_INVALIDATED", "definitionId在程序中不存在"),

    /**
     * 条件的parameter不能为空
     */
    PARAMETER_CAN_NOT_NULL("PARAMETER_CAN_NOT_NULL", "条件的parameter不能为空"),

    CONVERT_PARM_ERR("CONVERT_PARM_ERR", "兑换参数异常"),

    /**
     * 范围比较符不合法
     */
    RANGE_OPERATOR_INVALID("OPERATOR_INVALID", "范围比较符不合法"),

    /**
     * 范围条件，结果数值不合法
     */
    RANGE_VALUE_INVALID("VALUE_INVALID", "范围条件，结果数值不合法"),

    /**
     * 比较符不合法
     */
    OPERATOR_INVALID("OPERATOR_INVALID", "比较符不合法"),

    /**
     * 条件，结果数值不合法
     */
    VALUE_INVALID("VALUE_INVALID", "条件，结果数值不合法"),

    /**
     * 目标包价格未选择容器
     */
    NONE_CONTAINER("NONE_CONTAINER", "目标包价格未选择容器"),

    /**
     * 创建促销分组异常
     */


    CRAETE_GROUP_ERR("CRAETE_GROUP_ERR", "创建促销分组异常"),

    DEL_GROUP_ERR("DEL_GROUP_ERR", "删除促销分组异常"),

    /**
     * 成功
     */
    SUCCESS("SUCCESS", "成功"),

    /**
     * 没有可用促销
     */
    HAS_NO_VALID_ACTIVITY_RULE("HAS_NO_VALID_ACTIVITY_RULE", "没有可用促销"),

    NO_VALID_CACHE_INSTANCE("NO_VALID_CACHE_INSTANCE", "缓存名称不正确"),

    /**
     * 促销分组数据不合法
     */
    GROUP_CONDITION_INVALID("GROUP_CONDITION_INVALID", "促销分组数据不合法"),

    /**
     * container条件异常
     */
    CONTAINER_CONDITION_INVALID("CONTAINER_CONDITION_INVALID", "container条件异常"),

    /**
     * 促销条件匹配失败
     */
    NOT_MATCH_RULE("NOT_MATCH_RULE", "促销条件匹配失败"),

    NO_CONDITIONS("NO_CONDITIONS", "促销规则不存在可用条件"),

    ACTION_SIZE_ERR("ACTION_SIZE_ERR", "只能有一个促销结果"),

    GROUP_USED("GROUP_USED", "分组已被使用"),

    SEND_MQ_ERR("SEND_MQ_ERR", "消息发布异常"),

    ORDER_CREATED_CAN_NOT_NULL("ORDER_CREATED_CAN_NOT_NULL", "订单创建时间不能为空"),

    ORDER_CREATED_FORMATE_ERR("ORDER_CREATED_FORMATE_ERR", "订单创建时间格式异常"),

    ORDER_DATA_ERR("ORDER_DATA_ERR", "订单数据异常"),

    PROMOTE_DATA_ERR("PROMOTE_DATA_ERR", "促销数据异常"),

    ORDER_ENTRY_CAN_NOT_NULL("ORDER_ENTRY_CAN_NOT_NULL", "订单行不能为空"),

    PROMOTION_DATE_INVALID("PROMOTION_DATE_INVALID", "促销时间不符"),

    INACTIVT_ERR("INACTIVT_ERR", "停用异常"),

    COUPON_IS_NULL("COUPON_IS_NULL", "优惠券信息为空"),

    CUSTOMER_COUPN_NOT_EXIST("CUSTOMER_COUPN_NOT_EXIST", "用户不拥有改优惠券"),

    CUSTOMER_COUPN_NOT_VALID("CUSTOMER_COUPN_NOT_VALID", "用户优惠券校验不通过"),

    GENERATE_REDEEM_CODE_ERR("GENERATE_REDEEM_CODE_ERR", "兑换码生成异常"),

    OPERATE_COUPON_ERR("OPERATE_COUPON_ERR", "优惠券占用释放异常"),

    PICK_UP_NO_GIFT("PICK_UP_NO_GIFT", "门店自提订单不参与促销活动"),
    PRODUCT_ERR("PRODUCT_ERR", "商品数据异常"),
    CATEGORY_ERR("CATEGORY_ERR","商品分类数据异常"),
    APPEND_FIELD_ERR("APPEND_FIELD_ERR","添加订单字段异常");

    private String code;
    private String msg;

    MsgMenu(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    @Override
    public String toString() {
        return "{\"MsgMenu\":{"
                + "                        \"code\":\"" + code + "\""
                + ",                         \"msg\":\"" + msg + "\""
                + "}}";
    }
}
