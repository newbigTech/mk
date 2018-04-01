package com.hand.hmall.client;

import com.hand.hmall.client.impl.RuleClientHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hand on 2017/2/11.
 */
@FeignClient(value = "hmall-drools-service", fallback = RuleClientHystrix.class)
public interface IRuleClient {

    @RequestMapping(value = "/h/sale/coupon/queryByCode/{couponCode}" , method = RequestMethod.GET)
    public ResponseData queryByCode(@PathVariable("couponCode") String couponCode);

    @RequestMapping(value = "/h/sale/coupon/queryByCodeCanUse/{couponCode}" , method = RequestMethod.GET)
    public ResponseData queryByCodeCanUse(@PathVariable("couponCode") String couponCode);

    @RequestMapping(value = "/h/sale/coupon/selectById" , method = RequestMethod.GET)
    public ResponseData selectById(@RequestParam("id") String id);

    @RequestMapping(value = "/h/sale/coupon/selectByCouponId" , method = RequestMethod.GET)
    public ResponseData selectByCouponId(@RequestParam("couponId") String couponId);

    @RequestMapping(value = "/h/sale/coupon/selectCouponIdById" , method = RequestMethod.GET)
    public ResponseData selectCouponIdById(@RequestParam("id") String id);

}
