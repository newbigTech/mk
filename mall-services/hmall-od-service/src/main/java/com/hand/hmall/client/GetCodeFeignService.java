package com.hand.hmall.client;

import com.hand.hmall.client.impl.GetCodeFeginServiceHystrix;
import com.hand.hmall.client.impl.OrderCreateFeignServiceHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @name GetCodeFeginServiceHystrix
 * @Describe 提供订单号得获取及pin码获取的功能
 * @Author noob
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
@FeignClient(value = "hmall-thirdparty-service",fallback = GetCodeFeginServiceHystrix.class)
public interface GetCodeFeignService {
    /**
     * 获取订单编号
     * @return
     */
    @RequestMapping(value = "/h/getordercode",method = RequestMethod.GET)
    public ResponseData getOrderCode();
    /**
     * 获取pin码
     * @return
     */
    @RequestMapping(value = "/h/getpincode",method = RequestMethod.GET)
    public ResponseData getPinCode();
}
