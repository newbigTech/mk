package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhangzilong
 * name: PinAlterInfo
 * discription: 预警信息表
 * date: 2017/11/17
 * version: 0.1
 */
@Entity
@Table(name = "HMALL_PIN_ALTER_INFO")
public class PinAlterInfo{

    @Id
    @Column
    private Integer alterInfoId;

    @Column
    private String pin;

    @Column
    private String eventCode;

    @Column
    private String nextEventCode;

    @Column
    private Date level1Time;

    @Column
    private Date level2Time;

    @Column
    private Date level3Time;

    @Transient
    private String nextEventDes;

    @Transient
    private Double level1DelayHours;

    @Transient
    private Double level2DelayHours;

    @Transient
    private Double level3DelayHours;

    public Integer getAlterInfoId() {
        return alterInfoId;
    }

    public void setAlterInfoId(Integer alterInfoId) {
        this.alterInfoId = alterInfoId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getNextEventCode() {
        return nextEventCode;
    }

    public void setNextEventCode(String nextEventCode) {
        this.nextEventCode = nextEventCode;
    }

    public Date getLevel1Time() {
        return level1Time;
    }

    public void setLevel1Time(Date level1Time) {
        this.level1Time = level1Time;
    }

    public Date getLevel2Time() {
        return level2Time;
    }

    public void setLevel2Time(Date level2Time) {
        this.level2Time = level2Time;
    }

    public Date getLevel3Time() {
        return level3Time;
    }

    public void setLevel3Time(Date level3Time) {
        this.level3Time = level3Time;
    }

    public String getNextEventDes() {
        return nextEventDes;
    }

    public void setNextEventDes(String nextEventDes) {
        this.nextEventDes = nextEventDes;
    }

    public Double getLevel1DelayHours() {
        return level1DelayHours;
    }

    public void setLevel1DelayHours(Double level1DelayHours) {
        this.level1DelayHours = Math.floor(level1DelayHours*10)/10;
    }

    public Double getLevel2DelayHours() {
        return level2DelayHours;
    }

    public void setLevel2DelayHours(Double level2DelayHours) {
        this.level2DelayHours = Math.floor(level2DelayHours*10)/10;
    }

    public Double getLevel3DelayHours() {
        return level3DelayHours;
    }

    public void setLevel3DelayHours(Double level3DelayHours) {
        this.level3DelayHours = Math.floor(level3DelayHours*10)/10;
    }
}
