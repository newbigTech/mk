package com.hand.hmall.client.impl;

import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.Coupon;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2017/1/7.
 */

public class PromoteClientServiceImpl implements IPromoteClientService {

    @RequestMapping(value = "/h/promotion/coupon/insert", method = RequestMethod.POST)
    public ResponseData add(@RequestBody Coupon coupon){
        return null;
    }

    @Override
    public ResponseData queryByUserIdAndStatus(@RequestBody Map<String,Object> map){
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("获取优惠券信息失败");
        return responseData;
    }

    @Override
    public ResponseData queryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData hQueryByUserIdAndStatus(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData hQueryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map) {
        return null;
    }

    @RequestMapping(value = "/h/promotion/coupon/getByCouponId", method = RequestMethod.GET)
    public ResponseData getByCouponId(@RequestParam("couponId") String couponId) {
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("获取优惠券信息失败");
        return responseData;
    }

    @Override
    public ResponseData setUsed(Map map) {
        return null;
    }

    @Override
    public ResponseData getCusomerCouponByCouponId(String couponId) {
        return null;
    }


    @Override
    public ResponseData submitRedemption(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData deleteRedemption(@RequestParam("redemptionId") String redemptionId, @RequestParam("type") String type) {
        return null;

    }

    @Override
    public ResponseData setInvalidByCid(@RequestParam("cid") String cid) {
        return null;
    }

    @Override
    public ResponseData startUsingCoupon(@RequestParam("cid") String cid) {
        return null;
    }

    public ResponseData convertByDraw(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);

    }

    @Override
    public ResponseData minusCouponCount(@RequestBody Map convertMap){
        return null;
    }

    @Override
    public ResponseData usedByTempId(@RequestBody List<Map<String, Object>> couponTempIds) {
        return null;
    }

    @Override
    public ResponseData checkedCouponCount(@RequestBody List<Map<String, Object>> mapList) {
        return null;
    }

    @Override
    public ResponseData selectCouponById(String conponId, String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseData deleteByCid(@RequestParam("id") String id) {
        return null;
    }

    @Override
    public ResponseData queryByCidAndUserIds(Map<String, Object> map) {
        return null;
    }
}
