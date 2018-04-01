package com.hand.hmall.client.impl;

import com.hand.hmall.client.IRuleClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hand on 2017/2/11.
 */
public class RuleClientHystrix implements IRuleClient {
    @Override
    public ResponseData queryByCode(@PathVariable("couponCode") String couponCode) {
        return null;
    }

    @Override
    public ResponseData queryByCodeCanUse(@PathVariable("couponCode") String couponCode) {
        return null;
    }

    @Override
    public ResponseData selectById(@RequestParam("id") String id) {
        return null;
    }

    @Override
    public ResponseData selectByCouponId(@RequestParam("couponId") String couponId) {
        return null;
    }

    @Override
    public ResponseData selectCouponIdById(@RequestParam("id") String id) {
        return null;
    }
}
