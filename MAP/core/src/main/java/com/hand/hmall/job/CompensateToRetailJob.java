package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.as.dto.AsCompensate;
import com.hand.hmall.as.service.IAsCompensateService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by qinzhipeng
 *
 * @Description: 销售赔付单推送retail job
 * on 2017/10/13.
 */
@RemoteJob
public class CompensateToRetailJob extends AbstractJob implements RemoteJobTask {
    @Autowired
    private IAsCompensateService asCompensateService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        AsCompensate asCompensate = new AsCompensate();
        // asCompensate.setCompensateId(10029L);
        asCompensate.setSyncflag("N");
        List<AsCompensate> asCompensateList = asCompensateService.selectSendRetailData(asCompensate);
        if (CollectionUtils.isNotEmpty(asCompensateList)) {
            for (AsCompensate compensate : asCompensateList) {
                asCompensateService.compensateSyncRetail(iRequest, compensate);
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
