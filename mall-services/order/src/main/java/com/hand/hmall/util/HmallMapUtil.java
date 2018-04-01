package com.hand.hmall.util;


import org.apache.commons.lang.StringUtils;

import java.util.Map;
//tryGet(Map map, String[] keys)判断map中是否有这些key

/**
 * @author 孙锐
 * @Title:HmallMapUtil
 * @Description: 工具类，生成当前时间字符串和流水号
 * @date 2017/5/24 14:41
 * @version 1.0
 */
public class HmallMapUtil {
    //判断map中是否全部包含指定的数组中的key
    public static boolean tryGet(Map map, String[] keys){
        for (String key : keys){
           if( !map.containsKey(key) ){
               return false;
           }
        }
        return true;
    }

    /**
     * 判断必需字段是否为空
     * @param map 需要被判断的map
     * @param singleKeys 该key中的每个字段都不能为空
     * @param multiKeys 该keys中一个存在一个字段不为空即可
     * @return
     */
    public static boolean isInput(Map map, String[] singleKeys,String[] multiKeys){
        //单项是否非空
        boolean singleInput = true;
        //多项是否不全为空,如果无多填一选项,就只看单项判断
        boolean multiInput = true;
        for (String key : singleKeys){
            if(map.get(key) == null || "".equals(map.get(key))){
                //有一项不填就不能通过
                return false;
            }
        }

        for (String key : multiKeys) {
            if (map.get(key) != null && !"".equals(map.get(key))){
                //填上一项即可通过非空检测
               return true;
            }
            //出现多选项并且为空则给多选判断赋值false
            multiInput = false;
        }
        return multiInput && singleInput;
    }

    /**
     * 根据keys数组，将fromMap中key与keys中的值相对应的k-v存储到头Map中
     * @param fromMap 源map
     * @param toMap 新map
     * @param keys 需要转移的key
     */
    public static void setMap(Map fromMap, Map toMap, String[] keys){
        for (String key : keys){
            toMap.put(key,fromMap.get(key));
        }
    }


}
