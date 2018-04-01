package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.Map;

/**
 * Created by shanks on 2017/2/22.
 */
public interface IRedemptionService {

    ResponseData submitRedemption(Map<String, Object> map);
    void deleteReal(String id, String type);
    Map<String,?> selectByIdAndType(String id, String type);
    boolean deleteRedemptionCount(String id, String type, Integer countNumber);
    ResponseData checkedCouponCount(String couponId, String name, int count);

}
