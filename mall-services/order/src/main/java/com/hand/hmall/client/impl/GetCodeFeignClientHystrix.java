/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.client.impl;

import com.hand.hmall.client.GetCodeFeignClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.stereotype.Service;

/**
 * @name GetCodeFeginServiceHystrix
 * @Describe 提供订单行号获取的功能
 * @Author 李伟
 * @Date 2017/7/21 10:34
 * @version 1.0
 */
@Service
public class GetCodeFeignClientHystrix implements GetCodeFeignClient
{
    /**
     * 获取订单编号
     * @return
     */
    @Override
    public ResponseData getOrderCode() {
        return new ResponseData(false);
    }

    /**
     * 获取订单行编号
     * @return
     */
    @Override
    public ResponseData getOrderEntryCode() {
        return new ResponseData(false);
    }

    /**
     * 获取pin码
     * @return
     */
    @Override
    public ResponseData getPinCode(){
        return new ResponseData (false);
    }
}
