package com.hand.hmall.job.bill;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.service.IOmAccountsRecordService;
import com.hand.hmall.om.service.IOmAccountsService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hmall.job
 * @Description
 * @date 2017/9/12
 */
@RemoteJob
public class UnionBillJob extends AbstractJob implements RemoteJobTask {

    private Logger logger = LoggerFactory.getLogger(WechatBillJob.class);

    @Autowired
    private IOmAccountsService accountsService;

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IGlobalVariantService iGlobalVariantService;

    @Autowired
    private IOmAccountsRecordService iOmAccountsRecordService;


    /**
     * 银联账单解析
     *
     * @param jobExecutionContext
     * @throws Exception
     */
    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setProgramName(this.getClass().getName());
        logManager.setProgramDescription("银联账单解析");
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager = logManagerService.logBegin(iRequest, logManager);

        ResponseData responseData;
        StringBuffer sb = new StringBuffer();
        //失败标志位
        boolean flag = true;
        try {
            Integer maxDate = Integer.valueOf(iGlobalVariantService.getValue("ACCOUNT_DAY"));
            if (null == maxDate) {
                throw new Exception("全局变量ACCOUNT_DAY没有维护");
            }
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            //按照当天日期,获取前天倒推VALUE天数的日期
            for (int i = maxDate + 1; i > 0; i--) {
                //倒推前一天的账单
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date preDate = calendar.getTime();

                //判断账单是否已经获取
                Map<String, String> condition = new HashMap<>();
                condition.put("channel", "UNIONPAY");
                condition.put("recordDate", new DateTime(preDate).toString("yyyy-MM-dd"));
                if (null != iOmAccountsRecordService.getOmAccountsRecord(condition)) {
                    sb.append(preDate + "已存在账单记录,无需下载解析");
                    continue;
                } else {//下载对应日期的账单并解析
                    responseData = accountsService.UnionBillAnalysisService(preDate);
                    if (responseData.isSuccess()) {
                        sb.append(preDate + "账单解析成功");
                    } else {
                        flag = false;
                        sb.append(preDate + "账单解析失败,原因:" + responseData.getMsgCode() + ";");
                    }
                }
            }

        } catch (Exception e) {
            logger.error("插入失败或者重复插入", e);
            logManager.setProcessStatus("FAIL");
            logManager.setMessage("插入失败或者重复插入");
            logManager.setReturnMessage(e.getMessage());
            logManagerService.logEnd(iRequest, logManager);
            return;
        }
        if (!flag) {
            logger.error("账单解析失败");
            logManager.setProcessStatus("FAIL");
            logManager.setMessage("账单解析失败");
        } else {
            logManager.setProcessStatus("SUCCESS");
            logManager.setMessage("账单解析成功");
            logger.error("账单解析成功");
        }
        logManager.setReturnMessage(sb.toString());
        logManagerService.logEnd(iRequest, logManager);
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
