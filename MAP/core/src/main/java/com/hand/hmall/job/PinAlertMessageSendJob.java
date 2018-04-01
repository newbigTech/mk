package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hmall.pin.service.IPinAlterInfoService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * author: zhangzilong
 * name: PinAlertMessageSendJob.java
 * discription: Pin码警报短信发送Job
 * date: 2017/11/20
 * version: 0.1
 */
@RemoteJob
public class PinAlertMessageSendJob implements RemoteJobTask {


    @Autowired
    private IPinAlterInfoService iPinAlterInfoService;

    @Override
    public JobResult execute() throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        iPinAlterInfoService.sendMsg(iRequest);
        return JobResult.success(null);
    }
}
