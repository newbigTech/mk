package com.hand.promotion.pojo.activity;


/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ValuePojo implements java.io.Serializable {
    private String value;
    private String front;
    private String quantity;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
