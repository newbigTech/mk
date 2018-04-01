package com.hand.promotion.client;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.impl.AfterSaleClientServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 调用 aftersale 服务
 *
 * @author xinyangMei
 */
@FeignClient(value = "hmall-aftersale-service", fallback = AfterSaleClientServiceImpl.class)
public interface IAfterSaleClientService {
    /**
     * 根据订单id查询订单数据
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/serviceOrder/queryByOrderId", method = RequestMethod.POST)
    ResponseData queryByOrderId(@RequestParam("orderId") Long orderId);
}
