package com.hand.hmall.client;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.client.impl.PromoteClientImpl;
import com.hand.hmall.client.impl.UserClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author XinyangMei
 * @Title IUserClient
 * @Description desp
 * @date 2017/7/21 14:54
 */
@FeignClient(value="user",fallback = UserClient.class)
public interface IUserClient {
    @RequestMapping(value = "/i/customer/info/queryNotEqual", method = RequestMethod.POST)
    public ResponseData queryNotEqual(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/customer/info/queryByUserIds", method = RequestMethod.POST)
    public ResponseData queryByUserIds(@RequestBody Map<String, Object> map);

}
