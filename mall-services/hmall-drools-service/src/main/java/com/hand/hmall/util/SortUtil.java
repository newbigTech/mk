package com.hand.hmall.util;

import com.hand.hmall.model.Order;
import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.OrderEntryPojo;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/9.
 * 排序工具类
 */
public class SortUtil {

    public static void sort(List list, String property, boolean asc) {
        Comparator comparator = ComparableComparator.getInstance();
        comparator = ComparatorUtils.nullLowComparator(comparator);
        if (!asc) {
            comparator = ComparatorUtils.reversedComparator(comparator);
        }
        Collections.sort(list, new BeanComparator(property, comparator));
    }


    /**
     * 对集合进行排序
     *
     * @param resultList 要排序的集合
     * @param score      按照该字段排序
     * @param isAsc      true为降序 false为升序
     * @throws Exception
     */
    public static void listsort(List<Map<String, ?>> resultList, String score, boolean isAsc) throws Exception {

        Collections.sort(resultList, new Comparator<Map<String, ?>>() {

            public int compare(Map<String, ?> map1, Map<String, ?> map2) {

                //o1，o2是list中的Map，可以在其内取得值，按其排序
                Integer s1 = (Integer) map1.get(score);
                Integer s2 = (Integer) map2.get(score);
                if(isAsc){
                    return s1.compareTo(s2);
                }else {
                    return -s1.compareTo(s2);
                }
//                if ((s1 > s2 ^ isAsc)) {
//                    return -1;
//                } else if (s1.intValue() == s2.intValue()) {
//                    return 0;
//                } else {
//                    return 1;
//                }
            }
        });

    }

    /**
     * 按照金额对订单行集合排序
     * @param products
     * @param isAsc true为降序 false为升序
     * @throws Exception
     */
    public static void productsSortByPrice(List<OrderEntryPojo> products, boolean isAsc) throws Exception {

        Collections.sort(products, new Comparator<OrderEntryPojo>() {

            public int compare(OrderEntryPojo product1, OrderEntryPojo product2) {
                Double s1 = Double.valueOf(product1.getBasePrice());
                Double s2 = Double.valueOf(product2.getBasePrice());

                if(isAsc){
                    return s1.compareTo(s2);
                }else {
                    return -s1.compareTo(s2);
                }
            }
        });

    }

    /**
     * 方法已弃用
     * @param orders
     */
    public static void sortOrder(List<Order> orders) {
        int i, j;
        int length = orders.size();
        Order targetOrder;


        //插入排序
        for (i = 1; i < length; i++) {
            j = i;
            targetOrder = orders.get(i);
            while (j > 0 && targetOrder.getPrice().getAccount() > orders.get(j - 1).getPrice().getAccount()) {
                orders.set(j, orders.get(j - 1));
                j--;
            }

            orders.set(j, targetOrder);
        }


    }

}
