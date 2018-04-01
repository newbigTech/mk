package com.hand.hmall.client.impl;

import com.hand.hmall.client.GetCodeFeignService;
import com.hand.hmall.dto.ResponseData;
import org.springframework.stereotype.Service;

/**
 * @name GetCodeFeginServiceHystrix
 * @Describe 提供订单号得获取及pin码获取的功能
 * @Author noob
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
@Service
public class GetCodeFeginServiceHystrix implements GetCodeFeignService {
    /**
     * 获取订单编号
     * @return
     */
    @Override
    public ResponseData getOrderCode() {
        return new ResponseData(false);
    }

    /**
     * 获取pin码
     * @return
     */
    @Override
    public ResponseData getPinCode() {
        return new ResponseData(false);
    }
}
