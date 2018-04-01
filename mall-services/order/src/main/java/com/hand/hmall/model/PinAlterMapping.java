package com.hand.hmall.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author: zhangzilong
 * name: PinAlterMapping
 * discription: PIN码预警映射表
 * date: 2017/11/17
 * version: 0.1
 */
@Entity
@Table(name = "HMALL_PIN_ALTER_MAPPING")
public class PinAlterMapping{

    @Id
    @Column
    private Integer alterMappingId;

    @Column
    private String customerSupportType;

    @Column 
    private String eventCode;
    
    @Column
    private String nextEventCode;

    @Column
    private Integer level1Time;

    @Column
    private Integer level2Time;

    @Column
    private Integer level3Time;

    public Integer getAlterMappingId() {
        return alterMappingId;
    }

    public void setAlterMappingId(Integer alterMappingId) {
        this.alterMappingId = alterMappingId;
    }

    public String getCustomerSupportType() {
        return customerSupportType;
    }

    public void setCustomerSupportType(String customerSupportType) {
        this.customerSupportType = customerSupportType;
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

    public Integer getLevel1Time() {
        return level1Time;
    }

    public void setLevel1Time(Integer level1Time) {
        this.level1Time = level1Time;
    }

    public Integer getLevel2Time() {
        return level2Time;
    }

    public void setLevel2Time(Integer level2Time) {
        this.level2Time = level2Time;
    }

    public Integer getLevel3Time() {
        return level3Time;
    }

    public void setLevel3Time(Integer level3Time) {
        this.level3Time = level3Time;
    }
}
