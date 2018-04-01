package com.hand.promotion.util;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Describe 将前台map参数转换成对应DTO
 * @Author noob
 * @Date 2017/6/28 11:34
 */
public class MapToBean {
    private static Logger logger = LoggerFactory.getLogger("MapToBean");
    public static void transMap2Bean(Map<String, Object> map, Object object) {
        if (map == null || object == null) {
            return;
        }
        try {
            BeanUtils.populate(object, map);
        } catch (Exception e) {
            logger.error("transMap2Bean2 Error " , e);
        }
    }
}
