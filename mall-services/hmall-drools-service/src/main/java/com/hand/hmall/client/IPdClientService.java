package com.hand.hmall.client;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */
import com.hand.hmall.client.impl.PdClientServiceImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author XinyangMei
 * @Title IPdClientService
 * @Description desp
 * @date 2017/8/4 10:54
 */
@FeignClient(value = "hmall-pd-service", fallback = PdClientServiceImpl.class)
public interface IPdClientService {
    /**
     * 调用商品微服务，查询商品基础价格
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/i/price/getGiftPrice", method = RequestMethod.POST)
    public ResponseData getProductPrice(@RequestParam("productId") Integer productId);

}


