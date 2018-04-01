package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2016/12/6.
 */
public interface ICouponService {

    /**
     *用户获取优惠券
     *
     * @param map 参数信息，couponid ，customerId,paymentInfo
     * @param
     * @return
     */
    ResponseData convertCoupon(Map map);

    /**
     * 管理员分配优惠券
     * @param convertData
     * @param couponId
     * @return
     */
    ResponseData convertCouponByAdmin(List<Map<String, Object>> convertData, String couponId);

    /**
     *用户使用优惠券
     *
     * @param couponId 优惠券id
     * @return
     */
    ResponseData setUsed(String couponId,String orderId,String operate,String custometId);


    /**
     *显示优惠券列表
     *
     * @param userId
     * @return
     */
    ResponseData getAll(String userId, String status);

    /**
     *显示优惠券详情
     *
     * @param id
     * @return
     */
    ResponseData select(String id);

    /**
     *删除优惠券列表
     *
     * @param couponId
     * @return
     */
    ResponseData delete(String couponId);

    /**
     * HAP 查询该用户下的优惠券
     */
    ResponseData queryByUserId(String userId, int page, int pagesize);

    List<Map<String,?>> queryUserIdAndStatus(String userId, String status);

    /**
	  * 根据  couponId  userId 查出 优惠券
	  */
    ResponseData selectCouponById(String couponId, String userId);

    /**
     * 根据优惠券码生成响应的优惠券兑换码
     *
     * @param couponCode 优惠券编码
     * @param redeemNum 兑换量
     * @return
     */
    ResponseData generateRedeemCode(String couponCode,int redeemNum);


    /**
     * 获取优惠券码下的所有可用兑换码
     *
     * @param couponCode
     * @return
     */
    ResponseData getRedeemCodes(String couponCode);



}
