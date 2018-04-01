package com.hand.hmall.pin.dto;

import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author chenzhigang
 * @version 0.1
 * @name Pin
 * @description PIN码实体类
 * @date 2017/8/4
 */
@Table(name = "HMALL_PIN_INFO")
public class Pin extends BaseDTO {

    //  主键
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long pinInfoId;

    //  PIN码
    @Column
    private String code;

    //  订单行号
    @Column
    private String entryCode;

    //  事件编号
    @Column
    private String eventCode;

    //  事件描述
    @Column
    private String eventDes;

    //  系统
    @Column
    private String system;

    //  操作人员（账号）
    @Column
    private String operator;

    //  操作人电话
    @Column
    private String mobile;

    //  操作时间
    @Column
    private Date operationTime;

    //  节点信息
    @Column
    private String eventInfo;

    //  一级预警标记
    @Column
    private String flagLevel1;

    //  二级预警标记
    @Column
    private String flagLevel2;

    //  三级预警标记
    @Column
    private String flagLevel3;

    public Long getPinInfoId() {
        return pinInfoId;
    }

    public void setPinInfoId(Long pinInfoId) {
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

    public String getFlagLevel1() {
        return flagLevel1;
    }

    public void setFlagLevel1(String flagLevel1) {
        this.flagLevel1 = flagLevel1;
    }

    public String getFlagLevel2() {
        return flagLevel2;
    }

    public void setFlagLevel2(String flagLevel2) {
        this.flagLevel2 = flagLevel2;
    }

    public String getFlagLevel3() {
        return flagLevel3;
    }

    public void setFlagLevel3(String flagLevel3) {
        this.flagLevel3 = flagLevel3;
    }
}
