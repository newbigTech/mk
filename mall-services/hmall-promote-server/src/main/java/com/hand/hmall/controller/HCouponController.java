package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ICouponService;
import com.hand.hmall.service.ICustomerCouponService;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2017/1/10.
 * 用于MAP调用查询优惠券相关数据
 */
@RestController
@RequestMapping(value = "/h/promotion/coupon")
public class HCouponController {
    @Autowired
    private ICouponService couponService;
    @Autowired
    private ICustomerCouponService customerCouponService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 停用优惠券
     * 根据优惠券（coupon）的主键设置该优惠券已发放的优惠券（customerCoupon）状态为已失效
     *
     * @param cid
     * @return
     */
    @GetMapping(value = "/setInvalidByCid")
    public ResponseData setInvalidByCid(@RequestParam("cid") String cid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerCouponService.setInvalidByCid(cid);
            }
        }).start();
        return new ResponseData();
    }

    /**
     * 启用优惠券
     * 根据优惠券（coupon）的主键设置该优惠券已发放的优惠券（customerCoupon）状态为可以使用
     * @param cid
     * @return
     */
    @GetMapping(value = "/startUsingCoupon")
    public ResponseData startUsingCoupon(@RequestParam("cid") String cid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerCouponService.startUsingCoupon(cid);
            }
        }).start();
        return new ResponseData();
    }

    /**
     * 根据用户已领取的优惠券（customerCoupon）的主键查询已领取优惠券详细信息
     * @param couponId
     * @return
     */
    @GetMapping(value = "/getByCouponId")
    public ResponseData getByCouponId(@RequestParam("couponId") String couponId) {
        return customerCouponService.getByCouponId(couponId);
    }

    /**
     * HAP 根据userId查询该用户下的优惠券
     *
     * @param userId
     * @return
     */
    @GetMapping("/listUserId/{userId}/{page}/{pagesize}")
    public ResponseData queryByUserId(@PathVariable("userId") String userId, @PathVariable("page") int page, @PathVariable("pagesize") int pagesize) {
        return couponService.queryByUserId(userId, page, pagesize);
    }

    @PostMapping("/convert/admin")
    public ResponseData convertByAdmin(@RequestBody Map<String, Object> convertMap) {
        List<Map<String, Object>> convertData = (List<Map<String, Object>>) convertMap.get("convertData");

        String couponId = convertMap.get("couponId").toString();
        String type = convertMap.get("type").toString();
        if (type != null && !type.trim().equals("") && type.trim().equals("COUPON_TYPE_04")) {
            return couponService.convertCouponByAdmin(convertData, couponId);
        } else {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("只有【管理员发放】类型的优惠券允许发放！");
            return responseData;
        }
    }


    /**
     * 根据用户id和状态查询优惠券
     *
     * @param map
     * @return
     */
    @PostMapping("/hQueryByUserIdAndStatus")
    public ResponseData hQueryByUserIdAndStatus(@RequestBody Map<String, Object> map) {
        return customerCouponService.queryUserIdAndStatus(map.get("userId").toString(), map.get("status").toString());
    }

    /**
     * 根据用户id 优惠券状态 优惠券使用渠道查询优惠券
     *
     * @param map
     * @return
     */
    @PostMapping("/hQueryByUserIdAndStatusAndRange")
    public ResponseData queryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map) {
        return customerCouponService.queryUserIdAndStatusAndRange(map.get("userId").toString(), map.get("status").toString(), map.get("range").toString());
    }

    /**
     * 根据优惠券（coupon）id和用户id查询优惠券
     *
     * @param map
     * @return
     */
    @PostMapping("/queryByCidAndUserIds")
    public ResponseData queryByCidAndUserIds(@RequestBody Map<String, Object> map) {
        logger.info("--------------------queryByCidAndUserIds----------------------\n{}", JSON.toJSONString(map));
        ResponseData responseData = customerCouponService.queryByCidAndUserIds(map);
        logger.info("--------------------resp----------------------\n{}", JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     * 根据优惠券（coupon）的主键删除该优惠券已发放的优惠券（customerCoupon）
     * @param id
     * @return
     */
    @GetMapping("/deleteByCid")
    public ResponseData deleteByCid(@RequestParam("id") String id) {
        return customerCouponService.deleteByCid(id);
    }
}
