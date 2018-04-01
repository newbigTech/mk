package com.hand.hmall.logistics.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WmsConsignment
 * @description WMS物流信息持久层实体类
 * @date 2017/8/10
 */
public class WmsConsignment implements Serializable {

    // CONSIGNMENT_ID
    private Long consignmentId;

    // SAP_CODE
    private String spaCode;

    // STATUS
    private String status;

    // SHIPPING_DATE
    private Date shippingDate;

    // LOGISTICS_COMPANIES
    private Long logisticsCompanies;

    // LOGISTICS_NUMBER
    private String logisticsNumber;

    // ORDER_TYPE
    private String orderType;

    // ORDER_ID
    private Long orderId;

    // SYNCFLAG
    private String syncflag;

    //SYNC_ZMALL
    private String syncZmall;

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getSpaCode() {
        return spaCode;
    }

    public void setSpaCode(String spaCode) {
        this.spaCode = spaCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncZmall() {
        return syncZmall;
    }

    public void setSyncZmall(String syncZmall) {
        this.syncZmall = syncZmall;
    }
}
