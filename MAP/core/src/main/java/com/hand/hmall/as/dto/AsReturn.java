package com.hand.hmall.as.dto;

/**
 * @author liuhongxi
 * @version 0.1
 * @name AsReturn
 * @description 退货单Dto
 * @date 2017/8/23
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;

@Entity
@XmlAccessorType(XmlAccessType.NONE)
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_RETURN")
public class AsReturn extends BaseDTO {

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)

    @Column
    private Long asReturnId;

    @Column
    private String code;

    @Column
    @XmlElement
    private String status;

    @Column
    private Long serviceOrderId;

    @Column
    private Long orderId;

    @Column
    @XmlElement
    private String note;

    @Column
    private String name;

    @Column
    private String mobile;

    @Column
    private String address;

    @Column
    private String cs;

    @Column
    private Date finishTime;

    @Column
    private String syncflag;

    @Column
    private Date appointmentDate;

    @Column
    private Date executionDate;

    @Column
    private Double transFee;

    @Column
    private Double packageFee;

    @Column
    private String returnType;

    @Column
    private Double returnFee;

    @Column
    private String receivePosition;

    @Column
    private String logisticsNumber;

    @Column
    @XmlElement
    private String sapCode;

    @Column
    private String responsibleParty;

    @Transient
    private String userGroup;

    @Transient
    private String customerid;

    @Transient
    private String vtweg;

    @Transient
    private List<AsReturnEntry> asReturnEntryList;

    @Transient
    private String returnTypeToRetail;

    @Transient
    private String soldParty;

    @Transient
    private String salesOffice;

    @Transient
    private String sex;

    @Transient
    private String receiverDistrict;

    @Transient
    private String appointmentDateString;

    @Transient
    private String creationDateString;

    @Transient
    private String creationTimeString;

    //订单表中的code字段
    @Transient
    private String orderCode;

    //服务单表中的code字段
    @Transient
    private String serviceOrderCode;

    private Date creationDate;

    @Transient
    private Double currentAmount;

    @Transient
    private Double orderAmount;

    private Double referenceFee;

    private Long swapOrderId;

    @Transient
    private String chosenCoupon;

    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public Long getSwapOrderId() {
        return swapOrderId;
    }

    public void setSwapOrderId(Long swapOrderId) {
        this.swapOrderId = swapOrderId;
    }

    @Transient
    private Double paymentAmount;
    
    @Transient
    private String receivePositionText;

    /**
     * 订单类型 天猫还是电商
     */
    @Transient
    private String websiteId;

    @Transient
    private String swapOrderCode;

    @Transient
    private String escOrderCode;

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getReferenceFee() {
        return referenceFee;
    }

    public void setReferenceFee(Double referenceFee) {
        this.referenceFee = referenceFee;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<AsReturnEntry> getAsReturnEntryList() {
        return asReturnEntryList;
    }

    public void setAsReturnEntryList(List<AsReturnEntry> asReturnEntryList) {
        this.asReturnEntryList = asReturnEntryList;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    public Double getTransFee() {
        return transFee;
    }

    public void setTransFee(Double transFee) {
        this.transFee = transFee;
    }

    public Double getPackageFee() {
        return packageFee;
    }

    public void setPackageFee(Double packageFee) {
        this.packageFee = packageFee;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Double getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Double returnFee) {
        this.returnFee = returnFee;
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

    public String getReturnTypeToRetail() {
        return returnTypeToRetail;
    }

    public void setReturnTypeToRetail(String returnTypeToRetail) {
        this.returnTypeToRetail = returnTypeToRetail;
    }

    public String getSoldParty() {
        return soldParty;
    }

    public void setSoldParty(String soldParty) {
        this.soldParty = soldParty;
    }

    public String getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getAppointmentDateString() {
        return appointmentDateString;
    }

    public void setAppointmentDateString(String appointmentDateString) {
        this.appointmentDateString = appointmentDateString;
    }

    public String getCreationDateString() {
        return creationDateString;
    }

    public void setCreationDateString(String creationDateString) {
        this.creationDateString = creationDateString;
    }

    public String getCreationTimeString() {
        return creationTimeString;
    }

    public void setCreationTimeString(String creationTimeString) {
        this.creationTimeString = creationTimeString;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getServiceOrderCode() {
        return serviceOrderCode;
    }

    public void setServiceOrderCode(String serviceOrderCode) {
        this.serviceOrderCode = serviceOrderCode;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getSwapOrderCode() {
        return swapOrderCode;
    }

    public void setSwapOrderCode(String swapOrderCode) {
        this.swapOrderCode = swapOrderCode;
    }

    public String getReceivePositionText() {
        return receivePositionText;
    }

    public void setReceivePositionText(String receivePositionText) {
        this.receivePositionText = receivePositionText;
    }
}
