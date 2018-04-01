package com.hand.hmall.as.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 张子龙
 * @version 0.1
 * @name Svsales
 * @description 服务销售单DTO
 * @date 2017/7/17
 */
@Table(name = "HMALL_AS_SVSALES")
public class Svsales extends BaseDTO {
    @Id
    @Column
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long asSvsalesId;

    @Column
    @ExcelVOAttribute(name = "服务销售单单号", column = "A", isExport = true)
    private String code;

    @Column
    private String status;

    @Column
    private Long serviceOrderId;

    @Column
    private Long orderId;

    @Column
    private String note;

    @Column
    private String name;

    @Column
    @ExcelVOAttribute(name = "手机号", column = "C", isExport = true)
    private String mobile;

    @Column
    private String address;

    @Column
    @ExcelVOAttribute(name = "受理客服", column = "D", isExport = true)
    private String cs;

    @Column
    private Date finishTime;

    @Column
    private String syncflag;

    @Column
    private Date creationDate;

    @Column
    private String payStatus;

    @Column
    @ExcelVOAttribute(name = "应付金额合计", column = "F", isExport = true)
    private BigDecimal shouldPay;

    @Column
    @ExcelVOAttribute(name = "实付金额合计", column = "G", isExport = true)
    private BigDecimal realPay;

    @Column
    private Date appointmentDate;

    @Column
    private String responsibleParty;
    @Transient
    @ExcelVOAttribute(name = "用户账号", column = "B", isExport = true)
    private String customerId;
    @Transient
    @ExcelVOAttribute(name = "收费项目", column = "E", isExport = true)
    private String chargeType;
    //服务销售单行
    @Transient
    private List<SvsalesEntry> svsalesEntries;
    @Transient
    //服务单列表页面上的搜索条件 创建日期从
    private String creationStartTime;
    @Transient
    //服务单列表页面上的搜索条件 创建日期至
    private String creationEndTime;
    @Transient
    //服务单列表页面上的搜索条件 完结日期从
    private String finishStartTime;
    @Transient
    //服务单列表页面上的搜索条件 完结日期至
    private String finishEndTime;
    @Transient
    //备注,用于服务销售单列表导出
    private String svsalesEntryNote;
    /**
     * 用户组
     */
    @Transient
    private String userGroup;
    @Transient
    private String escOrderCode;
    @Transient
    private String linkCode;
    @Column
    @XmlElement
    private String sapCode;
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
    @Transient
    //服务销售单序号,用于导入
    private String svsalesNum;
    @Transient
    private String serviceOrderCode;
    @Transient
    private String orderCode;

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public Long getAsSvsalesId() {
        return asSvsalesId;
    }

    public void setAsSvsalesId(Long asSvsalesId) {
        this.asSvsalesId = asSvsalesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public BigDecimal getRealPay() {
        return realPay;
    }

    public void setRealPay(BigDecimal realPay) {
        this.realPay = realPay;
    }

    public String getCreationStartTime() {
        return creationStartTime;
    }

    public void setCreationStartTime(String creationStartTime) {
        this.creationStartTime = creationStartTime;
    }

    public String getCreationEndTime() {
        return creationEndTime;
    }

    public void setCreationEndTime(String creationEndTime) {
        this.creationEndTime = creationEndTime;
    }

    public String getFinishStartTime() {
        return finishStartTime;
    }

    public void setFinishStartTime(String finishStartTime) {
        this.finishStartTime = finishStartTime;
    }

    public String getFinishEndTime() {
        return finishEndTime;
    }

    public void setFinishEndTime(String finishEndTime) {
        this.finishEndTime = finishEndTime;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public List<SvsalesEntry> getSvsalesEntries() {
        return svsalesEntries;
    }

    public void setSvsalesEntries(List<SvsalesEntry> svsalesEntries) {
        this.svsalesEntries = svsalesEntries;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
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

    public String getAppointmentDateString() {

        return appointmentDateString;
    }

    public void setAppointmentDateString(String appointmentDateString) {
        this.appointmentDateString = appointmentDateString;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSvsalesNum() {
        return svsalesNum;
    }

    public void setSvsalesNum(String svsalesNum) {
        this.svsalesNum = svsalesNum;
    }

    public String getServiceOrderCode() {
        return serviceOrderCode;
    }

    public void setServiceOrderCode(String serviceOrderCode) {
        this.serviceOrderCode = serviceOrderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSvsalesEntryNote() {
        return svsalesEntryNote;
    }

    public void setSvsalesEntryNote(String svsalesEntryNote) {
        this.svsalesEntryNote = svsalesEntryNote;
    }
}
