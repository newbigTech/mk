package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.Map;

/**
 * Created by shanks on 2017/2/7.
 */
public interface ICustomerCouponService {
    ResponseData queryUserIdAndStatus(Map map);
    ResponseData queryUserIdAndStatusForPromote(Map map);
    ResponseData queryUserIdAndStatus(String userId, String status);
    ResponseData queryUserIdAndStatusAndRange(String userId, String status, String range);
    ResponseData getByCouponId(String couponId);
    Map<String,Object> submitCustomerRedemption(Map<String, Object> map);
    boolean redemptionCount(String id, String userId, Integer countNumber);
    Map<String,?> queryByUserIdAndCId(String userId, String couponId);
    void setInvalidByCid(String cid);
    void startUsingCoupon(String cid);
    ResponseData queryByCidAndUserIds(Map<String, Object> map);
    ResponseData deleteByCid(String cid);
}
