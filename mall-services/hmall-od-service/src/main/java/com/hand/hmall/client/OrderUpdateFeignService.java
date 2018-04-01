/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.client;

import com.hand.hmall.client.impl.OrderUpdateFeignServiceHystrix;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 李伟
 * @version 1.0
 * @name:OrderUpdateFeignService
 * @Description:
 * @date 2017/7/21 9:15
 */
@FeignClient(value = "order" ,fallback = OrderUpdateFeignServiceHystrix.class)
public interface OrderUpdateFeignService {
    /**
     * 订单更新
     *
     * @Author: 李伟
     * @param order 更新订单所需传入的相关参数
     * @return
     * @Date: 2017/7/21 9:19
     *
     */
    @RequestMapping(value = "/o/order/updateOrder",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateOrder(@RequestBody HmallOmOrder order);
}
