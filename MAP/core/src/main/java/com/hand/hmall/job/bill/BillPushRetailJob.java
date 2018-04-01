package com.hand.hmall.job.bill;

import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.OmAccounts;
import com.hand.hmall.om.service.IOmAccountsService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.ws.client.IBillPushClient;
import com.hand.hmall.ws.entities.BillRequestBody;
import com.hand.hmall.ws.entities.BillResponseBody;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangyanan
 * @Description:第三方账单推送Retail定时 JIRA:MAG-1177
 * @Date:Crated in 14:44 2017/9/22
 * @Modified By:
 */
@RemoteJob
public class BillPushRetailJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private IOmAccountsService accountsService;
    @Autowired
    private IBillPushClient iBillPushClient;

    @Autowired
    private ILogManagerService logManagerService;

    /**
     * 将第三方账单定时推送Retail系统
     *
     * @param jobExecutionContext
     * @throws Exception
     */
    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //启动日志管理
        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setProgramName(this.getClass().getName());
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager.setProgramDescription("账单信息推送Retail系统");
        logManager = logManagerService.logBegin(iRequest, logManager);

        //获得将要发送Retail系统的账单数据
        Map<String, Object> map = accountsService.organizationBillDataToRetail();
        //有符合的数据发送Retail系统
        if (map.size() > 0) {
            BillRequestBody billRequestBody = (BillRequestBody) map.get("requestBody");
            BillResponseBody responseBody = null;
            try {
                responseBody = iBillPushClient.billPush(billRequestBody);
            } catch (WSCallException e) {
                e.printStackTrace();
                logManager.setProcessStatus("FAIL");
                logManager.setMessage("账单推送Retail失败");
                logManager.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logManager);
                return;
            }
            if ("保存成功".equals(responseBody.getMsg())) {
                List<OmAccounts> omAccountsList = (List<OmAccounts>) map.get("omAccountsList");
                for (OmAccounts omAccounts : omAccountsList) {
                    omAccounts.set__status("update");
                    omAccounts.setSyncFlag("Y");
                }
                accountsService.batchUpdate(iRequest, omAccountsList);
                logManager.setProcessStatus("success");
                logManager.setMessage("账单传送Retail成功");
                logManager.setReturnMessage("成功");
                logManagerService.logEnd(iRequest, logManager);
            } else {
                logManager.setProcessStatus("FAIL");
                logManager.setMessage("账单传送Retail失败");
                logManager.setReturnMessage("失败");
                logManagerService.logEnd(iRequest, logManager);
            }
        } else {
            logManager.setProcessStatus("success");
            logManager.setMessage("没有未发送的账单记录,暂不发送Retail系统");
            logManager.setReturnMessage("成功");
            logManagerService.logEnd(iRequest, logManager);
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
