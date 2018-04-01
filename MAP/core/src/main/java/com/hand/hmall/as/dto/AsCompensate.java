package com.hand.hmall.as.dto;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensate
 * @description 销售赔付单头表DTO
 * @date 2017/10/11
 */

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_COMPENSATE")
public class AsCompensate extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long compensateId;

    @Column
    private String code;

    @Column
    private String status;

    @Column
    private Long orderId;

    @Column
    private Long serviceId;

    @Column
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
    private BigDecimal compensateFee;

    @Column
    private String sapCode;

    @Column
    private Date creationDate;

    /**
     * 平台订单编号
     */
    @Transient
    private String escOrderCode;
    /**
     * 用户编号
     */
    @Transient
    private String customerid;
    /**
     * 用户组
     */
    @Transient
    private String userGroup;
    /**
     * 单位描述
     */
    @Transient
    private String description;
    @Transient
    private String sex;//同步retail使用，性别
    @Transient
    private String soldParty;//同步retail使用，售达方
    @Transient
    private String salesOffice;//同步retail使用，销售办公室
    @Transient
    private String receiverDistrict;//同步retail使用，运输区域
    @Transient
    private String shippingType;
    @Transient
    private Date tradeFinishTime;

    @Transient
    //销售赔付单序号,用于导入
    private String compensateNum;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    private List<AsCompensateEntry> asCompensateEntryList;

    public List<AsCompensateEntry> getAsCompensateEntryList() {
        return asCompensateEntryList;
    }

    public Long getCompensateId() {
        return compensateId;
    }

    public void setCompensateId(Long compensateId) {
        this.compensateId = compensateId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public BigDecimal getCompensateFee() {
        return compensateFee;
    }

    public void setCompensateFee(BigDecimal compensateFee) {
        this.compensateFee = compensateFee;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Date getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(Date tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    public void setAsCompensateEntryList(List<AsCompensateEntry> asCompensateEntryList) {
        this.asCompensateEntryList = asCompensateEntryList;
    }

    public String getCompensateNum() {
        return compensateNum;
    }

    public void setCompensateNum(String compensateNum) {
        this.compensateNum = compensateNum;
    }
}
