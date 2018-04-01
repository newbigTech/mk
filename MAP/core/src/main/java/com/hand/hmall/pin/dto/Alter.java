package com.hand.hmall.pin.dto;

import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 错误警报表
 * @version 0.1
 * @name Alter
 * @description PIN码实体类
 * @date 2017/8/10
 */
@Table(name = "HMALL_PIN_ALTER")
public class Alter extends BaseDTO {
    /**
     * 主键
     */
    //  主键
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long pinAlterId;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 事件描述
     */
    private String eventDes;

    /**
     * 一级预警
     */
    private String alterLevel1;

    /**
     * 二级预警
     */
    private String alterLevel2;

    /**
     * 三级预警
     */
    private String alterLevel3;

    public Long getPinAlterId() {
        return pinAlterId;
    }

    public void setPinAlterId(Long pinAlterId) {
        this.pinAlterId = pinAlterId;
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

    public String getAlterLevel1() {
        return alterLevel1;
    }

    public void setAlterLevel1(String alterLevel1) {
        this.alterLevel1 = alterLevel1;
    }

    public String getAlterLevel2() {
        return alterLevel2;
    }

    public void setAlterLevel2(String alterLevel2) {
        this.alterLevel2 = alterLevel2;
    }

    public String getAlterLevel3() {
        return alterLevel3;
    }

    public void setAlterLevel3(String alterLevel3) {
        this.alterLevel3 = alterLevel3;
    }
}