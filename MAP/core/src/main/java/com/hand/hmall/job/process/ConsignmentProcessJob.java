package com.hand.hmall.job.process;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.process.engine.ProcessManager;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentProcessJob
 * @description 发货单流程
 * @date 2017/7/17 16:15
 */
@RemoteJob
public class ConsignmentProcessJob extends AbstractJob implements RemoteJobTask {

    private static final Logger logger = LoggerFactory.getLogger(ConsignmentProcessJob.class);
    private static final String NEW_CREATE = "NEW_CREATE";
    private static final String JOB_DESC = "发货单流程JOB";

    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    @Qualifier("consignmentProcessManager")
    private ProcessManager<Consignment> consignmentProcessManager;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);
        List<Consignment> consignmentList = iConsignmentService.selectByStatus(NEW_CREATE);
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "发现" + CollectionUtils.size(consignmentList) + "张发货单");

        if (CollectionUtils.isNotEmpty(consignmentList)) {
            for (Consignment consignment : consignmentList) {
                consignmentProcessManager.start(consignment);
            }
        }

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, JOB_DESC + "结束");
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
