package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * author: zhangzilong
 * name: ConsignmentJob
 * discription: 发货单推送Zmall接口
 * date: 2017/8/2
 * version: 0.1
 */
@RemoteJob
public class ConsignmentJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    IConsignmentService service;
    @Autowired
    ILogManagerService logManagerService;

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
        log.setProgramDescription("配货单回传商城");
        log = logManagerService.logBegin(iRequest, log);
        try {
            Map<String, String> result = service.sendToZmall();
            log.setMessage(result.get("dataJson"));
            log.setProcessStatus(result.get("code"));
            log.setReturnMessage(result.get("message"));
            logManagerService.logEnd(iRequest, log);
        } catch (Exception e) {
            log.setProcessStatus("E");
            log.setReturnMessage(e.getMessage());
            logger.error(e.getMessage());
            logManagerService.logEnd(iRequest, log);
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}