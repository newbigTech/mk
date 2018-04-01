package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleCouponClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/7.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleCouponClientImpl.class)
public interface ISaleCalculationClient {
    @RequestMapping(value = "/h/sale/calculation/optionCartByMap" , method = RequestMethod.POST)
    public ResponseData optionCartByMap(@RequestBody List<Map<String, Object>> carts);


    @RequestMapping(value = "sale/execution/promote" , method = RequestMethod.POST)
    public ResponseData promote(@RequestBody Map<String, Object> orderMap);
}
