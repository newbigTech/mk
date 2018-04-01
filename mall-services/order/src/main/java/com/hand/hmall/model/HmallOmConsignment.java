/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.model;


import javax.persistence.*;
import java.util.Date;

/**
 * @author 李伟
 * @name:HmallOmConsignment
 * @Description:HMALL_OM_CONSIGNMENT 对应的实体类
 * @version 1.0
 * @date 2017/8/21 14:03
 */
@Entity
@Table(name = "HMALL_OM_CONSIGNMENT")
public class HmallOmConsignment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "HMALL_OM_CONSIGNMENT_S.nextval from dual")
    private Long consignmentId;

    private String code;

    private String brand;

    private Long orderId;

    private Long logisticsCompanies;

    private String logisticsNumber;

    private Long pointOfServiceId;

    private String shippingType;

    private Date shippingDate;

    private String receiverName;

    private String receiverCountry;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverTown;

    private String receiverAddress;

    private String receiverZip;

    private String receiverMobile;

    private String receiverPhone;

    private String note;

    private Date estimateDeliveryTime;

    private String splitAllowed;

    private String syncflag;

    private Double objectVersionNumber;

    private Date creationDate;

    private Long createdBy;

    private Long lastUpdatedBy;

    private Date lastUpdateDate;

    private Long lastUpdateLogin;

    private Long programApplicationId;

    private Long programId;

    private Date programUpdateDate;

    private Long requestId;

    private String attributeCategory;

    private Date tradeFinishedTime;



    public Long getConsignmentId(){
         return consignmentId;
     }

    public void setConsignmentId(Long consignmentId){
         this.consignmentId = consignmentId;
     }

    public String getCode(){
         return code;
     }

    public void setCode(String code){
         this.code = code;
     }

    public String getBrand(){
         return brand;
     }

    public void setBrand(String brand){
         this.brand = brand;
     }

    public Long getOrderId(){
         return orderId;
     }

    public void setOrderId(Long orderId){
         this.orderId = orderId;
     }

    public Long getLogisticsCompanies(){
         return logisticsCompanies;
     }

    public void setLogisticsCompanies(Long logisticsCompanies){
         this.logisticsCompanies = logisticsCompanies;
     }

    public String getLogisticsNumber(){
         return logisticsNumber;
     }

    public void setLogisticsNumber(String logisticsNumber){
         this.logisticsNumber = logisticsNumber;
     }

    public Long getPointOfServiceId(){
         return pointOfServiceId;
     }

    public void setPointOfServiceId(Long pointOfServiceId){
         this.pointOfServiceId = pointOfServiceId;
     }

    public String getShippingType(){
         return shippingType;
     }

    public void setShippingType(String shippingType){
         this.shippingType = shippingType;
     }

    public Date getShippingDate(){
         return shippingDate;
     }

    public void setShippingDate(Date shippingDate){
         this.shippingDate = shippingDate;
     }

    public String getReceiverName(){
         return receiverName;
     }

    public void setReceiverName(String receiverName){
         this.receiverName = receiverName;
     }

    public String getReceiverCountry(){
         return receiverCountry;
     }

    public void setReceiverCountry(String receiverCountry){
         this.receiverCountry = receiverCountry;
     }

    public String getReceiverState(){
         return receiverState;
     }

    public void setReceiverState(String receiverState){
         this.receiverState = receiverState;
     }

    public String getReceiverCity(){
         return receiverCity;
     }

    public void setReceiverCity(String receiverCity){
         this.receiverCity = receiverCity;
     }

    public String getReceiverDistrict(){
         return receiverDistrict;
     }

    public void setReceiverDistrict(String receiverDistrict){
         this.receiverDistrict = receiverDistrict;
     }

    public String getReceiverTown(){
         return receiverTown;
     }

    public void setReceiverTown(String receiverTown){
         this.receiverTown = receiverTown;
     }

    public String getReceiverAddress(){
         return receiverAddress;
     }

    public void setReceiverAddress(String receiverAddress){
         this.receiverAddress = receiverAddress;
     }

    public String getReceiverZip(){
         return receiverZip;
     }

    public void setReceiverZip(String receiverZip){
         this.receiverZip = receiverZip;
     }

    public String getReceiverMobile(){
         return receiverMobile;
     }

    public void setReceiverMobile(String receiverMobile){
         this.receiverMobile = receiverMobile;
     }

    public String getReceiverPhone(){
         return receiverPhone;
     }

    public void setReceiverPhone(String receiverPhone){
         this.receiverPhone = receiverPhone;
     }

    public String getNote(){
         return note;
     }

    public void setNote(String note){
         this.note = note;
     }

    public Date getEstimateDeliveryTime(){
         return estimateDeliveryTime;
     }

    public void setEstimateDeliveryTime(Date estimateDeliveryTime){
         this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getSplitAllowed(){
         return splitAllowed;
     }

    public void setSplitAllowed(String splitAllowed){
         this.splitAllowed = splitAllowed;
     }

    public String getSyncflag(){
         return syncflag;
     }

    public void setSyncflag(String syncflag){
         this.syncflag = syncflag;
     }

    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Double objectVersionNumber) {
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

    public Date getTradeFinishedTime() {
        return tradeFinishedTime;
    }

    public void setTradeFinishedTime(Date tradeFinishedTime) {
        this.tradeFinishedTime = tradeFinishedTime;
    }
}
