package com.hand.hmall.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author yijie.liu@hand-china.com
 * @version 0.1
 * @name SortList
 * @description 序号
 * @date 2017年5月26日10:52:23
 */
public class SortList {
    public static void sort(List<Map<String, ?>> data, String condition) {
        Collections.sort(data, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                Long a = Long.parseLong(o1.get(condition).toString());
                Long b = Long.parseLong(o2.get(condition).toString());
                // 升序
                // return a.compareTo(b);
                // 降序
                return b.compareTo(a);
            }
        });
    }

    /**
     * 添加序号
     *
     * @param list
     * @return
     */
    public static List<Map<String, ?>> addNumber(List<Map<String, ?>> list) {
        for (int i = 1; i <= list.size(); i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i - 1);
            map.put("number", i);
            list.set(i - 1, map);
        }
        return list;
    }

    /**
     * 添加序号
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> addNumberO(List<Map<String, Object>> list) {
        for (int i = 1; i <= list.size(); i++) {
            Map<String, Object> map = list.get(i - 1);
            map.put("number", i);
            list.set(i - 1, map);
        }
        return list;
    }

    /**
     * 添加序号
     *
     * @param list
     * @return
     */
    public static List<Map<String, ?>> addNumber(List<Map<String, ?>> list, Integer currPage, Integer pageSize) {
        Integer number = (currPage - 1) * pageSize + 1;
        for (int i = 1; i <= list.size(); i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i - 1);
            map.put("number", number);
            list.set(i - 1, map);
            number++;
        }
        return list;
    }

    /**
     * 添加序号
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> addNumberO(List<Map<String, Object>> list, Integer currPage, Integer pageSize) {
        Integer number = (currPage - 1) * pageSize + 1;
        for (int i = 1; i <= list.size(); i++) {
            Map<String, Object> map = list.get(i - 1);
            map.put("number", number);
            list.set(i - 1, map);
            number++;
        }
        return list;
    }


}
