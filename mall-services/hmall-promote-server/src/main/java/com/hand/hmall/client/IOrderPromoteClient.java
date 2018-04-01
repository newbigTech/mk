package com.hand.hmall.client;

import com.hand.hmall.client.impl.OrderPromoteClientHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/4/24.
 */
@FeignClient(value = "order", fallback = OrderPromoteClientHystrix.class)
public interface IOrderPromoteClient {
    @RequestMapping(value = "o/order/query/queryTempIdByPOId/{id}", method = RequestMethod.GET)
    List<Object> queryTempIdByPOId(@PathVariable("id") String id);

    @RequestMapping(value = "/p/paymentInfo/insertPayment", method = RequestMethod.POST)
    ResponseData insertPayment(@RequestBody Map map);
}
