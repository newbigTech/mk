package com.hand.hmall.job;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.service.IProductService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 马君
 * @version 0.1
 * @name SyncItemToProductJob
 * @description 同步物料到商品主数据
 * @date 2017/6/28 19:07
 */
@RemoteJob
public class SyncItemToProductJob extends AbstractJob implements RemoteJobTask {

    private static final Logger logger = LoggerFactory.getLogger(SyncItemToProductJob.class);
    private static final String JOB_DESC = "零部件同步HMALL任务JOB";

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);
        iProductService.syncItemToProduct();
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "完成" + JOB_DESC);
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
