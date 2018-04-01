package com.hand.hmall.job;

import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.MstFabric;
import com.hand.hmall.mst.service.IMstFabricService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name 面料等级同步M3D
 * @description
 * @date 2017/8/24 17:14
 */
@RemoteJob
public class SyncFabricLevelToM3DJob extends AbstractJob implements RemoteJobTask {

    private static final String JOB_DESC = "面料等级同步M3D";

    @Autowired
    private IMstFabricService iMstFabricService;

    @Autowired
    private IGlobalVariantService iGlobalVariantService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);

        // 查询待同步的面料等级
        MstFabric mstFabricParam = new MstFabric();
        mstFabricParam.setSyncflag(Constants.NO);
        List<MstFabric> mstFabricList = iMstFabricService.select(RequestHelper.newEmptyRequest(), mstFabricParam, 1, Integer.MAX_VALUE);
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "发现[" + CollectionUtils.size(mstFabricList) + "]条待同步面料等级");

        if (CollectionUtils.isEmpty(mstFabricList)) {
            return;
        }

        try {
            iMstFabricService.postToM3D(mstFabricList);
        } catch (Exception e) {
            iLogManagerService.logError(this.getClass(), JOB_DESC, null, e.getMessage());
        }

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, JOB_DESC + "完成");
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
