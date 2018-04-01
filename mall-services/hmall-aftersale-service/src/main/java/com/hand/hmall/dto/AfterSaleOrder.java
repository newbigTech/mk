package com.hand.hmall.dto;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @name AfterSaleOrder
 * @Describe 售后单实体类
 * @Author chenzhigang
 * @Date 2017/7/23
 * @version 1.0
 */
public class AfterSaleOrder {

    // 售后单ID
    private Long receiptOrderId;
    //	单号
    private String code;
    //	单据类型（区分各种售后单据）
    private String receiptType;
    //	单据状态
    private String status;
    //	订单号
    private String salesCode;
    //	备注
    private String note;
    //	用户
    private String userId;
    //	手机号
    private String mobile;
    //	客户地址
    private String address;
    //	受理客服
    private String cs;
    //	完结时间
    private Date finishTime;
    //	同步retail标记
    private String syncflag;
    //	客服预约执行日期
    private Date appointmentDate;
    //	实际执行日期
    private Date executionDate;
    //	服务技师姓名
    private String technicianName;
    //	是否收费
    private String isCharge;
    //	服务单单号
    private String linksCode;
    //	支付状态
    private String payStatus;
    //	运费
    private Double transFee;
    //	再存储费
    private Double restoreFee;
    //	维修费
    private Double repairFee;
    //	应付金额合计
    private Double shouldPay;
    //	实付金额合计
    private Double realPay;
    //	用户名称
    private String name;
    //	退款状态
    private String refundStatus;
    //	退款原因
    private String refundReason;
    //	退款金额合计
    private Double refoundSum;
    //	退货原因
    private String returnReason;
    //	退货类型
    private String returnType;
    //	退货接受地点
    private String receivePosition;
    //	物流单号
    private String logisticsNumber;
    //	实际返回顾客金额
    private Double returnFee;

    @Transient
    private List<ServiceOrderEntry> afterOrderEntries;

    public List<ServiceOrderEntry> getAfterOrderEntries() {
        return afterOrderEntries;
    }

    public void setAfterOrderEntries(List<ServiceOrderEntry> afterOrderEntries) {
        this.afterOrderEntries = afterOrderEntries;
    }

    public Long getReceiptOrderId() {
        return receiptOrderId;
    }

    public void setReceiptOrderId(Long receiptOrderId) {
        this.receiptOrderId = receiptOrderId;
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

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public Double getRefoundSum() {
        return refoundSum;
    }

    public void setRefoundSum(Double refoundSum) {
        this.refoundSum = refoundSum;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

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
}
