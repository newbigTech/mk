package com.hand.hmall.client;

import com.hand.hmall.client.impl.CodeSequenceClientHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @version 1.0
 * @name CodeSequenceClient
 * @Describe 实体编码序列客户端
 * @Author chenzhigang
 * @Date 2017/9/11
 */
@FeignClient(value = "hmall-thirdparty-service", fallback = CodeSequenceClientHystrix.class)
public interface CodeSequenceClient {

//    @GetMapping("/h/nextCode")
    @RequestMapping(method = RequestMethod.GET, value = "/h/nextCode")
    String nextCode(@RequestParam("type") String type);

    @RequestMapping(method = RequestMethod.GET, value = "/h/nextServiceCode")
    String nextServiceCode(@RequestParam("type") String type);

}
