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
import com.hand.hmall.ws.client.ICheckBillPushClient;
import com.hand.hmall.ws.entities.CheckBillRequestBody;
import com.hand.hmall.ws.entities.CheckBillResponseBody;
import com.hand.hmall.ws.entities.CheckBillResponseItem;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangyanan
 * @Description:MAG-1351 核对账单功能推送Retail定时
 * @Date:Crated in 14:08 2017/10/17
 * @Modified By:
 */
@RemoteJob
public class CheckBillRetailJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private IOmAccountsService iOmAccountsService;

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private ICheckBillPushClient iCheckBillPushClient;

    /**
     * 核对账单信息推送Retail系统,并返回会计凭证号(VOUCHERNO)
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
        logManager.setProgramDescription("核对账单信息推送Retail系统");
        logManager = logManagerService.logBegin(iRequest, logManager);

        //获得将要发送Retail系统核对账单数据
        Map<String, Object> map = iOmAccountsService.checkBillDataToRetail();
        //有符合的数据发送Retail系统
        if (map.size() > 0) {
            CheckBillRequestBody checkBillRequestBody = (CheckBillRequestBody) map.get("requestBody");
            CheckBillResponseBody responseBody = null;
            try {
                responseBody = iCheckBillPushClient.checkBillPush(checkBillRequestBody);
                //返回查询到的会计凭证号
                List<CheckBillResponseItem> checkBillResponseItemList = responseBody.getCheckBillResponseItemList();
                //批量更新会计凭证号
                if (null != checkBillResponseItemList && !checkBillResponseItemList.isEmpty()) {
                    List<OmAccounts> omAccountsList = new ArrayList<>();
                    for (CheckBillResponseItem checkBillResponseItem : checkBillResponseItemList) {
                        System.out.println(checkBillResponseItem.getBelnr());
                        OmAccounts omAccounts = new OmAccounts();
                        omAccounts.setAccountsId(Long.valueOf(checkBillResponseItem.getZckh()));
                        omAccounts.setVoucherno(checkBillResponseItem.getBelnr());
                        omAccounts.set__status("update");
                        omAccountsList.add(omAccounts);
                    }
                    iOmAccountsService.batchUpdate(iRequest, omAccountsList);
                    logManager.setProcessStatus("success");
                    logManager.setMessage("账单传送Retail成功");
                    logManager.setReturnMessage("成功");
                } else {
                    logManager.setProcessStatus("success");
                    logManager.setMessage("没有获取到符合条件的会计凭证号");
                    logManager.setReturnMessage("成功");
                }
            } catch (WSCallException e) {
                e.printStackTrace();
                logManager.setProcessStatus("FAIL");
                logManager.setMessage("账单推送Retail失败");
                logManager.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logManager);
                return;
            }
        } else {
            logManager.setProcessStatus("success");
            logManager.setMessage("没有符合条件的的核对账单记录,暂不发送Retail系统");
            logManager.setReturnMessage("成功");
        }
        logManagerService.logEnd(iRequest, logManager);
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
