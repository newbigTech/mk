package com.hand.hmall.om.dto;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hand on 2017/7/29.
 */
@Table(name = "HMALL_OM_PROMOTIONRULE")
public class OmPromotionruleDto {

    @Column
    private String activityId;

    @Column
    private String activityName;

    @Column
    private String releaseId;

    @Column
    private String type;

    @Column
    private String activityDes;

    @Transient
    private String pageShowMes;

    private String startTime;

    private String endTime;

    @Column
    private BigDecimal amount;

    @Column
    private String gift;

    @Column
    private BigDecimal giftQuantity;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityDes() {
        return activityDes;
    }

    public void setActivityDes(String activityDes) {
        this.activityDes = activityDes;
    }

    public String getPageShowMes() {
        return pageShowMes;
    }

    public void setPageShowMes(String pageShowMes) {
        this.pageShowMes = pageShowMes;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public BigDecimal getGiftQuantity() {
        return giftQuantity;
    }

    public void setGiftQuantity(BigDecimal giftQuantity) {
        this.giftQuantity = giftQuantity;
    }

}
