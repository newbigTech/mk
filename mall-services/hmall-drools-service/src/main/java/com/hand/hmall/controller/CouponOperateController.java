package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dao.SaleCouponDao;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title CouponOperateController
 * @Description 优惠券推送接口调用controller
 * @date 2017/7/29 12:20
 */
@RestController
@RequestMapping("/coupon/operate")
public class CouponOperateController {
    @Resource
    private IPromoteClientService promoteClientService;
    @Autowired
    private SaleCouponDao saleCouponDao;

    /**
     * 优惠券占用释放接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/operateCustomerCoupon")
    public ResponseData operateCustomerCoupon(@RequestBody Map map) {
        return promoteClientService.setUsed(map);
    }

    /**
     * 获取需同步的coupon
     * @return
     */
    @RequestMapping("/getSynCoupon")
    public ResponseData getSynCoupon() throws ParseException {
        List<Map<String,?>> coupons = saleCouponDao.queryCouponBySyn();
        return new ResponseData(coupons);
    }

    /**
     * 设置优惠券同步标识
     *
     * @param couponList
     * @return
     */
    @RequestMapping("/setSynCoupon")
    public ResponseData setCouponSyn(@RequestBody List<Map> couponList){
        for (Map requestMap : couponList) {
            List coupons = saleCouponDao.selectByCodeAndReleaseId((String)requestMap.get("couponCode"),(String)requestMap.get("releaseId"));
            for (Object coupon : coupons) {
                Map couponMap = (Map)coupon;
                if(!"Y".equals(couponMap.get("isSyn"))){
                    couponMap.replace("isSyn","Y");
                    saleCouponDao.updateCoupon(couponMap);
                }
            }
        }
        return new ResponseData();
    }

}
