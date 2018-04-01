package com.hand.hap.cloud.thirdParty.services;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * author: zhangzilong
 * name: IPostZmallService
 * discription: 回传Zmall统一入口，方便记录日志
 * date: 2017/9/4
 * version: 0.1
 */
public interface IPostZmallService {

    /**
     * 发送请求到Zmall的同时记录日志
     *
     * @param url
     * @param content
     * @param params
     * @return
     */
    JSONObject postToZmall(String url,String content, Map<String, String> params) throws Exception;
}
