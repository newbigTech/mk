package com.hand.hmall.job.process;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.process.engine.ProcessManager;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OrderToConsignmentJob
 * @description 订单生成发货单流程
 * @date 2017/6/5 14:14
 */
@RemoteJob
public class OrderToConsignmentJob extends AbstractJob implements RemoteJobTask {

    private static final String JOB_DESC = "订单生成发货单流程";

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    @Qualifier("orderProcessManager")
    private ProcessManager<Order> orderProcessManager;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);
        List<Order> orderList = iOrderService.selectPendingOrders();
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "发现" + CollectionUtils.size(orderList) + "张订单");

        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }

        for (Order order : orderList) {
            if (Constants.YES.equals(order.getIsIo())) {
                // 虚拟订单直接发起备货
                orderProcessManager.start(order);
            } else {
                // 非虚拟订单计算启动备货金额
                Double paymentAmount = order.getPaymentAmount() == null ? 0D : order.getPaymentAmount();
                Double stockUpAmount;
                try {
                    stockUpAmount = iOrderService.getStockUpAmount(order);
                } catch (Exception e) {
                    iLogManagerService.logError(this.getClass(), "订单生成发货单JOB", order.getOrderId(), e.getMessage());
                    continue;
                }
                if (paymentAmount >= stockUpAmount) {
                    // 如果已支付金额大于启动备货金额，发起备货
                    orderProcessManager.start(order);
                } else {
                    iLogManagerService.logError(this.getClass(), "订单生成发货单JOB", order.getOrderId(),
                            "订单[" + order.getOrderId() + "]已支付金额[" + paymentAmount + "]未达到启动备货金额[" + stockUpAmount + "]");
                }
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
