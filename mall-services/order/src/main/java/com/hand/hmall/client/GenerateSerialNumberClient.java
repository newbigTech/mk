package com.hand.hmall.client;

import com.hand.hmall.client.impl.GenerateSerialNumberClientHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 马君
 * @version 0.1
 * @name GenerateSerialNumberClient
 * @description 流水号生成三方服务调用客户端
 * @date 2017/7/19 14:20
 */
@FeignClient(value = "hmall-thirdparty-service", fallback = GenerateSerialNumberClientHystrix.class)
public interface GenerateSerialNumberClient {

    /**
     * 获取订单行流水号
     * @return String
     */
    @RequestMapping(value = "/h/getorderecode",method = RequestMethod.GET)
    ResponseData getOrderEntryCode();
}
