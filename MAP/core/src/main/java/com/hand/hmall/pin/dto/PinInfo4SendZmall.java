package com.hand.hmall.pin.dto;

/**
 * @author chenzhigang
 * @version 0.1
 * @name PinInfo4SendZmall
 * @description 推送到Zmall端的PIN码信息结构
 * @date 2017/8/17
 */
public class PinInfo4SendZmall {

    //PIN码 eg. P10000000XM
    private String pinCode;

    //商城订单编号 eg. 201707121172900
    private String zmallOrderCode;

    //订单行号 eg. 10
    private String orderEntryCode;

    //事件编号 eg. A1
    private String eventCode;

    //事件描述 eg. 配件备货
    private String eventDes;

    //系统 eg. xxx
    private String system;

    //操作人员 eg. xxx
    private String operator;

    //操作人电话 eg. 12345678910
    private String mobile;

    //操作时间 eg. YYYY-MM-DDhh:mm:ss
    private String operationTime;

    //节点信息 eg. xxx
    private String eventInfo;


    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getZmallOrderCode() {
        return zmallOrderCode;
    }

    public void setZmallOrderCode(String zmallOrderCode) {
        this.zmallOrderCode = zmallOrderCode;
    }

    public String getOrderEntryCode() {
        return orderEntryCode;
    }

    public void setOrderEntryCode(String orderEntryCode) {
        this.orderEntryCode = orderEntryCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventDes() {
        return eventDes;
    }

    public void setEventDes(String eventDes) {
        this.eventDes = eventDes;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

}
