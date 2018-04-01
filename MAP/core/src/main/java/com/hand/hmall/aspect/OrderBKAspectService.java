package com.hand.hmall.aspect;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hap.core.IRequest;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderBkService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XinyangMei
 * @Title OrderBKAspect
 * @Description 订单备份
 * @date 2017/8/8 9:36
 */
@Aspect
@Component("odBkAspect")
public class OrderBKAspectService {

    @Resource
    private IOrderBkService orderBkService;

    @Autowired
    private OperationHistoryHandler operationHistoryHandler;

    public OrderBKAspectService() {
        System.out.println("------------create aspect-----------");
    }

    /**
     * 官网订单行取消插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderEntryServiceImpl.cancelOrderEntry(..))")
    public void cancelOrderEntryPointcut() {}

    /**
     * 天猫订单行取消插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderEntryServiceImpl.cancelOrderEntryForTm(..))")
    public void cancelOrderEntryForTmPointcut(){}

    /**
     * 官网订单行全部取消插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderEntryServiceImpl.cancelAllOrderAndRntry(..))")
    public void cancelAllOrderAndRntryPointcut(){}

    /**
     * 天猫订单行全部取消插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderEntryServiceImpl.cancelAllOrderAndRntryForTm(..))")
    public void cancelAllOrderAndRntryForTmPointcut(){}

    /**
     * 订单行取消前构造操作信息
     * @param joinPoint 插入点参数
     */
    @Before("cancelOrderEntryPointcut() || cancelOrderEntryForTmPointcut() || cancelAllOrderAndRntryPointcut() || cancelAllOrderAndRntryForTmPointcut()")
    public void cancelOrderEntryBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        List<OrderEntry> orderEntryList = (List<OrderEntry>) args[1];
        operationHistoryHandler.buildMessageForOrderEntryCancel(orderEntryList);
    }

    /**
     * 订单行取消后插入备份和操作日志
     * @param joinPoint 插入点参数
     */
    @AfterReturning("cancelOrderEntryPointcut() || cancelOrderEntryForTmPointcut() || cancelAllOrderAndRntryPointcut() || cancelAllOrderAndRntryForTmPointcut()")
    public void cancelOrderEntryAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest request = (IRequest) args[0];
        orderBkService.saveOrderBkAndLog(request.getUserId(), "订单行取消");
    }

    /**
     * 发货单审核插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.ConsignmentServiceImpl.consignmentCheck(..))")
    public void consignmentCheckPointcut() {}

    /**
     * 发货单审核前构造操作信息
     * @param joinPoint 插入点参数
     */
    @Before("consignmentCheckPointcut()")
    public void consignmentCheckBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        Consignment consignment = (Consignment)args[1];
        operationHistoryHandler.buildMessageForConsignmentCheckBefore(consignment);
    }

    /**
     * 发货单审核后插入备份和操作日志
     * @param joinPoint 插入点参数
     */
    @AfterReturning("consignmentCheckPointcut()")
    public void consignmentCheckAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest iRequest = (IRequest)args[0];
        Consignment consignment = (Consignment) args[1];
        operationHistoryHandler.buildMessageForConsignmentCheckAfter(consignment);
        orderBkService.saveOrderBkAndLog(iRequest.getUserId(), "发货单审核");
    }

    /**
     * 订单保存切入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderServiceImpl.saveOrderFunc(..))")
    public void saveOrderPointcut() {}


    /**
     * 订单保存前构造操作信息
     * @param joinPoint 插入点参数
     */
    @Before("saveOrderPointcut()")
    public void saveOrderBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        Order order = (Order) args[1];
        operationHistoryHandler.buildMessageForOrderSave(order);
    }


    /**
     * 订单保存后插入备份和操作日志
     * @param joinPoint 插入点参数
     */
    @AfterReturning("saveOrderPointcut()")
    public void saveOrderAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest iRequest = (IRequest) args[0];
        orderBkService.saveOrderBkAndLog(iRequest.getUserId(), "订单保存");
    }

    /**
     * 发货单保存切入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.ConsignmentServiceImpl.saveConsignment(..))")
    public void saveConsignmentPointcut() {}

    /**
     * 发货单保存前构造操作信息
     * @param joinPoint 插入点参数
     */
    @Before("saveConsignmentPointcut()")
    public void saveConsignmentBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        Consignment consignment = (Consignment) args[1];
        operationHistoryHandler.buildMessageForConsignmentSave(consignment);
    }

    /**
     * 发货单保存后插入备份和操作日志
     * @param joinPoint 插入点参数
     */
    @AfterReturning("saveConsignmentPointcut()")
    public void saveConsignmentAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest iRequest = (IRequest) args[0];
        orderBkService.saveOrderBkAndLog(iRequest.getUserId(), "发货单保存");
    }

    /**
     * 订单行拆分服务插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.OrderEntryServiceImpl.spiltOrderEntry(..))")
    public void splitOrderEntryPointcut(){}

    @Before("splitOrderEntryPointcut()")
    public void splitOrderEntryBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        List<OrderEntry> orderEntryList = (List<OrderEntry>) args[1];
        operationHistoryHandler.buildMessageForOrderEntrySplitBefore(orderEntryList);
    }

    /**
     * 订单行拆分后插入备份和操作日志
     * @param joinPoint 插入点参数
     */
    @AfterReturning("splitOrderEntryPointcut()")
    public void splitOrderEntryAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest request = (IRequest) args[0];
        List<OrderEntry> orderEntryList = (List<OrderEntry>) args[1];
        operationHistoryHandler.buildMessageForOrderEntrySplitAfter(orderEntryList);
        orderBkService.saveOrderBkAndLog(request.getUserId(), "订单行拆分");
    }

    /**
     * 发货单拆分插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.ConsignmentServiceImpl.split(..))")
    public void splitConsignmentPointcut(){}

    @Before("splitConsignmentPointcut()")
    public void splitConsignmentBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        List<OrderEntry> orderEntryList = (List<OrderEntry>) args[1];
        operationHistoryHandler.buildMessageForConsignmentSplitBefore(orderEntryList);
    }

    @AfterReturning(pointcut = "splitConsignmentPointcut()", returning = "splitConsignments")
    public void splitConsignmentAfter(JoinPoint joinPoint, List<Consignment> splitConsignments) {
        Object args[] = joinPoint.getArgs();
        IRequest request = (IRequest) args[0];
        operationHistoryHandler.buildMessageForConsignmentSplitAfter(splitConsignments);
        orderBkService.saveOrderBkAndLog(request.getUserId(), "发货单拆分");
    }

    /**
     * 发货单挂载插入点
     */
    @Pointcut("execution(* com.hand.hmall.om.service.impl.ConsignmentServiceImpl.getBodyForOrderUpdateForConsignmentHold(..))")
    public void holdConsignmentPointcut(){}

    @Before("holdConsignmentPointcut()")
    public void holdConsignmentBefore(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        Consignment consignment = (Consignment) args[1];
        operationHistoryHandler.buildMessageForConsignmentHold(consignment);
    }

    @AfterReturning("holdConsignmentPointcut()")
    public void holdConsignmentAfter(JoinPoint joinPoint) {
        Object args[] = joinPoint.getArgs();
        IRequest iRequest = (IRequest) args[0];
        orderBkService.saveOrderBkAndLog(iRequest.getUserId(), "暂挂");
    }
}
