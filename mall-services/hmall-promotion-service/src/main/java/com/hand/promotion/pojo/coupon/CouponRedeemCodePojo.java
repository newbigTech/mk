package com.hand.promotion.pojo.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 优惠券兑换码Pojo
 */
@Document(collection = "CouponRedeemCode")
public class CouponRedeemCodePojo {
    @Id
    private String id;
    /**
     * 兑换码
     */
    private String code;

    /**
     * 优惠券编码
     */
    private String couponCode;

    /**
     * 是否兑换，Y为已兑换，N为未兑换
     */
    private String used;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "{\"CouponRedeemCodePojo\":{"
            + "                        \"code\":\"" + code + "\""
            + ",                         \"couponCode\":\"" + couponCode + "\""
            + ",                         \"used\":\"" + used + "\""
            + "}}";
    }
}
