package com.hand.promotion.util;

import com.hand.promotion.pojo.order.OrderEntryPojo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ListUtil {
    /**
     * 创建一个List集合
     *
     * @param t
     * @return
     */
    public static <T> List<T> createList(T t) {
        List<T> list = new ArrayList<T>();
        list.add(t);
        return list;
    }

    /**
     * 创建一个空的List集合
     *
     * @return
     */
    public static <T> List<T> createEmptyList() {
        List<T> list = new ArrayList<T>();
        return list;
    }


    /**
     * List<String>集合去重
     */
    public static List<String> distinct(List<String> list) {
        if (!CollectionUtils.isEmpty(list)) {
            Set set = new HashSet(list);
            list = new ArrayList(set);
        }
        return list;
    }

    /**
     * List<OrderEntryPojo>集合去重
     */
    public static List<OrderEntryPojo> distinctEntrys(List<OrderEntryPojo> list) {
        if (!CollectionUtils.isEmpty(list)) {
            Set set = new HashSet(list);
            list = new ArrayList(set);
        }
        return list;
    }

    /**
     * 对list集合进行分页处理
     *
     * @return
     */
    public static <T> List<T> listSplit(List<T> list, Integer pageSize, Integer page) {
        Integer total = list.size();
        List<T> newList = list.subList(pageSize * (page - 1), ((pageSize * page) > total ? total : (pageSize * page)));
        return newList;
    }

    public static <T> List<T> mapValueToList(Map map) {
        Collection values = map.values();
        List<T> list = new ArrayList<>(values);
        return list;
    }

}
