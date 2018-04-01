package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @name RefundOrder
 * @Describe 退款单
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Table(name = "HMALL_AS_REFUND")
public class RefundOrder {

    // 主键
    @Id
    @GeneratedValue
    @Column
    private Long asRefundId;

    // 退款单单号
    @Column
    private String code;

    // 单据状态
    @Column
    private String status;

    // 服务单ID
    @Column
    private Long serviceOrderId;

    // 销售订单ID
    @Column
    private Long orderId;

    // 备注
    @Column
    private String note;

    // 手机号
    @Column
    private String mobile;

    // 客户地址
    @Column
    private String address;

    // 受理客服
    @Column
    private String cs;

    // 完结时间
    @Column
    private Date finishTime;

    // 同步retail标记
    @Column
    private String syncflag;

    // 退款状态
    @Column
    private String payStatus;

    // 用户名称
    @Column
    private String name;

    // 退款金额合计
    @Column
    private Double refoundSum;

    // 退货原因
    @Column
    private String returnReason;

    // 版本号
    @Column
    private Long objectVersionNumber;

    @Column
    private Date creationDate;

    @Column
    private Long createdBy;

    @Column
    private Long lastUpdatedBy;

    @Column
    private Date lastUpdateDate;

    @Column
    private Long lastUpdateLogin;

    @Column
    private Long programApplicationId;

    @Column
    private Long programId;

    @Column
    private Date programUpdateDate;

    @Column
    private Long requestId;

    @Column
    private String attributeCategory;


    @Transient
    private List<RefundOrderEntry> refundOrderEntries;

    public List<RefundOrderEntry> getRefundOrderEntries() {
        return refundOrderEntries;
    }

    public void setRefundOrderEntries(List<RefundOrderEntry> refundOrderEntries) {
        this.refundOrderEntries = refundOrderEntries;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

}
