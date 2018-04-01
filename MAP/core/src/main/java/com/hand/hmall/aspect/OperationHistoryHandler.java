package com.hand.hmall.aspect;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OperationHistoryHandler
 * @description OperationHistoryHandler
 * @date 2017/10/12 20:11
 */
public interface OperationHistoryHandler {

    /**
     * 构建订单保存操作日志
     * @param order 订单
     * @return String
     */
    String buildMessageForOrderSave(Order order);

    /**
     * 构建发货单保存操作日志
     * @param consignment 发货单
     * @return String
     */
    String buildMessageForConsignmentSave(Consignment consignment);

    /**
     * 构建发货单拆分前操作日志
     * @param orderEntryList 被拆分订单行
     * @return String
     */
    String buildMessageForConsignmentSplitBefore(List<OrderEntry> orderEntryList);


    /**
     * 构建发货单拆分后操作日志
     * @param splitConsignments 被拆出的发货单
     * @return String
     */
    String buildMessageForConsignmentSplitAfter(List<Consignment> splitConsignments);

    /**
     * 构建订单行取消操作日志
     * @param orderEntryList 被取消订单行
     * @return String
     */
    String buildMessageForOrderEntryCancel(List<OrderEntry> orderEntryList);

    /**
     * 发货单审核前进行订单cache
     * @param consignment 被审核发货单
     * @return String
     */
    String buildMessageForConsignmentCheckBefore(Consignment consignment);

    /**
     * 发货单审核后组装操作日志
     * @param consignment 被审核发货单
     * @return String
     */
    String buildMessageForConsignmentCheckAfter(Consignment consignment);

    /**
     * 构建订单行拆分操作日志
     * @param orderEntryList 订单行列表
     * @return String
     */
    String buildMessageForOrderEntrySplitBefore(List<OrderEntry> orderEntryList);

    /**
     * 构建订单行拆分操作日志
     * @param orderEntryList 订单行列表
     * @return String
     */
    String buildMessageForOrderEntrySplitAfter(List<OrderEntry> orderEntryList);

    /**
     * 构建发货单暂挂操作日志
     * @param consignment 发货单
     * @return String
     */
    String buildMessageForConsignmentHold(Consignment consignment);

    /**
     * 将发生事务前的订单、发货单、订单行保存起来
     * @param sourceOrder 原订单
     */
    void cacheOrder(Order sourceOrder);
}
