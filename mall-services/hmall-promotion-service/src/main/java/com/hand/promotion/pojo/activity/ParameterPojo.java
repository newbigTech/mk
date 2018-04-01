package com.hand.promotion.pojo.activity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 条件参数
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ParameterPojo implements java.io.Serializable {
    /**
     * 数量
     */
    private String value;

    /**
     * 比较符
     */
    private String operator;
    /**
     * 范围值
     */
    private RangeValuePojo rangeValue;
    /**
     * 范围操作符（包含或排除）
     */
    private RangeOperatorPojo rangeOperator;

    private List<TargetValuePojo> targetValue;

    private List<MatchValuePojo> matchValue;

    public String getValue() {
        return value;
    }

    public ValuePojo transValuePojo() {
        try {
            ValuePojo valuePojo = JSON.parseObject(value, ValuePojo.class);
            return valuePojo;
        } catch (Exception e) {
            ValuePojo valuePojo = new ValuePojo();
            valuePojo.setValue(value);
            return valuePojo;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public OperatorPojo transOperatorPojo() {
        try {
            OperatorPojo operatorPojo = JSON.parseObject(operator, OperatorPojo.class);
            return operatorPojo;
        } catch (Exception e) {
            OperatorPojo operatorPojo = new OperatorPojo();
            operatorPojo.setValue(operator);
            return operatorPojo;
        }
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public RangeValuePojo getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(RangeValuePojo rangeValue) {
        this.rangeValue = rangeValue;
    }

    public RangeOperatorPojo getRangeOperator() {
        return rangeOperator;
    }

    public void setRangeOperator(RangeOperatorPojo rangeOperator) {
        this.rangeOperator = rangeOperator;
    }

    public List<TargetValuePojo> getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(List<TargetValuePojo> targetValue) {
        this.targetValue = targetValue;
    }

    public List<MatchValuePojo> getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(List<MatchValuePojo> matchValue) {
        this.matchValue = matchValue;
    }
}
