package com.hand.promotion.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.exception.CriteriaUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author XinyangMei
 * @Title MongoUtil
 * @Description desp
 * @date 2017/12/11 10:03
 */
public class MongoUtil {
    private static Logger logger = LoggerFactory.getLogger("MongoUtil");

    /**
     * 将查询的pojo转换为对应的Criteria，只处理基础数据字段 ，字段间的关系为and
     *
     * @param pojo
     * @param <T>
     * @return
     */
    public static <T> Criteria pojoToCriteria(T pojo) {
        Class pojoClass = pojo.getClass();
        java.lang.reflect.Field[] declaredFields = pojoClass.getDeclaredFields();
        Criteria criteria = null;
        boolean initCriteriaFlag = false;
        //遍历pojo的所有字段
        for (java.lang.reflect.Field declaredField : declaredFields) {

            //将pojo字段映射到criteria
            Object invokeResult = null;
            try {
                Method declaredMethod = pojoClass.getDeclaredMethod(getMenthodName(declaredField.getName()));
                invokeResult = declaredMethod.invoke(pojo);
            } catch (Exception e) {
                logger.error("########生成Criteria异常", e);
                throw new RuntimeException();
            }
            if (invokeResult != null) {
                if (!initCriteriaFlag) {
                    criteria = Criteria.where(declaredField.getName()).is(invokeResult);
                    initCriteriaFlag = true;
                } else {
                    criteria.and(declaredField.getName()).is(invokeResult);
                }
            }
        }

        return criteria;
    }

    /**
     * 将pojo转换为对应的update对象
     *
     * @param pojo
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> Update pojoToUpdate(T pojo) {
        Class pojoClass = pojo.getClass();
        java.lang.reflect.Field[] declaredFields = pojoClass.getDeclaredFields();
        Update update = new Update();
        //遍历pojo的所有字段
        for (java.lang.reflect.Field declaredField : declaredFields) {

            //将pojo字段映射到criteria
            Object invokeResult = null;
            try {
                Method declaredMethod = pojoClass.getDeclaredMethod(getMenthodName(declaredField.getName()));
                invokeResult = declaredMethod.invoke(pojo);
            } catch (Exception e) {
                logger.error("########生成Update异常", e);
                throw new RuntimeException();
            }
            if (invokeResult != null) {
                update.set(declaredField.getName(), invokeResult);
            }
        }
        return update;
    }

    /**
     * 根据fieldName 获取对应的getter方法名称
     *
     * @param fieldName
     * @return
     */
    public static String getMenthodName(String fieldName) {
        StringBuffer sb = new StringBuffer("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();

    }

}
