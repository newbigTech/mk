package com.hand.hmall.as.dto;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsRefund
 * @description 退款单DTO
 * @date 2017/12/13
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_REFUND")
public class AsRefund extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long asRefundId;

    @Column
    @ExcelVOAttribute(name = "退款单号", column = "A", isExport = true)
    private String code;

    @Column
    @ExcelVOAttribute(name = "状态", column = "K", isExport = true)
    private String status;

    @Column
    private Long serviceOrderId;

    @Column
    private Long orderId;

    @Column
    @ExcelVOAttribute(name = "备注", column = "O", isExport = true)
    private String note;

    @Column
    private String mobile;

    @Column
    private String address;

    @Column
    private String cs;

    @Column
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelVOAttribute(name = "完成时间", column = "N", isExport = true)
    private Date finishTime;

    @Column
    private String syncflag;

    @Column
    @ExcelVOAttribute(name = "是否已退款", column = "J", isExport = true)
    private String payStatus;

    @Column
    private String name;

    @Column
    private BigDecimal refoundSum;

    @Column
    private String returnReason;

    @Column
    private String node;

    /**
     * 实际返回客户金额
     * 根据returnId关联到退货单的returnFee
     */
    @Column
    private Double returnFee;

    @Transient
    private String refundCode;

    @Transient
    @ExcelVOAttribute(name = "服务单单号", column = "D", isExport = true)
    private String serviceCode;

    @Column
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelVOAttribute(name = "创建时间", column = "M", isExport = true)
    private Date creationDate;

    @Column
    private BigDecimal returnId;

    @Column
    private BigDecimal referenceSum;

    @Column
    @ExcelVOAttribute(name = "退款场景", column = "L", isExport = true)
    private String refundScenario;

    @Transient
    @ExcelVOAttribute(name = "退款方式", column = "E", isExport = true)
    private String payMode;

    @Transient
    private List<RefundEntry> refundEntryList;

    @Transient
    //from字段用于区分新建退款单的入口,from=asReturnOrderDetail代表从退货单页面跳转到新建退款单页面的
    private String from;

    //用于前台传入的订单金额与数据库作比较
    @Transient
    private BigDecimal orderAmount;//订单金额
    @Transient
    private Double currentAmount;
    @Transient
    private Double paymentAmount;

    @Transient
    @ExcelVOAttribute(name = "原销售订单号", column = "C", isExport = true)
    private String orderCode;

    @Transient
    @ExcelVOAttribute(name = "用户账户", column = "B", isExport = true)
    private String customerid;

    @Transient
    private String creationDateStart;

    @Transient
    private String creationDateEnd;

    @Transient
    private String finishTimeEnd;

    @Transient
    private String finishTimeStart;

    @Transient
    @ExcelVOAttribute(name = "支付账户", column = "F", isExport = true)
    private String account;

    @Transient
    @ExcelVOAttribute(name = "可退款金额", column = "G", isExport = true)
    private BigDecimal couldAmount;

    @Transient
    @ExcelVOAttribute(name = "退款金额", column = "H", isExport = true)
    private BigDecimal payAmount;

    @Transient
    @ExcelVOAttribute(name = "修改退款原因", column = "I", isExport = true)
    private String changeReason;

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAsRefundId() {
        return asRefundId;
    }

    public void setAsRefundId(Long asRefundId) {
        this.asRefundId = asRefundId;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRefoundSum() {
        return refoundSum;
    }

    public void setRefoundSum(BigDecimal refoundSum) {
        this.refoundSum = refoundSum;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public List<RefundEntry> getRefundEntryList() {
        return refundEntryList;
    }

    public void setRefundEntryList(List<RefundEntry> refundEntryList) {
        this.refundEntryList = refundEntryList;
    }

    public BigDecimal getReturnId() {
        return returnId;
    }

    public void setReturnId(BigDecimal returnId) {
        this.returnId = returnId;
    }

    public BigDecimal getReferenceSum() {
        return referenceSum;
    }

    public void setReferenceSum(BigDecimal referenceSum) {
        this.referenceSum = referenceSum;
    }

    public String getRefundScenario() {
        return refundScenario;
    }

    public void setRefundScenario(String refundScenario) {
        this.refundScenario = refundScenario;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getCreationDateStart() {
        return creationDateStart;
    }

    public void setCreationDateStart(String creationDateStart) {
        this.creationDateStart = creationDateStart;
    }

    public String getCreationDateEnd() {
        return creationDateEnd;
    }

    public void setCreationDateEnd(String creationDateEnd) {
        this.creationDateEnd = creationDateEnd;
    }

    public String getFinishTimeEnd() {
        return finishTimeEnd;
    }

    public void setFinishTimeEnd(String finishTimeEnd) {
        this.finishTimeEnd = finishTimeEnd;
    }

    public String getFinishTimeStart() {
        return finishTimeStart;
    }

    public void setFinishTimeStart(String finishTimeStart) {
        this.finishTimeStart = finishTimeStart;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getCouldAmount() {
        return couldAmount;
    }

    public void setCouldAmount(BigDecimal couldAmount) {
        this.couldAmount = couldAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Double getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Double returnFee) {
        this.returnFee = returnFee;
    }
}
