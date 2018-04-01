package com.hand.hmall.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author XinyangMei
 * @Title GetMapValueUtil
 * @Description desp
 * @date 2017/11/14 19:59
 */
public class GetMapValueUtil {

    /**
     * 获取Long类型的字段
     *
     * @param map
     * @param key
     * @return
     */
    public static Long getLong(Map map, String key) {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        return jsonObject.getLong(key);
    }

    /**
     * 获取Double类型的字段
     *
     * @param map
     * @param key
     * @return
     */
    public static Double getDouble(Map map, String key) {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        return jsonObject.getDouble(key);
    }

}
