package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleCouponClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleCouponClientImpl.class)
public interface ISaleCouponClient {
    @RequestMapping(value = "/h/sale/coupon/query" , method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/coupon/queryActivity" , method = RequestMethod.POST)
    public ResponseData queryActivity(@RequestBody Map<String, Object> datas);

    @RequestMapping(value = "/h/sale/coupon/queryByNotIn" , method = RequestMethod.POST)
    public ResponseData queryByNotIn(@RequestBody Map<String, Object> datas);

    @RequestMapping(value = "/h/sale/coupon/submit" , method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/coupon/startUsing" , method = RequestMethod.POST)
    public ResponseData startUsing(@RequestBody List<Map<String, Object>> datas);

    @RequestMapping(value = "/h/sale/coupon/endUsing" , method = RequestMethod.POST)
    public ResponseData endUsing(@RequestBody List<Map<String, Object>> datas);

    @RequestMapping(value = "/h/sale/coupon/detail" , method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id);

    @RequestMapping(value = "/h/sale/coupon/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps);

}
