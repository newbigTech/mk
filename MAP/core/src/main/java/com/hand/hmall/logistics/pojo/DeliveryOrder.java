package com.hand.hmall.logistics.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhangzilong
 * name: DeliveryOrder
 * discription: 交货单
 * date: 2017/11/7
 * version: 0.1
 */
@Table(name = "HMALL_OM_DELIVERY_ORDER")
@Entity
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_DELIVERY_ORDER_S.nextval from dual")
    private Long deliveryOrderId;

    @Column
    private String deliveryNote;

    @Column
    private Long consignmentId;

    @Column
    private String logisticsCompany;

    @Column
    private String logisticsNumber;

    @Column
    private Date shippingDate;

    @Column
    private String syncThirdLogistics;

    // 主交货单标识（Y-主交货单；N-非主交货单）
    @Column
    private String isMain;

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

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getSyncThirdLogistics() {
        return syncThirdLogistics;
    }

    public void setSyncThirdLogistics(String syncThirdLogistics) {
        this.syncThirdLogistics = syncThirdLogistics;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
