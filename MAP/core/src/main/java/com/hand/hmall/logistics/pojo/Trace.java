package com.hand.hmall.logistics.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Consignment
 * @description 发货记录dto
 * @date 2017年6月5日13:54:47
 */
@Table(name = "HMALL_OM_TRADE_TRACE")
@Entity
public class Trace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_TRADE_TRACE_S.nextval from dual")
    private Long tradeTraceId;

    private Long lineid;

    private Long consignmentId;

    private String status;

    private String operator;

    private String operatorphone;

    // 图片地址
    private String pictureUrl;

    private Date operatetime;

    private String content;
    private String appointDate;
    private String deliveryDate;
    private String signDate;
    private String changeAppointDate;
    private String thirdServiceCode;

    private Long deliveryOrderId;

    public String getOperatorphone() {
        return operatorphone;
    }

    public void setOperatorphone(String operatorphone) {
        this.operatorphone = operatorphone;
    }

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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getThirdServiceCode() {
        return thirdServiceCode;
    }

    public void setThirdServiceCode(String thirdServiceCode) {
        this.thirdServiceCode = thirdServiceCode;
    }

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }
}
