package com.hand.hmall.services.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alaowan
 * Created at 2017/12/22 11:13
 * @description
 */
public class BeanConvertUtil {

    private static boolean initialized;

    public static void registerBeanConvertors() {
        if (initialized)
            return;
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        initialized = true;
    }

    /**
     * @param originList  源集合
     * @param targetClass 目标Class
     * @return targetList   返d回集合
     */
    public static List copyListProperties(List<? extends Object> originList, Class targetClass) {
        List targetList = new ArrayList();
        Object targetObj = null;
        for (Object origin : originList) {
            try {
                targetObj = targetClass.newInstance();
                BeanUtils.copyProperties(targetObj, origin);
                targetList.add(targetObj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return targetList;
    }
}
