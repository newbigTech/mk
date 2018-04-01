package com.hand.hmall.om.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.List;

/**
 * author: zhangzilong
 * name: ConsignmentToRRS
 * discription: 发送至日日顺的发货单实体类
 * date: 2017/8/10
 * version: 0.1
 */
public class ConsignmentToRRS {

    @JsonIgnore
    private Long deliveryOrderId;

    private String order_code;

    private String tms_service_code;

    private String tms_order_code;

    private String service_type;

    private Double total_amount;

    private Double service_fee;

    private String receiver_name;

    private String receiver_zip;

    private String receiver_province;

    private String receiver_city;

    private String receiver_district;

    private String receiver_address;

    private String receiver_mobile;

    private String receiver_phone;

    private String sender_name;

    private String sender_zip;

    private String sender_province;

    private String sender_city;

    private String sender_district;

    private String sender_address;

    private String sender_mobile;

    private String sender_phone;

    private String order_source;

    private String order_source_code;

    private String remark;

    private String join_order_code;

    private Integer join_order_count;

    private List<OrderItem> order_item_list;

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTms_service_code() {
        return tms_service_code;
    }

    public void setTms_service_code(String tms_service_code) {
        this.tms_service_code = tms_service_code;
    }

    public String getTms_order_code() {
        return tms_order_code;
    }

    public void setTms_order_code(String tms_order_code) {
        this.tms_order_code = tms_order_code;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public Double getService_fee() {
        return service_fee;
    }

    public void setService_fee(Double service_fee) {
        this.service_fee = service_fee;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_zip() {
        return receiver_zip;
    }

    public void setReceiver_zip(String receiver_zip) {
        this.receiver_zip = receiver_zip;
    }

    public String getReceiver_province() {
        return receiver_province;
    }

    public void setReceiver_province(String receiver_province) {
        this.receiver_province = receiver_province;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_district() {
        return receiver_district;
    }

    public void setReceiver_district(String receiver_district) {
        this.receiver_district = receiver_district;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_zip() {
        return sender_zip;
    }

    public void setSender_zip(String sender_zip) {
        this.sender_zip = sender_zip;
    }

    public String getSender_province() {
        return sender_province;
    }

    public void setSender_province(String sender_province) {
        this.sender_province = sender_province;
    }

    public String getSender_city() {
        return sender_city;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public String getSender_district() {
        return sender_district;
    }

    public void setSender_district(String sender_district) {
        this.sender_district = sender_district;
    }

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getSender_phone() {
        return sender_phone;
    }

    public void setSender_phone(String sender_phone) {
        this.sender_phone = sender_phone;
    }

    public String getOrder_source() {
        return order_source;
    }

    public void setOrder_source(String order_source) {
        this.order_source = order_source;
    }

    public String getOrder_source_code() {
        return order_source_code;
    }

    public void setOrder_source_code(String order_source_code) {
        this.order_source_code = order_source_code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJoin_order_code() {
        return join_order_code;
    }

    public void setJoin_order_code(String join_order_code) {
        this.join_order_code = join_order_code;
    }

    public Integer getJoin_order_count() {
        return join_order_count;
    }

    public void setJoin_order_count(Integer join_order_count) {
        this.join_order_count = join_order_count;
    }

    public List<OrderItem> getOrder_item_list() {
        return order_item_list;
    }

    public void setOrder_item_list(List<OrderItem> order_item_list) {
        this.order_item_list = order_item_list;
    }
}
