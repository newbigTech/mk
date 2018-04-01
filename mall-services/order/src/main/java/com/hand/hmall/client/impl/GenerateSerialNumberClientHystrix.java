package com.hand.hmall.client.impl;

import com.hand.hmall.client.GenerateSerialNumberClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name GenerateSerialNumberClientHystrix
 * @description 流水号生成三方服务客户端断路器
 * @date 2017/7/19 14:24
 */
@Service
public class GenerateSerialNumberClientHystrix implements GenerateSerialNumberClient {

    /**
     * 获取订单行流水号断路器
     * @return String
     */
    @Override
    public ResponseData getOrderEntryCode() {
        return null;
    }
}
