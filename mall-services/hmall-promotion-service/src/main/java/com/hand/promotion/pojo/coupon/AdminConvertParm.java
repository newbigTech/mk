package com.hand.promotion.pojo.coupon;

import java.util.List;
import java.util.Map;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description 管理员发放优惠券入参
 */
public class AdminConvertParm {

    /**
     * 要发放的优惠券主键
     */
    private String couponId;

    /**
     * 券发放类型
     */
    private String type;

    /**
     * 要发放的账号,发券数量集合
     */
    private List<Map<String, Object>> convertData;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, Object>> getConvertData() {
        return convertData;
    }

    public void setConvertData(List<Map<String, Object>> convertData) {
        this.convertData = convertData;
    }

    @Override
    public String toString() {
        return "{\"AdminConvertParm\":{"
            + "                        \"couponId\":\"" + couponId + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"convertData\":" + convertData
            + "}}";
    }
}
