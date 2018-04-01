package com.hand.promotion.pojo.enums;

/**
 * 操作符
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum OperatorMenu {
    /**
     * 大于等于
     */
    GEATER_THAN_OR_EQUAL(">=", "GEATER_THAN_OR_EQUAL"),
    /**
     * 大于
     */
    GEATER_THAN(">", "GEATER_THAN"),
    /**
     * 等于
     */
    EQUAL("==", "EQUAL"),
    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL("<=", "LESS_THAN_OR_EQUAL"),
    /**
     * 小于
     */
    LESS_THAN("<", "LESS_THAN"),
    /**
     * 包含
     */
    MEMBER_OF("memberOf", "MEMBER_OF"),
    /**
     * 不包含
     */
    NOT_MEMBER_OF("not memberOf", "NOT_MEMBER_OF"),
    /**
     * 包含
     */
    CONTAINS("contains", "CONTAINS"),
    /**
     * 不包含
     */
    NOT_CONTAINS("not contains", "NOT_CONTAINS"),
    /**
     * 包含
     */
    IN("in", "IN"),
    /**
     * 不包含
     */
    NOT_IN("not in", "NOT_IN"),
    /**
     * 或者
     */
    OR("or", "OR");

    private String value;
    private String name;

    OperatorMenu(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static OperatorMenu findOperatorMenuByValue(String name) {
        for (OperatorMenu bean : OperatorMenu.values()) {
            if (bean.getName().equals(name)) {
                return bean;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }


    public String getName() {
        return name;
    }


    /**
     * 校验枚举值是否存在
     *
     * @param operator
     * @return boolean
     */
    public static boolean isExist(String operator) {

        OperatorMenu[] values = OperatorMenu.values();
        for (OperatorMenu value : values) {
            if(value.getValue().equals(operator)){
                return true;
            }
        }
        return false;

    }
}
