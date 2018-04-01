package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.client.IUserClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author XinyangMei
 * @Title HapUserController
 * @Description desp
 * @date 2017/7/21 14:51
 */
@RequestMapping("/h/user")
@RestController
public class HapUserController {
    @Autowired
    private IUserClient userClient;
    @RequestMapping(value = "/queryNotEqual", method = RequestMethod.POST)
    public ResponseData queryNotEqual(@RequestBody Map<String, Object> map) {
        return userClient.queryNotEqual(map);
    }

    @RequestMapping(value = "/queryByUserIds", method = RequestMethod.POST)
    public ResponseData queryByUserIds(@RequestBody Map<String, Object> map) {
        return userClient.queryByUserIds(map);
    }
}
