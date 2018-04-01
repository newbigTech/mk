package com.hand.hmall.client;

import com.hand.hmall.client.impl.AfterSaleClientServiceImpl;
import com.hand.hmall.client.impl.PromoteClientServiceImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 调用aftersale 服务
 */
@FeignClient(value = "hmall-aftersale-service", fallback = AfterSaleClientServiceImpl.class)
public interface IAfterSaleClientService {
    @RequestMapping(value = "/serviceOrder/queryByOrderId", method = RequestMethod.POST)
    public ResponseData queryByOrderId(@RequestParam("orderId") Long orderId);
}
