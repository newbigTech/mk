package com.hand.hmall.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.common.util.ThreadLogger;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.pin.dto.PinInfo4SendZmall;
import com.hand.hmall.pin.service.IPinService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name com.hand.hmall.job.SendPinInfoToZmallJob
 * @description 推送PIN码到商城
 * @date 2017-08-12
 */
@RemoteJob
public class SendPinInfoToZmallJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPinService pinService;

    @Autowired
    private RestClient restClient;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {


        List<PinInfo4SendZmall> notSyncPinInfo = pinService.queryNotSyncPinInfo();
        if (notSyncPinInfo.size() == 0) {
            return;
        }
        ThreadLogger threadLogger = new ThreadLogger();
        threadLogger.append("推送PIN码到商城: begin");
        Map<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");

        Response response;
        JSONObject jsonResult;
        StringWriter sw = new StringWriter();
        try {
            new ObjectMapper().writeValue(sw, notSyncPinInfo);
        } catch (IOException e) {
            threadLogger.append("PIN码数据实体转换JSON格式报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("PIN码数据实体转换JSON格式报错。此次job执行异常退出。", e);
        }

        threadLogger.append("开始推送PIN: start");

        try {

            String url = "/zmallsync/pinInfo?token=" + Auth.md5(SecretKey.KEY + sw.toString());

            response = restClient.postString(Constants.ZMALL, url, sw.toString(), "json", new HashMap<>(), contentType);
            threadLogger.append("结束推送PIN: end");
        } catch (IOException e) {
            threadLogger.append("推送PIN时调用Zmall接口报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("推送PIN时调用Zmall接口报错。此次job执行异常退出。", e);
        }


        try {
            jsonResult = JSONObject.fromObject(response.body().string());
            String zMallExeResult = "此次job执行结果 code: " + jsonResult.get("code") + ", message: " + jsonResult.get("message")
                    + ", resp: " + jsonResult.get("resp") + ", total: " + jsonResult.get("total");
            threadLogger.append(zMallExeResult);
        } catch (IOException e) {
            threadLogger.append("将Response字符串转换成JSON格式对象时报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("将Response字符串转换成JSON格式对象时报错。此次job执行异常退出。", e);
        }

        if ("S".equalsIgnoreCase(jsonResult.get("code").toString())) {
            // 将Zmall同步标志位更新为Y
            pinService.updateSynvZmallFlag(true);
            threadLogger.append("将Zmall同步标志位更新为Y, 完成。");
            threadLogger.append("推送PIN码到商城: success");
        } else {
            threadLogger.append("PIN码推送不成功, 不更新Zmall同步标志位。");
            threadLogger.append("推送PIN码到商城: failed");
        }

        logger.info(threadLogger.toString());
        threadLogger.clean();
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }

}
