package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author 李伟
 * @version 1.0
 * @name:ConsignmentStatusSyncOrderJob
 * @Description: 发货单状态同步订单状态
 * @date 2017/9/8 11:23
 */
@RemoteJob
public class ConsignmentStatusSyncOrderJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ILogManagerService logManagerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IConsignmentService consignmentService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setMessage("no data found");
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        log.setProgramDescription("发货单状态同步订单");
        log = logManagerService.logBegin(iRequest, log);

        //获取订单数据
        List<Order> orders = orderService.selectOrderListByStatus();
        logger.info("---同步订单的数量---:" + orders.size());

        if (CollectionUtils.isNotEmpty(orders)) {
            try {
                for (Order order : orders) {
                    logger.info("---订单Id---:" + order.getOrderId());
                    logger.info("---订单更改前状态---:" + order.getOrderStatus());
                    Consignment consignment = new Consignment();
                    consignment.setOrderId(order.getOrderId());
                    List<Consignment> consignments = consignmentService.select(consignment);

                    logger.info("---发货单数量---:" + consignments.size());
                    Boolean waitBuyerConfirmFlag = false; //是否待收货
                    Boolean tradeBuyerSingedFlag = false; //是否买家已签收
                    Boolean waitForDeliveryFlag = false; //是否待发货
                    Boolean processErrorFlag = false; //是否进程错误
                    Boolean abnormalFlag = false; //是否异常
                    if (CollectionUtils.isNotEmpty(consignments)) {
                        for (Consignment c : consignments) {
                            //发货单状态
                            String conStatus = c.getStatus();
                            logger.info("---发货单状态---:" + conStatus);
                            if (StringUtils.isNotEmpty(conStatus)) {
                                switch (conStatus) {
                                    //待收货
                                    case "WAIT_BUYER_CONFIRM":
                                        waitBuyerConfirmFlag = true;
                                        break;
                                    //买家已签收
                                    case "TRADE_BUYER_SIGNED":
                                        tradeBuyerSingedFlag = true;
                                        break;
                                    //待发货
                                    case "WAIT_FOR_DELIVERY":
                                        waitForDeliveryFlag = true;
                                        break;
                                    //进程错误
                                    case "PROCESS_ERROR":
                                        processErrorFlag = true;
                                        break;
                                    //异常
                                    case "ABNORMAL":
                                        abnormalFlag = true;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    logger.info("待收货[WAIT_BUYER_CONFIRM]:" + waitBuyerConfirmFlag + "||" + "买家已签收[TRADE_BUYER_SIGNED]:" + tradeBuyerSingedFlag + "||" +
                            "待发货[waitForDeliveryFlag]:" + waitForDeliveryFlag + "||" + "进程错误[PROCESS_ERROR]:" + processErrorFlag + "||" +
                            "异常[ABNORMAL]:" + abnormalFlag);

                    //发货单状态
                    //WAIT_BUYER_CONFIRM -- 待收货
                    //TRADE_BUYER_SIGNED -- 买家已签收
                    //WAIT_FOR_DELIVERY -- 待发货
                    //PROCESS_ERROR -- 进程错误
                    //ABNORMAL -- 异常

                    //订单状态
                    //PART_DELIVERY -- 部分发货
                    //WAIT_BUYER_CONFIRM -- 待收货
                    //TRADE_BUYER_SIGNED -- 买家已签收

                    // 订单下的发货单满足以下条件时，订单状态变为PART_DELIVERY
                    // 1.发货单状态存在WAIT_BUYER_CONFIRM或TRADE_BUYER_SIGNED
                    // 2.发货单状态存在ABNORMAL或WAIT_FOR_DELIVERY或PROCESS_ERROR
                    if ((waitBuyerConfirmFlag || tradeBuyerSingedFlag) && (abnormalFlag || waitForDeliveryFlag || processErrorFlag)) {
                        if (!"PART_DELIVERY".equals(order.getOrderStatus())) {
                            logger.info("----订单状态发生变化，重置同步商城标识位----");
                            order.setSyncZmall("N");
                            logger.info("----更改订单状态为[部分发货]:PART_DELIVERY----");
                            order.setOrderStatus("PART_DELIVERY");
                        } else {
                            logger.info("----订单状态为[部分发货]:PART_DELIVERY, 无需修改----");
                        }
                    } else if (waitBuyerConfirmFlag && (!(abnormalFlag || waitForDeliveryFlag || processErrorFlag))) {
                        //订单下的发货单满足以下条件时，订单状态变为WAIT_BUYER_CONFIRM
                        //1.发货单状态存在WAIT_BUYER_CONFIRM
                        //2.发货单状态不存在ABNORMAL或WAIT_FOR_DELIVERY或PROCESS_ERROR
                        if (!"WAIT_BUYER_CONFIRM".equals(order.getOrderStatus())) {
                            logger.info("----订单状态发生变化，重置同步商城标识位----");
                            order.setSyncZmall("N");
                            logger.info("----更改订单状态为[待收货]:WAIT_BUYER_CONFIRM----");
                            order.setOrderStatus("WAIT_BUYER_CONFIRM");
                        } else {
                            logger.info("----订单状态为[待收货]:WAIT_BUYER_CONFIRM, 无需修改----");
                        }

                    } else if (tradeBuyerSingedFlag && (!(abnormalFlag || waitForDeliveryFlag || processErrorFlag || waitBuyerConfirmFlag)) && (!order.getOrderStatus().equals("TRADE_FINISHED"))) {
                        //订单下的发货单满足以下条件时，订单状态变为TRADE_BUYER_SIGNED
                        //1.发货单状态存在TRADE_BUYER_SIGNED
                        //2.发货单状态不存在ABNORMAL或WAIT_FOR_DELIVERY或PROCESS_ERROR或WAIT_BUYER_CONFIRM
                        if (!"TRADE_BUYER_SIGNED".equals(order.getOrderStatus())) {
                            logger.info("----订单状态发生变化，重置同步商城标识位----");
                            order.setSyncZmall("N");
                            logger.info("----更改订单状态为[买家已签收]:TRADE_BUYER_SIGNED----");
                            order.setOrderStatus("TRADE_BUYER_SIGNED");
                        } else {
                            logger.info("----订单状态为[买家已签收]:TRADE_BUYER_SIGNED, 无需修改----");
                        }
                    }

                    //更新订单
                    orderService.updateByPrimaryKeySelective(iRequest, order);

                    logger.info("---更改后订单状态---:" + order.getOrderStatus());
                    logger.info("===========================================");
                    log.setProcessStatus(Constants.JOB_STATUS_SUCCESS);
                    log.setReturnMessage("发货单状态同步订单成功");
                    logManagerService.logEnd(iRequest, log);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.setProcessStatus(Constants.JOB_STATUS_ERROR);
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
            }

        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
