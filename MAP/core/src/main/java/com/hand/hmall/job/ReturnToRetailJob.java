package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.dto.ResponseData;
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
 * @Description: 退货单推送retail的job
 * @date 2017/9/24 13:48
 */
@RemoteJob
public class ReturnToRetailJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAsReturnService asReturnService;

    @Autowired
    private ILogManagerService logManagerService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setUserId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        iRequest.setUserName(Constants.JOB_DEFAULT_OPERATOR);

        //筛选数据
        List<AsReturn> asReturnList = asReturnService.selectDateForReturnToRetail(iRequest);
        if (CollectionUtils.isNotEmpty(asReturnList)) {
            for (AsReturn asReturn : asReturnList) {
                LogManager log = new LogManager();
                log.setStartTime(new Date());
                log.setProgramName(this.getClass().getName());
                log.setDataPrimaryKey(asReturn.getAsReturnId());
                log.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
                if (asReturn.getSapCode() == null) {
                    log.setProgramDescription("退货单推送retail-新增");
                } else {
                    log.setProgramDescription("退货单推送retail-修改");
                }
                log = logManagerService.logBegin(iRequest, log);

                try {
                    ResponseData responseData = asReturnService.sendToRetail(asReturn.getAsReturnId(), iRequest);
                    if (responseData.isSuccess()) {
                        log.setProcessStatus("S");
                        log.setReturnMessage("成功！");
                        logManagerService.logEnd(iRequest, log);
                    } else {
                        log.setProcessStatus("E");
                        log.setReturnMessage(responseData.getMsg());
                        logManagerService.logEnd(iRequest, log);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    log.setProcessStatus("E");
                    log.setReturnMessage(e.getMessage());
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
