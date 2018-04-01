package com.hand.hap.cloud.thirdParty.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.thirdParty.entities.OutBoundLogs;
import com.hand.hap.cloud.thirdParty.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.thirdParty.mq.MessageSender;
import com.hand.hap.cloud.thirdParty.services.IPostZmallService;
import com.hand.hap.cloud.thirdParty.utils.LogsUtils;
import com.hand.hap.cloud.thirdParty.utils.SendReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * author: zhangzilong
 * name: PostToZmallService
 * discription:回传Zmall统一入口，方便记录日志
 * date: 2017/9/4
 * version: 0.1
 */
@Service
public class PostToZmallServiceImpl implements IPostZmallService {

    @Autowired
    private MessageSender messageSender;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送请求到Zmall的同时记录日志
     *
     * @param url
     * @param content
     * @param params
     * @return
     */
    @Override
    public JSONObject postToZmall(String url, String content, Map<String, String> params) throws Exception {
        String response = SendReq.post(url, content, params);
        OutBoundLogs logs = LogsUtils.getLogs() == null ? new OutBoundLogs() : LogsUtils.getLogs();
        logs.setResponseBody(response);
        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        if (logs.getRequestTime() != null) {
            logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
        }
        logs.setSuccess(true);
        messageSender.sendMsg(logs);
        LogsUtils.clear();
        return JSON.parseObject(response);
    }
}
