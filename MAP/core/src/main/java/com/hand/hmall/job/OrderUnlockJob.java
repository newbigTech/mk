package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shoupeng.wei@hang-china.com
 * @version 0.1
 * @name OrderSyncStore
 * @description 将所有已加锁订单置为未加锁状态
 * @date 2017年8月1日20:30:22
 */
@RemoteJob
public class OrderUnlockJob extends AbstractJob implements RemoteJobTask {

    //job 执行过程状态，E 失败，S 成功，U Job启动
    private static final String PROCESS_STATUS_E = "E";
    private static final String PROCESS_STATUS_S = "S";
    private static final String PROCESS_STATUS_U = "U";

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IOrderService orderService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setUserId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setProcessStatus(PROCESS_STATUS_U);
        logManager.setMessage("No Data Found.");
        logManager.setProgramName(this.getClass().getName());
        logManager.setProgramDescription("订单定时解锁");
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager = logManagerService.logBegin(iRequest, logManager);

        //获取所有锁定的订单信息
        List<Order> orderList = orderService.getLockedOrder();

        List<String> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            logManager.setMessage("需要解锁的订单信息：" + resultList);
            try {
                for (Order order : orderList) {
                    order.setLocked("N");
                    order = orderService.updateByPrimaryKeySelective(iRequest, order);
                    resultList.add(order.getCode());
                }
                logManager.setProcessStatus(PROCESS_STATUS_S);
                logManager.setReturnMessage("解锁成功的订单编码：" + resultList);

                logManagerService.logEnd(iRequest, logManager);
            } catch (Exception e) {
                logManager.setProcessStatus(PROCESS_STATUS_E);
                logManager.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logManager);
                throw e;
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
