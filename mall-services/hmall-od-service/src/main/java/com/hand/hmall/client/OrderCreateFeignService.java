package com.hand.hmall.client;

import com.hand.hmall.client.impl.OrderCreateFeignServiceHystrix;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @name OrderCreateFeignServiceHystrix
 * @Describe 提供订单下载功能
 * @Author noob
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
@FeignClient(value = "order",fallback = OrderCreateFeignServiceHystrix.class)
public interface OrderCreateFeignService {

    /**
     * 订单下载
     *
     * @Author: noob
     * @param order 新增订单所需传入的相关参数
     * @return
     * @Date: 2017/5/12 16:02
     *
     */
    @RequestMapping(value = "/o/order/addOrder",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData addOrder(@RequestBody HmallOmOrder order);


}
