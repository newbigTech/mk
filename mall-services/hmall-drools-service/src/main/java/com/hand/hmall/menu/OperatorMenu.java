package com.hand.hmall.menu;

/**
 * Created by hand on 2017/1/9.
 */
public enum OperatorMenu {
    GEATER_THAN_OR_EQUAL(">="),GEATER_THAN(">"),EQUAL("=="),LESS_THAN_OR_EQUAL("<="),LESS_THAN("<"),
    MEMBER_OF("memberOf"),NOT_MEMBER_OF("not memberOf"),CONTAINS("contains"),NOT_CONTAINS("not contains"),
    IN("in"),NOT_IN("not in");

    private String value;

    OperatorMenu(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
