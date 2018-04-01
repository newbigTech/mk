package com.hand.hmall.job;

import com.hand.common.job.JobConcurrentManager;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.service.IConsignmentService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name CanBeShippedJudgeJob
 * @description 全款可发运判断
 * @date 2017/8/29 9:07
 */
@RemoteJob
public class CanBeShippedJudgeJob extends AbstractJob implements RemoteJobTask {

    private static final String JOB_DESC = "全款可发运判断job";
    Logger logger = LoggerFactory.getLogger(CanBeShippedJudgeJob.class);
    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private JobConcurrentManager jobConcurrentManager;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

        //排斥订单推送retail job的业务redis锁
        jobConcurrentManager.beginWithLock("job_toRetail_mutex_canBeShiped");
        logger.info("start CanBeShippedJudgeJob " + formatter.format(new Date(System.currentTimeMillis())) + ">>>>:" + Thread.currentThread().getName());

        try {

            iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);

            iConsignmentService.judgeCanBeShipped();

            iLogManagerService.logSuccess(this.getClass(), JOB_DESC, null, JOB_DESC + "完成");

        } catch (Exception e) {
            iLogManagerService.log(this.getClass(), JOB_DESC, null, e.getMessage(), JOB_DESC, "异常");
        } finally {
            jobConcurrentManager.finish("job_toRetail_mutex_canBeShiped");
            logger.info("release lock CanBeShippedJudgeJob " + formatter.format(new Date(System.currentTimeMillis())) + ">>>>:" + Thread.currentThread().getName());
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
