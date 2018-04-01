package com.hand.hmall.aspect;

import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 马君
 * @version 0.1
 * @name OperationHistoryHandlerImpl
 * @description OperationHistoryHandlerImpl
 * @date 2017/10/13 9:45
 */
@Service
public class OperationHistoryHandlerImpl implements OperationHistoryHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private IOrderEntryService iOrderEntryService;

    @Autowired
    private IOrderService iOrderService;

    /**
     * 构建订单保存操作日志
     * @param order 订单
     * @return
     */
    @Override
    public String buildMessageForOrderSave(Order order) {
        Order sourceOrder = queryOrder(order.getOrderId());
        cacheOrder(sourceOrder);

        Class<?> type = order.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.equals("orderId") || field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            Object fieldValue = triggerGetter(order, field);
            if (fieldValue != null) {
                Object oriValue = triggerGetter(sourceOrder, field);
                if (!fieldValue.equals(oriValue)) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("订单" + sourceOrder.getCode());
                    buffer.append("字段" + fieldName + "从" + transferIfDate(oriValue));
                    buffer.append("改为" + transferIfDate(fieldValue) + ";");
                    OperationHistoryCache.append(buffer.toString());
                }
            }
        }

        List<OrderEntry> orderEntryList = order.getOrderEntries();
        if (CollectionUtils.isEmpty(orderEntryList)) {
            return OperationHistoryCache.get();
        }

        buildMessageForOrderEntries(orderEntryList);

        return OperationHistoryCache.get();
    }

    /**
     * 构建发货单保存操作日志
     * @param consignment 发货单
     * @return
     */
    @Override
    public String buildMessageForConsignmentSave(Consignment consignment) {
        Consignment sourceConsignment = queryConsignment(consignment.getConsignmentId());
        Order sourceOrder = queryOrder(sourceConsignment.getOrderId());
        cacheOrder(sourceOrder);

        Class<?> type = consignment.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.equals("consignmentId") || field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            Object fieldValue = triggerGetter(consignment, field);
            if (fieldValue != null) {
                Object oriValue = triggerGetter(sourceConsignment, field);
                if (!fieldValue.equals(oriValue)) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("发货单" + sourceConsignment.getCode());
                    buffer.append("字段" + fieldName + "从" + transferIfDate(oriValue));
                    buffer.append("改为" + transferIfDate(fieldValue) + ";");
                    OperationHistoryCache.append(buffer.toString());
                }
            }
        }

        List<OrderEntry> orderEntryList = consignment.getOrderEntries();
        if (CollectionUtils.isEmpty(orderEntryList)) {
            return OperationHistoryCache.get();
        }

        buildMessageForOrderEntries(orderEntryList);

        return OperationHistoryCache.get();
    }

    /**
     * 构建发货单拆分前操作日志
     * @param orderEntryList 被拆分订单行
     * @return
     */
    @Override
    public String buildMessageForConsignmentSplitBefore(List<OrderEntry> orderEntryList) {
        OrderEntry orderEntry = queryOrderEntry(orderEntryList.get(0).getOrderEntryId());
        Order sourceOrder = queryOrder(orderEntry.getOrderId());
        cacheOrder(sourceOrder);
        Long consignmentId = orderEntry.getConsignmentId();
        Consignment consignment = queryConsignment(consignmentId);
        OperationHistoryCache.append("发货单" + consignment.getCode() + "被拆分为");
        return OperationHistoryCache.get();
    }

    /**
     * 构建发货单拆分后操作日志
     * @param consignmentList
     * @return
     */
    @Override
    public String buildMessageForConsignmentSplitAfter(List<Consignment> consignmentList) {
        StringBuffer buffer = new StringBuffer();
        for (Consignment consignment : consignmentList) {
            buffer.append("发货单" + consignment.getCode() + "及");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        OperationHistoryCache.append(buffer.toString());
        return OperationHistoryCache.get();
    }

    /**
     * 构建订单行取消操作日志
     * @param orderEntryList 被取消订单行
     * @return
     */
    @Override
    public String buildMessageForOrderEntryCancel(List<OrderEntry> orderEntryList) {
        OrderEntry oriEntry = queryOrderEntry(orderEntryList.get(0).getOrderEntryId());
        Order sourceOrder = queryOrder(oriEntry.getOrderId());
        cacheOrder(sourceOrder);
        for (OrderEntry orderEntry : orderEntryList) {
            OrderEntry sourceEntry = queryOrderEntry(orderEntry.getOrderEntryId());
            OperationHistoryCache.append("PIN码行" + sourceEntry.getPin() + "已取消;");
        }
        return OperationHistoryCache.get();
    }

    /**
     * 发货单审核前进行订单cache
     * @param consignment 被审核发货单
     * @return
     */
    @Override
    public String buildMessageForConsignmentCheckBefore(Consignment consignment) {
        Consignment sourceConsignment = queryConsignment(consignment.getConsignmentId());
        Order sourceOrder = queryOrder(sourceConsignment.getOrderId());
        cacheOrder(sourceOrder);
        return OperationHistoryCache.get();
    }

    /**
     * 发货单审核后组装操作日志
     * @param consignment 被审核发货单
     * @return
     */
    @Override
    public String buildMessageForConsignmentCheckAfter(Consignment consignment) {
        Consignment sourceConsignment = queryConsignment(consignment.getConsignmentId());
        OperationHistoryCache.append("发货单" + sourceConsignment.getCode() + "已审核,");
        OperationHistoryCache.append("状态:" + consignment.getStatus() + ",");
        OperationHistoryCache.append("异常原因:" + consignment.getAbnormalReason());
        return OperationHistoryCache.get();
    }

    /**
     * 构造订单的修改日志
     * @param orderEntryList 订单行
     * @return String
     */
    private String  buildMessageForOrderEntries(List<OrderEntry> orderEntryList) {
        Field[] entryFields = OrderEntry.class.getDeclaredFields();
        for (OrderEntry orderEntry : orderEntryList) {
            OrderEntry sourceEntry = queryOrderEntry(orderEntry.getOrderEntryId());
            for (Field entryField : entryFields) {
                String entryFieldName = entryField.getName();
                if (entryFieldName.equals("orderEntryId") || entryField.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                Object entryFieldValue = triggerGetter(orderEntry, entryField);
                if (entryFieldValue != null) {
                    Object oriEntryFieldValue = triggerGetter(sourceEntry, entryField);
                    if (!entryFieldValue.equals(oriEntryFieldValue)) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("PIN码行" + sourceEntry.getPin());
                        buffer.append("字段" + entryFieldName + "从" + transferIfDate(oriEntryFieldValue));
                        buffer.append("改为" + transferIfDate(entryFieldValue) + ";");
                        OperationHistoryCache.append(buffer.toString());
                    }
                }
            }
        }

        return OperationHistoryCache.get();
    }

    /**
     * 构建订单行拆分操作日志
     * @param orderEntryList 订单行列表
     * @return
     */
    @Override
    public String buildMessageForOrderEntrySplitBefore(List<OrderEntry> orderEntryList) {
        OrderEntry sourceEntry = queryOrderEntry(orderEntryList.get(0).getOrderEntryId());
        Order sourceOrder = queryOrder(sourceEntry.getOrderId());
        cacheOrder(sourceOrder);
        return OperationHistoryCache.get();
    }

    /**
     * 构建订单行拆分操作日志
     * @param orderEntryList 订单行列表
     * @return
     */
    @Override
    public String buildMessageForOrderEntrySplitAfter(List<OrderEntry> orderEntryList) {
        for (OrderEntry orderEntry : orderEntryList) {
            OrderEntry oriOrderEntry = iOrderEntryService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderEntry);
            StringBuffer buffer = new StringBuffer();
            buffer.append("PIN码行" + oriOrderEntry.getPin());
            buffer.append("被拆分为PIN码行" + oriOrderEntry.getPin());
            buffer.append("及PIN码行" + orderEntry.getSpiltOutEntry().getPin() + ";");
            OperationHistoryCache.append(buffer.toString());
        }
        return OperationHistoryCache.get();
    }

    /**
     * 构建发货单暂挂操作日志
     * @param consignment 发货单
     * @return
     */
    @Override
    public String buildMessageForConsignmentHold(Consignment consignment) {
        Consignment sourceConsignment = queryConsignment(consignment.getConsignmentId());
        Order sourceOrder = queryOrder(sourceConsignment.getOrderId());
        cacheOrder(sourceOrder);
        OperationHistoryCache.append("发货单" + sourceConsignment.getCode() + "暂挂");
        return OperationHistoryCache.get();
    }

    /**
     * 根据发货单id查询发货单
     * @param consignmentId 发货单id
     * @return Consignment
     */
    private Consignment queryConsignment(Long consignmentId) {
        Consignment consignment = new Consignment();
        consignment.setConsignmentId(consignmentId);
        consignment = iConsignmentService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), consignment);
        return consignment;
    }

    /**
     * 查询订单行
     * @param orderEntryId 订单行id
     * @return OrderEntry
     */
    private OrderEntry queryOrderEntry(Long orderEntryId) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderEntryId(orderEntryId);
        return iOrderEntryService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderEntry);
    }

    /**
     * 查询订单
     * @param orderId 订单id
     * @return Order
     */
    private Order queryOrder(Long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        return iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), order);
    }

    /**
     * 调用字段的getter方法
     * @param model 实体类
     * @param field 字段
     * @return Object
     */
    private Object triggerGetter(Object model, Field field){
        String fieldName = field.getName();
        Method method;
        try {
            try {
                method = model.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (NoSuchMethodException e) {
                method = model.getClass().getMethod(fieldName);
            }
            return method.invoke(model, new Object[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 如果是时间则转化格式
     * @param value 字段值
     */
    private Object transferIfDate(Object value) {
        if (value instanceof Date) {
            return sdf.format((Date)value);
        }
        return value;
    }

    /**
     * 将发生事务前的订单、发货单、订单行保存起来
     * @param sourceOrder 原订单
     */
    @Override
    public void cacheOrder(Order sourceOrder) {
        OperationHistoryCache.setOrder(sourceOrder);
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(sourceOrder.getOrderId());
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntry);
        OperationHistoryCache.setOrderEntries(orderEntryList);
        Consignment consignment = new Consignment();
        consignment.setOrderId(sourceOrder.getOrderId());
        List<Consignment> consignmentList = iConsignmentService.select(consignment);
        OperationHistoryCache.setConsignments(consignmentList);
    }
}
