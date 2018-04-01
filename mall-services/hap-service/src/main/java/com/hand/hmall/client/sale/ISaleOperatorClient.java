package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleOperatorClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleOperatorClientImpl.class)
public interface ISaleOperatorClient {

    @RequestMapping(value = "/h/sale/operator/queryByBaseId" , method = RequestMethod.POST)
    public ResponseData queryByBaseId(@RequestBody Map<String, Object> map);
    @RequestMapping(value = "/coupon/operate/getSynCoupon" , method = RequestMethod.POST)
    public ResponseData getSynCoupon();

    @RequestMapping(value = "/coupon/operate/setSynCoupon" , method = RequestMethod.POST)
    public ResponseData setSynCoupon(@RequestBody List<Map> couponList);
}
