package com.hand.hmall.util;


import java.util.Map;
//tryGet(Map map, String[] keys)判断map中是否有这些key

public class HmallMapUtil {
    public static boolean tryGet(Map map, String[] keys){
        for (String key : keys){
           if( !map.containsKey(key) ){
               return false;
           }
        }
        return true;
    }

//判断必需字段是否为空
    public static boolean isInput(Map map, String[] singleKeys,String[] multiKeys){
        //单项是否非空
        boolean singleInput = true;
        //多项是否不全为空,如果无多填一选项,就只看单项判断
        boolean multiInput = true;
        for (String key : singleKeys){
            if(map.get(key).equals("")){
                //有一项不填就不能通过
                return false;
            }
        }

        for (String key : multiKeys) {
            if (!map.get(key).equals("")){
                //填上一项即可通过非空检测
               return true;
            }
            //出现多选项并且为空则给多选判断赋值false
            multiInput = false;
        }
        return multiInput && singleInput;
    }

    public static void setMap(Map fromMap, Map toMap, String[] keys){
        for (String key : keys){
            toMap.put(key,fromMap.get(key));
        }
    }


}
