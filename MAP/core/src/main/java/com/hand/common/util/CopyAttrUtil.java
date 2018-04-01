package com.hand.common.util;/**
 * Created by WangQiang on 2017/5/12.
 * Email: qiang.wang01@hand-china.com
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qiang.wang01@hand-china.com
 * @version 1.0
 * @name ReflectUtil
 * @description   将1类中相同的属性字段值copy到2类
 * @date 2017/5/12
 */
public class CopyAttrUtil {
    public static void reflectionAttr(Object source,Object target) throws Exception{
        Class clazz1 = Class.forName(source.getClass().getName());
        Class clazz2 = Class.forName(target.getClass().getName());
//      获取两个实体类的所有属性
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        CopyAttrUtil cr = new CopyAttrUtil();
//      遍历class1Bean，获取逐个属性值，然后遍历class2Bean查找是否有相同的属性，如有相同则赋值
        for (Field f1 : fields1) {
            if(f1.getName().equals("id"))
                continue;
            Object value = cr.invokeGetMethod(source ,f1.getName(),null);
            for (Field f2 : fields2) {
                if(f1.getName().equals(f2.getName())){
                    cr.invokeSetMethod(target, f2.getName(), value);
                }
            }
        }

    }

    /**
     *
     * 执行某个Field的getField方法
     *
     * @param clazz 类
     * @param fieldName 类的属性名称
     * @param args 参数，默认为null
     * @return
     */
    private Object invokeGetMethod(Object clazz, String fieldName, Object[] args)
    {
        //拼接方法名
        String methodName = fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
        Method method = null;
        try
        {
            method = Class.forName(clazz.getClass().getName()).getDeclaredMethod("get" + methodName);
            return method.invoke(clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * 执行某个Field的setField方法
     *
     * @param clazz 类
     * @param fieldName 类的属性名称
     * @param args 参数，默认为null
     * @return
     */
    private Object invokeSetMethod(Object clazz, String fieldName, Object args)
    {

        //拼接方法名
        String methodName = fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
        Method method = null;
        try
        {
            Class[] parameterTypes = new Class[1];
            Class c = Class.forName(clazz.getClass().getName());
            Field field = c.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            method = c.getDeclaredMethod("set" + methodName,parameterTypes);
            if (null != args && !"".equals(args)) {
                String  value=args.toString();
                String fieldType = field.getType().getSimpleName();
                if ("String".equals(fieldType)) {
                    method.invoke(clazz, value.trim());
                } else if ("Date".equals(fieldType)) {
                    Date temp = parseDate(value.trim());
                    method.invoke(clazz, temp);
                } else if ("Integer".equals(fieldType)
                        || "int".equals(fieldType)) {
                    Integer intval = Integer.parseInt(value.trim());
                    method.invoke(clazz, intval);
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    Long temp = Long.parseLong(value.trim());
                    method.invoke(clazz, temp);
                } else if ("Double".equalsIgnoreCase(fieldType)) {
                    Double temp = Double.parseDouble(value.trim());
                    method.invoke(clazz, temp);
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    Boolean temp = Boolean.parseBoolean(value.trim());
                    method.invoke(clazz, temp);
                } else {
                    System.out.println("not supper type" + fieldType);
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }
}