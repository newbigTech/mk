package com.hand.hmall.logistics.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * author: zhangzilong
 * name: Pin.java
 * discription: Pin码信息实体
 * date: 2017/11/9
 * version: 0.1
 */
@Table(name = "HMALL_PIN_INFO")
public class LgsPin {

    @Id
    @Column
    private Integer pinInfoId;

    @Column
    private String code;

    @Column
    private String entryCode;

    @Column
    private String eventCode;

    @Column
    private String operator;

    @Column
    private String mobile;

    @Column
    private Date operationTime;

    @Column
    private String eventInfo;

    public Integer getPinInfoId() {
        return pinInfoId;
    }

    public void setPinInfoId(Integer pinInfoId) {
        this.pinInfoId = pinInfoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
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

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }
}
