package com.hand.hmall.job;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.mst.service.IMediaService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 马君
 * @version 0.1
 * @name SyncMediaToZmallJob
 * @description 同步多媒体到Zmall
 * @date 2017/8/1 8:47
 */
@RemoteJob
public class SyncMediaToZmallJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private IMediaService iMediaService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) {
        iMediaService.syncToZmall();
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
