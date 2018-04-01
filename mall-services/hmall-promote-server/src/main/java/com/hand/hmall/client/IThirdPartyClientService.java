package com.hand.hmall.client;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.client.impl.ThirdPartyClientServiceImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title IThirdPartyClientService
 * @Description desp
 * @date 2017/9/29 10:36
 */
@FeignClient(value = "hmall-thirdparty-service", fallback = ThirdPartyClientServiceImpl.class)
public interface IThirdPartyClientService {
    @RequestMapping(value = "/h/getcouponcode/{type}/{length}", method = RequestMethod.GET)
    public ResponseData getcouponcode(@RequestParam(value = "type") String type, @RequestParam(value = "length") String length);
}
