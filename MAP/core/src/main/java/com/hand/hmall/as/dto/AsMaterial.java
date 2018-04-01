package com.hand.hmall.as.dto;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsMaterial
 * @description 物耗单DTO
 * @date 2017/12/4
 */

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_MATERIAL")
public class AsMaterial extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long id;

    @Column
    @ExcelVOAttribute(name = "物耗单单号", column = "A", isExport = true)
    private String code;//物耗单单号

    @Column
    private Long orderId;//原销售订单号

    @Column
    private Long serviceOrderId;//服务单号

    @Column
    private Long consignmentId;//原配货单号

    @Column
    private String status;//订单状态

    @Column
    @ExcelVOAttribute(name = "客户ID", column = "E", isExport = true)
    private Long userId;//会员账号

    @Column
    private String name;//客户姓名

    @Column
    private String mobile;//客户联系方式

    @Column
    @ExcelVOAttribute(name = "客户地址", column = "F", isExport = true)
    private String address;//客户地址

    @Column
    @ExcelVOAttribute(name = "责任方", column = "B", isExport = true)
    private String responsibleParty;//责任方

    @Column
    @ExcelVOAttribute(name = "是否收费", column = "D", isExport = true)
    private String isCharge;//是否收费

    @Column
    @ExcelVOAttribute(name = "订单原因", column = "C", isExport = true)
    private String orderReason;//订单原因

    @Column
    @ExcelVOAttribute(name = "受理客服", column = "K", isExport = true)
    private String cs;//受理客服

    @Column
    @ExcelVOAttribute(name = "完结时间", column = "M", isExport = true)
    private Date finishTime;//完结时间

    @Column
    private String note;//备注

    @Column
    private String syncRetail;//同步retail标记

    @Column
    @ExcelVOAttribute(name = "创建时间", column = "L", isExport = true)
    private Date creationDate;//创建时间

    @Column
    private Long createdBy;

    @Column
    @ExcelVOAttribute(name = "SAP系统单号", column = "J", isExport = true)
    private String sapCode;//retail单号
    @Column
    private Date appointmentDate;//预计到货时间
    @Column
    private String logisticsNumber;//快递单号
    @Column
    private String logisticsCompany;//快递公司
    @Transient
    private String customerid;
    @Transient
    private String soldParty;//同步retail使用，售达方
    @Transient
    private String salesOffice;//同步retail使用，销售办公室
    @Transient
    private String receiverDistrict;//同步retail使用，运输区域
    @Transient
    private String sex;//同步retail使用，性别
    @Transient
    private String userLevel;//同步retail使用，会员级别
    @Transient
    private List<AsMaterialEntry> asMaterialEntries;

    @Transient
    @ExcelVOAttribute(name = "备注", column = "S", isExport = true)
    private String materialEntryNote;

    @Transient
    @ExcelVOAttribute(name = "手机号", column = "G", isExport = true)
    private String mobileNumber;

    @Transient
    @ExcelVOAttribute(name = "平台订单号", column = "H", isExport = true)
    private String escOrderCode;

    @Transient
    @ExcelVOAttribute(name = "商品编码", column = "N", isExport = true)
    private String productCode;

    @Transient
    @ExcelVOAttribute(name = "数量", column = "P", isExport = true)
    private Long quantity;

    @Transient
    @ExcelVOAttribute(name = "销售价格", column = "O", isExport = true)
    private Double unitFee;

    @Transient
    @ExcelVOAttribute(name = "服务单单号", column = "I", isExport = true)
    private String serviceOrderCode;

    @Transient
    private String strMaterialStatus;

    @Transient
    private String orderCode;

    @Transient
    private Long asRefundId;

    @Transient
    private String userGroup;

    @Transient
    private String creationDateStart;

    @Transient
    private String creationDateEnd;

    @Transient
    private String finishTimeStart;

    @Transient
    private String finishTimeEnd;

    @Transient
    @ExcelVOAttribute(name = "补件原因", column = "Q", isExport = true)
    private String patchReason;

    @Transient
    @ExcelVOAttribute(name = "提货地点", column = "R", isExport = true)
    private String pointofservice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
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

    public String getSyncRetail() {
        return syncRetail;
    }

    public void setSyncRetail(String syncRetail) {
        this.syncRetail = syncRetail;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public List<AsMaterialEntry> getAsMaterialEntries() {
        return asMaterialEntries;
    }

    public void setAsMaterialEntries(List<AsMaterialEntry> asMaterialEntries) {
        this.asMaterialEntries = asMaterialEntries;
    }

    public String getMaterialEntryNote() {
        return materialEntryNote;
    }

    public void setMaterialEntryNote(String materialEntryNote) {
        this.materialEntryNote = materialEntryNote;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public String getServiceOrderCode() {
        return serviceOrderCode;
    }

    public void setServiceOrderCode(String serviceOrderCode) {
        this.serviceOrderCode = serviceOrderCode;
    }

    public String getStrMaterialStatus() {
        return strMaterialStatus;
    }

    public void setStrMaterialStatus(String strMaterialStatus) {
        this.strMaterialStatus = strMaterialStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getAsRefundId() {
        return asRefundId;
    }

    public void setAsRefundId(Long asRefundId) {
        this.asRefundId = asRefundId;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
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

    public String getFinishTimeStart() {
        return finishTimeStart;
    }

    public void setFinishTimeStart(String finishTimeStart) {
        this.finishTimeStart = finishTimeStart;
    }

    public String getFinishTimeEnd() {
        return finishTimeEnd;
    }

    public void setFinishTimeEnd(String finishTimeEnd) {
        this.finishTimeEnd = finishTimeEnd;
    }

    public String getPatchReason() {
        return patchReason;
    }

    public void setPatchReason(String patchReason) {
        this.patchReason = patchReason;
    }

    public String getPointofservice() {
        return pointofservice;
    }

    public void setPointofservice(String pointofservice) {
        this.pointofservice = pointofservice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
