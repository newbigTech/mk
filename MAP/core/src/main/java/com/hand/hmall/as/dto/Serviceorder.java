package com.hand.hmall.as.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name Serviceorder
 * @description 售后单据实体类
 * @date 2017/7/17
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_SERVICEORDER")
public class Serviceorder extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long serviceOrderId;

    @ExcelVOAttribute(name = "服务单编号", column = "A", isExport = true)
    private String code;


    private String orderType;


    @ExcelVOAttribute(name = "服务单状态", column = "B", isExport = true)
    private String status;


    @ExcelVOAttribute(name = "服务类别1", column = "F", isExport = true)
    private String svCategory1;


    @ExcelVOAttribute(name = "服务类别2", column = "G", isExport = true)
    private String svCategory2;


    private Long orderId;


    private String complaint;


    private String note;


    @ExcelVOAttribute(name = "用户账户", column = "C", isExport = true)
    private String userId;


    @ExcelVOAttribute(name = "用户手机号", column = "D", isExport = true)
    private String mobile;


    private String address;

    @ExcelVOAttribute(name = "跟单客服", column = "E", isExport = true)
    private String cs;

    // 雇员姓名
    @Transient
    private String employeeName;

    @ExcelVOAttribute(name = "完结时间", column = "I", isExport = true)
    private Date finishTime;


    private String syncflag;


    private Date appointmentDate;


    private Date executionDate;

    @ExcelVOAttribute(name = "创建时间", column = "H", isExport = true)
    private Date creationDate;

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

    @Transient
    private String customerid;

    //订单表中的CODE字段，用于服务单列表页面向服务单详情页面传递参数
    @Transient
    private String orderCode;

    //服务单列表页面的搜索条件订单编号，对应订单表中的ESC_ORDER_CODE字段
    @Transient
    private String escOrderCode;

    @Transient
    private List<ServiceorderEntry> serviceOrderEntries;

    /**
     * 图片描述
     */
    @Transient
    private String imageDescribe;

    /**
     * 图片url
     */
    @Transient
    private String url;

    @Transient
    private String websiteId;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public String getImageDescribe() {
        return imageDescribe;
    }

    public void setImageDescribe(String imageDescribe) {
        this.imageDescribe = imageDescribe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public List<ServiceorderEntry> getServiceOrderEntries() {
        return serviceOrderEntries;
    }

    public void setServiceOrderEntries(List<ServiceorderEntry> serviceOrderEntries) {
        this.serviceOrderEntries = serviceOrderEntries;
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

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
