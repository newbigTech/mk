package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @name ServiceOrder
 * @Describe 服务单实体类
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Entity
@Table(name = "HMALL_AS_SERVICEORDER")
public class ServiceOrder {

    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_AS_SERVICEORDER_S.nextval from dual")
//    @Column
    private Long serviceOrderId;

    // 商城传商城订单号escOrderCode，接口查询订单表中（hmall_om_order)字段escOrderCode值与其相等，
    // 且字段website_id在网站表（HMALL_MST_WEBSITE）中对应的字段code=1的销售订单ID，存到该字段中
    @Transient
    private String escOrderCode;

    // 服务单号
    @Column
    private String code;

    // 单据类型（区分各种售后单据）
    @Column
    private String orderType;

    // 订单ID
    @Column
    private Long orderId;

    // 单据状态
    @Column
    private String status;

    // 服务类别1
    @Column
    private String svCategory1;

    // 服务类别2
    @Column
    private String svCategory2;

    // 投诉内容
    @Column
    private String complaint;

    // 备注
    @Column
    private String note;

    // 用户
    @Column
    private String userId;

    @Transient
    private String customerid;

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

    // 客服预约执行日期
    @Column
    private Date appointmentDate;

    // 实际执行日期
    @Column
    private Date executionDate;

    // 版本号
    @Column
    private Long objectVersionNumber;

    // 用户名称
    @Column
    private String name;

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


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    @Transient
    List<ServiceOrderEntry> serviceOrderEntries;

    @Transient
    List<RefundOrder> refundOrders;

    @Transient
    List<ReturnOrder> returnOrders;

    @Transient
    List<SvsalesOrder> svsalesOrders;

    @Transient
    List<Map> mediaLinks;

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public List<RefundOrder> getRefundOrders() {
        return refundOrders;
    }

    public void setRefundOrders(List<RefundOrder> refundOrders) {
        this.refundOrders = refundOrders;
    }

    public List<ReturnOrder> getReturnOrders() {
        return returnOrders;
    }

    public void setReturnOrders(List<ReturnOrder> returnOrders) {
        this.returnOrders = returnOrders;
    }

    public List<SvsalesOrder> getSvsalesOrders() {
        return svsalesOrders;
    }

    public void setSvsalesOrders(List<SvsalesOrder> svsalesOrders) {
        this.svsalesOrders = svsalesOrders;
    }

    public List<ServiceOrderEntry> getServiceOrderEntries() {
        return serviceOrderEntries;
    }

    public void setServiceOrderEntries(List<ServiceOrderEntry> serviceOrderEntries) {
        this.serviceOrderEntries = serviceOrderEntries;
    }

    public List<Map> getMediaLinks() {
        return mediaLinks;
    }

    public void setMediaLinks(List<Map> mediaLinks) {
        this.mediaLinks = mediaLinks;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
