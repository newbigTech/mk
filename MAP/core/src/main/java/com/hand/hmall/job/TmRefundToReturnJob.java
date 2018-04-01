package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.as.dto.AsTmrefund;
import com.hand.hmall.as.service.IAsTmrefundService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 天猫退款单生成退货单job
 * @date 2017/9/21 21:07
 */
@RemoteJob
public class TmRefundToReturnJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IAsTmrefundService asTmrefundService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        iRequest.setUserName(Constants.JOB_DEFAULT_OPERATOR);

        List<AsTmrefund> list = asTmrefundService.selectDataForTmRefundToReturnJob(iRequest);
        if (CollectionUtils.isNotEmpty(list)) {
            for (AsTmrefund asTmrefund : list) {

                LogManager log = new LogManager();
                log.setStartTime(new Date());
                log.setProgramName(this.getClass().getName());
                log.setDataPrimaryKey(asTmrefund.getTmrefundId());
                log.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
                log.setProgramDescription("TM退款单生成退货单");
                log = logManagerService.logBegin(iRequest, log);
                try {
                    asTmrefundService.tmRefundToReturn(iRequest, asTmrefund);
                    log.setProcessStatus("S");
                    log.setReturnMessage("成功！");
                    logManagerService.logEnd(iRequest, log);
                } catch (Exception e) {
                    logger.info(e.getMessage());

                    asTmrefund.setCreatreturn("N");
                    asTmrefundService.updateByPrimaryKeySelective(iRequest, asTmrefund);

                    log.setProcessStatus("E");
                    log.setReturnMessage("TM退款单" + asTmrefund.getTmrefundId() + "生成退货单失败:" + e.getMessage());
                    logManagerService.logEnd(iRequest, log);
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
