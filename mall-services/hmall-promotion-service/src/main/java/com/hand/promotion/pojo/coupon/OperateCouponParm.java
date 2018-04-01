package com.hand.promotion.pojo.coupon;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/17
 * @description 占用释放优惠券入参
 */
public class OperateCouponParm implements Serializable{


    /**
     * 要操作的优惠券主键
     */
    @NotNull
    private String couponId;

    /**
     * 操作,占用,释放.1为占用2为释放
     */
    @NotNull
    private String operation;

    /**
     * 券所有人
     */
    @NotNull
    private String customerId;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
