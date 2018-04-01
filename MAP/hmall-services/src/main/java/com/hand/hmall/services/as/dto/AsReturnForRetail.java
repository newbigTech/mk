package com.hand.hmall.services.as.dto;

/**
 * @author liuhongxi
 * @version 0.1
 * @name AsReturn
 * @description 退货单Dto
 * @date 2017/8/23
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;

@XmlAccessorType(XmlAccessType.NONE)
public class AsReturnForRetail implements Serializable {
    private Long asReturnId;

    private String code;

    @XmlElement
    private String status;

    private Long serviceOrderId;

    private Long orderId;

    @XmlElement
    private String note;

    private String name;

    private String mobile;

    private String address;

    private String cs;

    private Date finishTime;

    private String syncflag;

    private Date appointmentDate;

    private Date executionDate;

    private Long transFee;

    private Long packageFee;

    private String returnType;

    private Long returnFee;

    private String receivePosition;

    private String logisticsNumber;

    @XmlElement
    private String sapCode;

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }


    public void setAsReturnId(Long asReturnId) {
        this.asReturnId = asReturnId;
    }

    public Long getAsReturnId() {
        return asReturnId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getCs() {
        return cs;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setTransFee(Long transFee) {
        this.transFee = transFee;
    }

    public Long getTransFee() {
        return transFee;
    }

    public void setPackageFee(Long packageFee) {
        this.packageFee = packageFee;
    }

    public Long getPackageFee() {
        return packageFee;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnFee(Long returnFee) {
        this.returnFee = returnFee;
    }

    public Long getReturnFee() {
        return returnFee;
    }

    public void setReceivePosition(String receivePosition) {
        this.receivePosition = receivePosition;
    }

    public String getReceivePosition() {
        return receivePosition;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

}
