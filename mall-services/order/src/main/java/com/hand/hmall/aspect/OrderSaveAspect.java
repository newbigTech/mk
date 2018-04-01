package com.hand.hmall.aspect;

import com.hand.hmall.mapper.FndBusinessLogMapper;
import com.hand.hmall.mapper.HmallOmConsignmentMapper;
import com.hand.hmall.mapper.HmallOmOrderEntryMapper;
import com.hand.hmall.mapper.HmallOmOrderMapper;
import com.hand.hmall.model.*;
import com.hand.hmall.service.IOrderBkService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name 订单保存切面
 * @description OrderSaveAspect
 * @date 2017/10/24 9:05
 */
@Component
@Aspect
public class OrderSaveAspect {


    @Autowired
    private IOrderBkService iOrderBkService;

    @Autowired
    private FndBusinessLogMapper fndBusinessLogMapper;

    @Autowired
    private HmallOmOrderMapper hmallOmOrderMapper;

    @Autowired
    private HmallOmOrderEntryMapper hmallOmOrderEntryMapper;

    @Autowired
    private HmallOmConsignmentMapper hmallOmConsignmentMapper;

    private final ThreadLocal<HmallOmOrder> oriOrder = new ThreadLocal<>();

    private ThreadLocal<List<HmallOmConsignment>> consignmentList = new ThreadLocal<>();

    private ThreadLocal<List<HmallOmOrderEntry>> orderEntryList = new ThreadLocal<>();

    /**
     * 添加订单插入点
     */
    @Pointcut("execution(* com.hand.hmall.service.impl.OrderCreateServiceImpl.addOrder(..))")
    public void addOrderPointcut(){}

    /**
     * 修改订单切入点
     */
    @Pointcut("execution(* com.hand.hmall.service.impl.OrderCreateServiceImpl.updateOrder(..))")
    public void upOrderPointcut(){}

    /**
     * 订单插入后保存操作日志
     * @param joinPoint joinPoint
     */
    @AfterReturning("addOrderPointcut()")
    public void addOrderAfterReturning(JoinPoint joinPoint) {

        // 从jointpoint中获取订单对象
        HmallOmOrder order = (HmallOmOrder) joinPoint.getArgs()[0];

        // 日志消息
        String logMessage = "订单" + order.getCode() + "已下载至中台";

        // 查询系统用户id
        Long systemId = iOrderBkService.selectUserByCode("system");

        // 保存订单日志
        FndBusinessLog log = new FndBusinessLog();
        log.setOrderId(order.getOrderId());
        log.setOperationType("中台订单下载");
        log.setCurrentVersion(null);
        log.setLastVersion(null);

        // 保存审核人
        if (systemId != null) {
            log.setOperationUser(systemId);
        }

        log.setOperationContent(logMessage);
        log.setOperationTime(new Date());

        fndBusinessLogMapper.insertSelective(log);
    }

    @Before("upOrderPointcut()")
    public void upOrderAfterBefore(JoinPoint joinPoint) {
        // 从jointpoint中获取订单对象
        HmallOmOrder order = (HmallOmOrder) joinPoint.getArgs()[0];

        // 查询原始订单
        HmallOmOrder sourceOrder = hmallOmOrderMapper.selectByMutiItems(order.getEscOrderCode(), order.getWebsiteId(),
                order.getSalechannelId(), order.getStoreId());
        oriOrder.set(sourceOrder);

        // 查询原发货单
        HmallOmConsignment consignment = new HmallOmConsignment();
        consignment.setOrderId(sourceOrder.getOrderId());
        consignmentList.set(hmallOmConsignmentMapper.select(consignment));

        // 查询原订单行
        HmallOmOrderEntry orderEntry = new HmallOmOrderEntry();
        orderEntry.setOrderId(sourceOrder.getOrderId());
        orderEntryList.set(hmallOmOrderEntryMapper.select(orderEntry));
    }

    @AfterReturning("upOrderPointcut()")
    public void upOrderAfterReturning(JoinPoint joinPoint) {

        // 先保存订单备份
        HmallOmOrderBk orderBk = iOrderBkService.saveOrderBk(oriOrder.get(), consignmentList.get(), orderEntryList.get());

        // 查询系统用户id
        Long systemId = iOrderBkService.selectUserByCode("system");

        // 保存订单日志
        FndBusinessLog log = new FndBusinessLog();
        log.setOrderId(oriOrder.get().getOrderId());
        log.setOperationType("中台订单更新");
        log.setCurrentVersion(orderBk.getVersion() + 1);
        log.setLastVersion(orderBk.getVersion());

        // 保存审核人
        if (systemId != null) {
            log.setOperationUser(systemId);
        }

        log.setOperationContent("订单" + oriOrder.get().getCode() + "已更新");
        log.setOperationTime(new Date());

        fndBusinessLogMapper.insertSelective(log);
    }
}
