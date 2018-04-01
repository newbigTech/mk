package com.hand.hmall.client.impl;

import com.hand.hmall.client.OrderCreateFeignService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @name OrderCreateFeignServiceHystrix
 * @Describe 提供订单下载功能
 * @Author noob
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
@Service
public class OrderCreateFeignServiceHystrix implements OrderCreateFeignService {
    /**
     * 订单下载
     *
     * @Author: noob
     * @param order 新增订单所需传入的相关参数
     * @return
     * @Date: 2017/5/12 16:02
     *
     */
    @Override
    public ResponseData addOrder(@RequestBody HmallOmOrder order) {
        return new ResponseData(false);
    }



}
