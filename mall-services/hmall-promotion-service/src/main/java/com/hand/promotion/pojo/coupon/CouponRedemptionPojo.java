package com.hand.promotion.pojo.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券剩余兑换次数
 */
@Document(collection = "CouponRedemption")
public class CouponRedemptionPojo {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 关联的优惠券主键
     */
    private String redemptionId;
    private String type;

    /**
     * 剩余兑换次数
     */
    private Integer number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(String redemptionId) {
        this.redemptionId = redemptionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "{\"CouponRedemptionPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"redemptionId\":\"" + redemptionId + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"number\":\"" + number + "\""
            + "}}";
    }
}
