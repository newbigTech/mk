package com.hand.hmall.logistics.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Consignment
 * @description 发货单dto
 * @date 2017年6月5日13:54:47
 */
@Table(name = "HMALL_OM_CONSIGNMENT")
public class LgsConsignment {
    @Id
    @Column
    private Long consignmentId;

    @Column
    private String code;

    @Column
    private String brand;

    @Column
    private Long orderId;

    @Column
    private Long logisticsCompanies;

    @Column
    private String logisticsNumber;

    @Column
    private Long pointOfServiceId;

    @Column
    private String shippingType;

    @Column
    private Date shippingDate;

    @Column
    private String receiverName;

    @Column
    private String receiverCountry;

    @Column
    private Long receiverState;

    @Column
    private Long receiverCity;

    @Column
    private Long receiverDistrict;

    @Column
    private String receiverTown;

    @Column
    private String receiverAddress;

    @Column
    private String receiverZip;

    @Column
    private String receiverMobile;

    @Column
    private String receiverPhone;

    @Column
    private String note;

    @Column
    private Date estimateDeliveryTime;

    @Column
    private String splitAllowed;

    @Column
    private String syncflag;

    @Column
    private Long approvedBy;

    @Column
    private Date approvedDate;

    @Column
    private Integer approvedTimes;

    @Column
    private String splitReason;

    @Column
    private String abnormalReason;

    @Column
    private String csApproved;

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getLogisticsCompanies() {
        return logisticsCompanies;
    }

    public void setLogisticsCompanies(Long logisticsCompanies) {
        this.logisticsCompanies = logisticsCompanies;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public Long getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(Long receiverState) {
        this.receiverState = receiverState;
    }

    public Long getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(Long receiverCity) {
        this.receiverCity = receiverCity;
    }

    public Long getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(Long receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverTown() {
        return receiverTown;
    }

    public void setReceiverTown(String receiverTown) {
        this.receiverTown = receiverTown;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getSplitAllowed() {
        return splitAllowed;
    }

    public void setSplitAllowed(String splitAllowed) {
        this.splitAllowed = splitAllowed;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getApprovedTimes() {
        return approvedTimes;
    }

    public void setApprovedTimes(Integer approvedTimes) {
        this.approvedTimes = approvedTimes;
    }

    public String getSplitReason() {
        return splitReason;
    }

    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }

    public String getAbnormalReason() {
        return abnormalReason;
    }

    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason;
    }

    public String getCsApproved() {
        return csApproved;
    }

    public void setCsApproved(String csApproved) {
        this.csApproved = csApproved;
    }
}
