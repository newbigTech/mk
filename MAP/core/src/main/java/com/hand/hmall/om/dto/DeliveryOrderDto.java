package com.hand.hmall.om.dto;

import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Table;
import java.util.Date;

/**
 * author: zhangzilong
 * name: DeliveryOrderDto.java
 * discription: 交货单实体类
 * date: 2017/11/9
 * version: 0.1
 */
@Table(name = "HMALL_OM_DELIVERY_ORDER")
public class DeliveryOrderDto extends BaseDTO{
    
    private Integer deliveryOrderId;
    
    private String deliveryNote;

    private String logisticsCompany;
    
    private String logisticsNumber;
    
    private Integer consignmentId;

    private Date shippingDate;

    public Integer getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Integer deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
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

    public Integer getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Integer consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }
}
