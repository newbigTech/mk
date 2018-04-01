package com.hand.promotion.pojo.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券用户剩余兑换数量
 */
@Document(collection = "CustomerRedeption")
public class CustomerRedeptionPojo {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户剩余兑换量
     */
    private Integer number;

    /**
     * 优惠券主键
     */
    private String cid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "{\"CustomerRedeptionPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"userId\":\"" + userId + "\""
            + ",                         \"number\":\"" + number + "\""
            + ",                         \"cid\":\"" + cid + "\""
            + "}}";
    }
}
