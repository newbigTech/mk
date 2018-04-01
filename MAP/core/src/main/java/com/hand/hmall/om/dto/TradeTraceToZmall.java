package com.hand.hmall.om.dto;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name TradeTraceToZmall
 * @description 物流信息传送zmall商城DTO
 * @date 2017/8/11
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_TRADE_TRACE")
public class TradeTraceToZmall extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    @JsonIgnore
    private Long tradeTraceId;

    @Column
    @JsonIgnore
    private Long consignmentId;

    @Column
    private String status;

    @Column
    private String operator;

    @Column
    private String mobile;

    @Column
    private Date operationTime;

    @Column
    private String content;

    @Column
    private String pictrueUrl;

    @Column
    private String appointDate;

    @Column
    private String deliveryDate;

    @Column
    private String signDate;

    @Column
    private String changeAppointDate;

    @Column
    @JsonIgnore
    private String syncflag;

    @Transient
    private String consignmentCode;

    public Long getTradeTraceId() {
        return tradeTraceId;
    }

    public void setTradeTraceId(Long tradeTraceId) {
        this.tradeTraceId = tradeTraceId;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getChangeAppointDate() {
        return changeAppointDate;
    }

    public void setChangeAppointDate(String changeAppointDate) {
        this.changeAppointDate = changeAppointDate;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getConsignmentCode() {
        return consignmentCode;
    }

    public void setConsignmentCode(String consignmentCode) {
        this.consignmentCode = consignmentCode;
    }

    public String getPictrueUrl() {
        return pictrueUrl;
    }

    public void setPictrueUrl(String pictrueUrl) {
        this.pictrueUrl = pictrueUrl;
    }
}

