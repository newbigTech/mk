package com.hand.hap.cloud.hpay.services.toZmall.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.toZmall.IPostTargetSystemService;
import com.hand.hap.cloud.hpay.utils.SendReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * author: zhangzilong
 * name: PostToZmallService
 * discription:回传Zmall统一入口，方便记录日志
 * date: 2017/9/4
 * version: 0.1
 */
@Service
public class PostToTargetSystemServiceImpl implements IPostTargetSystemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;

    /**
     * 发送请求到目标系统的同时记录日志
     *
     * @param url
     * @param content
     * @param params
     * @return
     */
    @Override
    public JSONObject postToTargetSystem(String url, String content, Map<String, String> params) throws Exception {
        String response = SendReq.post(url, content, params);
        logger.info("目标系统返回信息>>>>>>>>>>>>>>" + response);
        return JSON.parseObject(response);
    }
}
