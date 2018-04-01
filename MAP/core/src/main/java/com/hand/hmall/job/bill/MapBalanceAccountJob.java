package com.hand.hmall.job.bill;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.om.service.IOmBalanceService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:zhangyanan
 * @Description:中台对账轮询定时JOB
 * @Date:Crated in 14:39 2017/11/13
 * @Modified By:
 */
@RemoteJob
public class MapBalanceAccountJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private IOmBalanceService iOmBalanceService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        iOmBalanceService.handleAssociatedData();
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
