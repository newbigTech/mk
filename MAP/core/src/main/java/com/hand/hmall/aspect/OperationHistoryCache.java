package com.hand.hmall.aspect;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OperationHistoryCache
 * @description <p>操作历史版本信息持有者：切面将会把操作历史数据暂存到这里，
 * 该类线程安全，保存订单备份时，取出订单修改历史并记录历史
 * @date 2017/10/12 9:47
 */
public class OperationHistoryCache {

    private static final ThreadLocal<StringBuffer> cache = new ThreadLocal<>();
    private static final ThreadLocal<Order> orderCache = new ThreadLocal<>();
    private static final ThreadLocal<List<Consignment>> consignmentsCache = new ThreadLocal<>();
    private static final ThreadLocal<List<OrderEntry>> orderEntriesCache = new ThreadLocal<>();

    /**
     * 添加消息
     * @param message 消息
     */
    public static void append(String message) {
        if (cache.get() == null) {
            cache.set(new StringBuffer());
        }
        cache.get().append(message);
    }

    /**
     * 获取Cache中的消息
     * @return String
     */
    public static String get() {
        if (cache.get() != null) {
            return cache.get().toString();
        }
        return "";
    }

    /**
     * 获取并清空Cache中的数据
     * @return String
     */
    public static String getAndClear() {
        String message = "";
        if (cache.get() != null) {
            message = cache.get().toString();
            cache.get().delete(0, message.length());
        }
        return message;
    }

    public static void setOrder(Order order) {
        System.out.println("======================set=" + Thread.currentThread().getName());
        orderCache.set(order);
    }

    public static Order getOrder() {
        System.out.println("======================get=" + Thread.currentThread().getName());
        return orderCache.get();
    }

    public static void setConsignments(List<Consignment> consignments) {
        consignmentsCache.set(consignments);
    }

    public static List<Consignment> getConsignments() {
        return consignmentsCache.get();
    }

    public static void setOrderEntries(List<OrderEntry> orderEntries) {
        orderEntriesCache.set(orderEntries);
    }

    public static List<OrderEntry> getOrderEntries() {
        return orderEntriesCache.get();
    }
}
