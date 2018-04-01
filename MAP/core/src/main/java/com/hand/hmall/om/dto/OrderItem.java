package com.hand.hmall.om.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * author: zhangzilong
 * name: OrderItem.java
 * discription: 发货单推送日日顺，发货单行实体类
 * date: 2017/11/23
 * version: 0.1
 */
public class OrderItem implements Serializable {

    @JsonIgnore
    private Long deliveryEntryId;

    private String item_id;

    private String item_name;

    private String item_code;

    private Long item_quantity;

    private Long item_pack;

    private Double item_price;

    //计算过程在item_volume_str的set方法中，计算方法为：
    //item_volume_str用*隔开获得长宽高，乘积除以10^9
    private Double item_volume;

    @JsonIgnore
    private String item_volume_str;

    private Double item_weight;

    public Long getDeliveryEntryId() {
        return deliveryEntryId;
    }

    public void setDeliveryEntryId(Long deliveryEntryId) {
        this.deliveryEntryId = deliveryEntryId;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public Long getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(Long item_quantity) {
        this.item_quantity = item_quantity;
    }

    public Long getItem_pack() {
        return item_pack;
    }

    public void setItem_pack(Long item_pack) {
        this.item_pack = item_pack;
    }

    public Double getItem_price() {
        return item_price;
    }

    public void setItem_price(Double item_price) {
        this.item_price = item_price;
    }

    public Double getItem_volume() {
        return item_volume;
    }

    public void setItem_volume(Double item_volume) {
        this.item_volume = item_volume;
    }

    public Double getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(Double item_weight) {
        this.item_weight = item_weight;
    }

    public String getItem_volume_str() {
        return item_volume_str;
    }

    public void setItem_volume_str(String item_volume_str) {
        this.item_volume_str = item_volume_str.trim();
        if (this.item_volume_str.indexOf("*") > 0) {
            String[] volumeStrArr = this.item_volume_str.split("[*]", -1);
            if (volumeStrArr.length == 3) {
                this.item_volume = new BigDecimal(volumeStrArr[0]).multiply(new BigDecimal(volumeStrArr[1])).multiply(new BigDecimal(volumeStrArr[2])).divide(new BigDecimal("1000000000"))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
        }
        if (this.item_volume_str.indexOf("X") > 0) {
            String[] volumeStrArr = this.item_volume_str.split("X", -1);
            if (volumeStrArr.length == 3) {
                this.item_volume = new BigDecimal(volumeStrArr[0]).multiply(new BigDecimal(volumeStrArr[1])).multiply(new BigDecimal(volumeStrArr[2])).divide(new BigDecimal("1000000000"))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
        }
    }
}
