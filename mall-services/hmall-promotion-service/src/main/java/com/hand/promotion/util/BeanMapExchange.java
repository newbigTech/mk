package com.hand.promotion.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class BeanMapExchange {

    /**
     * 将beans转换为maps对象
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> beans) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (T bean : beans) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (bean != null) {
                BeanMap beanMap = BeanMap.create(bean);
                map.putAll(beanMap);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 将bean转换为map对象
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            map.putAll(beanMap);
        }
        return map;
    }

    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(maps);
        List<T> list = JSONArray.parseArray(jsonStr, clazz);
        return list;
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(map);
        T object = JSONArray.parseObject(jsonStr, clazz);
        return object;
    }

}
