package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/1/5.
 */
public interface ISaleCouponService {
    ResponseData query(Map<String, Object> map) throws ParseException;

    ResponseData queryAll();
    ResponseData queryActivity(Map<String, Object> map) throws ParseException;
    ResponseData queryByNotIn(Map<String, Object> map) ;

    ResponseData submit(Map<String, Object> map);
    ResponseData delete(List<Map<String, Object>> maps);
    void deleteReal(List<Map<String, Object>> maps);
    Map<String,Object> startUsing(Map<String, Object> map);
    Map<String,Object> endUsing(Map<String, Object> map);
    Map<String,Object> selectCouponDetail(String id);

    ResponseData selectByCode(String couponCode);
    ResponseData selectByCodeCanUse(String couponCode);
    ResponseData selectById(String id);
    ResponseData selectCouponIdById(String id);
    ResponseData selectByCouponId(String couponId);

    /**
     * 校验用户选择的优惠券是否是排它券（isExclusive 为Y）
     *
     * @return
     */
    boolean checkExclusiveCoupon(String couponId);

    List<String> checkedCoupon(Map<String, Object> map);
}
