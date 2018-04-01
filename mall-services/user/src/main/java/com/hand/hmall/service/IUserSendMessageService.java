/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.Map;

/**
 * @author 李伟
 * @version 1.0
 * @name:IUserSendMessageService
 * @Description:
 * @date 2017/8/1 14:37
 */
public interface IUserSendMessageService
{

    /**
     * 前台发送短信验证请求
     * @param map
     */
    ResponseData sendSMG(Map<String, Object> map);
}
