/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IUserSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 李伟
 * @version 1.0
 * @name:UserSendMessageController
 * @Description: 前台发送短信
 * @date 2017/8/1 15:45
 */
@RestController
@RequestMapping("/i")
public class UserSendMessageController {

    @Autowired
    private IUserSendMessageService iUserSendMessageService;

    @RequestMapping(value = "/sendMSG", method = RequestMethod.POST)
    public ResponseData sendMSG(@RequestBody Map<String, Object> map)
    {
        return iUserSendMessageService.sendSMG(map);
    }

}
