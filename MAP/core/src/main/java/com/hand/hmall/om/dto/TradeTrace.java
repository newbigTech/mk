package com.hand.hmall.om.dto;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name TradeTrace 物流跟踪表 DTO类
 * @description
 * @date 2017/8/11
 */

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_TRADE_TRACE")
public class TradeTrace extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long tradeTraceId;

    @Column
    private Long lineid;

    @Column
    private Long consignmentId;

    @Column
    private String thirdpartyServicecode;

    @Column
    private String thirdpartyUniquecode;

    @Column
    private String status;

    @Column
    private String operator;

    @Column
    private String operatorphone;

    @Column
    private Date operatetime;

    @Column
    private String content;

    @Column
    private String syncflag;

    @Column
    private String appointDate;

    @Column
    private String deliveryDate;

    @Column
    private String signDate;

    @Column
    private String changeAppointDate;

    @Column
    private Integer deliveryOrderId;

    @Transient
    private String deliveryNote;

    public Long getTradeTraceId() {
        return tradeTraceId;
    }

    public void setTradeTraceId(Long tradeTraceId) {
        this.tradeTraceId = tradeTraceId;
    }

    public Long getLineid() {
        return lineid;
    }

    public void setLineid(Long lineid) {
        this.lineid = lineid;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getThirdpartyServicecode() {
        return thirdpartyServicecode;
    }

    public void setThirdpartyServicecode(String thirdpartyServicecode) {
        this.thirdpartyServicecode = thirdpartyServicecode;
    }

    public String getThirdpartyUniquecode() {
        return thirdpartyUniquecode;
    }

    public void setThirdpartyUniquecode(String thirdpartyUniquecode) {
        this.thirdpartyUniquecode = thirdpartyUniquecode;
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

    public String getOperatorphone() {
        return operatorphone;
    }

    public void setOperatorphone(String operatorphone) {
        this.operatorphone = operatorphone;
    }

    public Date getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
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

    public Integer getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Integer deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }
}
