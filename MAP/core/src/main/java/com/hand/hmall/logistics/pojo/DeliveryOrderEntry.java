package com.hand.hmall.logistics.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chenzhigang
 * @version 0.1
 * @name DeliveryOrderEntry
 * @description 交货单行
 * @date 2017/11/15
 */
@Table(name = "HMALL_OM_DELIVERY_ENTRY")
@Entity
public class DeliveryOrderEntry {

    @Column
    private Long orderEntryId;

    @Column
    private Long deliveryOrderId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_DELIVERY_ENTRY_S.nextval from dual")
    private Long deliveryEntryId;

    @Column
    private Integer shippedQuantity;

    @Column
    private String deliveryNoteLine;

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

    public Long getDeliveryEntryId() {
        return deliveryEntryId;
    }

    public void setDeliveryEntryId(Long deliveryEntryId) {
        this.deliveryEntryId = deliveryEntryId;
    }

    public String getDeliveryNoteLine() {
        return deliveryNoteLine;
    }

    public void setDeliveryNoteLine(String deliveryNoteLine) {
        this.deliveryNoteLine = deliveryNoteLine;
    }

    public Integer getShippedQuantity() {
        return shippedQuantity;
    }

    public void setShippedQuantity(Integer shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    public Long getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
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
}
