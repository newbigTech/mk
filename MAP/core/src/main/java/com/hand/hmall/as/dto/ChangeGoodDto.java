package com.hand.hmall.as.dto;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 换货单接收对象
 * @date 2017/9/18 20:32
 */
public class ChangeGoodDto {

    private Long orderId;

    private String receiverName;

    private String receiverMobile;

    private String receiverPhone;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverZip;

    private String receiverAddress;

    private Date customerDemandTime;

    private String responsibleParty;

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    private List<ServiceorderEntry> serviceorderEntryList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Date getCustomerDemandTime() {
        return customerDemandTime;
    }

    public void setCustomerDemandTime(Date customerDemandTime) {
        this.customerDemandTime = customerDemandTime;
    }

    public List<ServiceorderEntry> getServiceorderEntryList() {
        return serviceorderEntryList;
    }

    public void setServiceorderEntryList(List<ServiceorderEntry> serviceorderEntryList) {
        this.serviceorderEntryList = serviceorderEntryList;
    }
}
