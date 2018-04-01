package com.hand.hap.util;/**
 * Created by WangQiang on 2017/6/5.
 * Email: qiang.wang01@hand-china.com
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author qiang.wang01@hand-china.com
 * @version 1.0
 * @name EmptyCheckUtil
 * @description  若对象内全为空或者为null,则返回true
 * @date 2017/6/5
 */
public class EmptyCheckUtil {

    public static boolean checkFieldValueNull(Object bean){
        boolean nullFlag=true;
        if(bean!=null){
            Class<?> cls = bean.getClass();
           // Method[] methods = cls.getDeclaredMethods();
            Field[] fields = cls.getDeclaredFields();
            for (Field f :fields) {
                f.setAccessible(true);
                try {
                    if (f.get(bean) != null && !"".equals(f.get(bean))) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                        nullFlag=false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return nullFlag;
    }
}
