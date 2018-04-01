/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.client.impl;

import com.hand.hmall.client.OrderUpdateFeignService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 李伟
 * @version 1.0
 * @name:OrderUpdateFeignServiceHystrix
 * @Description:
 * @date 2017/7/21 9:17
 */
@Service
public class OrderUpdateFeignServiceHystrix implements OrderUpdateFeignService {

    /**
     * 订单更新
     *
     * @Author: 李伟
     * @param order 更新订单所需传入的相关参数
     * @return
     * @Date: 2017/7/21 9:21
     *
     */
    @Override
    public ResponseData updateOrder(@RequestBody HmallOmOrder order) {
        return new ResponseData(false);
    }

}
