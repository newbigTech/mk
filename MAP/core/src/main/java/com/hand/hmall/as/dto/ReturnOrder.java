package com.hand.hmall.as.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ReturnOrder
 * @description 退货单实体类
 * @date 2017/7/17
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_RECEIPT")
public class ReturnOrder extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long receiptOrderId;

    @ExcelVOAttribute(name = "服务单编号", column = "A", isExport = true)
    private String code;


    private String receiptType;


    @ExcelVOAttribute(name = "服务单状态", column = "B", isExport = true)
    private String status;


    @ExcelVOAttribute(name = "服务类别1", column = "F", isExport = true)
    private String svCategory1;


    @ExcelVOAttribute(name = "服务类别2", column = "G", isExport = true)
    private String svCategory2;


    private String salesCode;


    private String shippingType;


    private String complaint;


    private String note;

    private String logisticsNumber;

    private String receivePosition;

    private String returnReason;

    private String returnType;

    private String userId;

    @ExcelVOAttribute(name = "用户手机号", column = "D", isExport = true)
    private String mobile;

    private String address;

    @ExcelVOAttribute(name = "跟单客服", column = "E", isExport = true)
    private String cs;

    @ExcelVOAttribute(name = "完结时间", column = "I", isExport = true)
    private Date finishTime;

    private String syncflag;

    private Date appointmentDate;

    private Date executionDate;

    private String technicianName;

    private String isCharge;

    @ExcelVOAttribute(name = "创建时间", column = "H", isExport = true)
    private Date creationDate;

    @Column
    private String linksCode;

    @Column
    private String payStatus;

    @Column
    private Double transFee;

    @Column
    private Double restoreFee;

    @Column
    private Double repairFee;

    @Column
    private Double shouldPay;

    @Column
    private Double realPay;

    @Column
    private Double returnFee;

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

    @Column
    private String name;
    /**
     * 用户组
     */
    @Transient
    private String userGroup;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReceivePosition() {
        return receivePosition;
    }

    public void setReceivePosition(String receivePosition) {
        this.receivePosition = receivePosition;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Double getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Double returnFee) {
        this.returnFee = returnFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public Long getReceiptOrderId() {
        return receiptOrderId;
    }

    public void setReceiptOrderId(Long receiptOrderId) {
        this.receiptOrderId = receiptOrderId;
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

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSvCategory1() {
        return svCategory1;
    }

    public void setSvCategory1(String svCategory1) {
        this.svCategory1 = svCategory1;
    }

    public String getSvCategory2() {
        return svCategory2;
    }

    public void setSvCategory2(String svCategory2) {
        this.svCategory2 = svCategory2;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

    public String getLinksCode() {
        return linksCode;
    }

    public void setLinksCode(String linksCode) {
        this.linksCode = linksCode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Double getTransFee() {
        return transFee;
    }

    public void setTransFee(Double transFee) {
        this.transFee = transFee;
    }

    public Double getRestoreFee() {
        return restoreFee;
    }

    public void setRestoreFee(Double restoreFee) {
        this.restoreFee = restoreFee;
    }

    public Double getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(Double repairFee) {
        this.repairFee = repairFee;
    }

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public Double getRealPay() {
        return realPay;
    }

    public void setRealPay(Double realPay) {
        this.realPay = realPay;
    }
}
