/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.client;

import com.hand.hmall.client.impl.GetCodeFeignClientHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @name GetCodeFeginServiceHystrix
 * @Describe 提供订单行号获取的功能
 * @Author 李伟
 * @Date 2017/7/21 10:32
 * @version 1.0
 */
@FeignClient(value = "hmall-thirdparty-service",fallback = GetCodeFeignClientHystrix.class)
public interface GetCodeFeignClient
{

    /**
     * 获取订单编号
     * @return
     */
    @RequestMapping(value = "/h/getordercode",method = RequestMethod.GET)
    ResponseData getOrderCode();

    /**
     * 获取订单行编号
     * @return
     */
    @RequestMapping(value = "/h/getorderecode",method = RequestMethod.GET)
    ResponseData getOrderEntryCode();

    /**
     * 获取pin码
     * @return
     */
    @RequestMapping(value = "/h/getpincode",method = RequestMethod.GET)
    ResponseData getPinCode();
}
