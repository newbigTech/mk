package com.hand.hmall.job;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.StringUtils;
import com.markor.map.framework.restclient.RestClient;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title PromoteActivitySynJob
 * @Description 促销活动推送job
 * @date 2017/10/21 15:45
 */
@RemoteJob
public class PromoteActivitySynJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private ILogManagerService logManagerService;
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private RestClient restClient;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 执行推送商城
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
        logManager.setMessage("No Data Found.");
        logManager.setProgramName(this.getClass().getName());
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager.setProgramDescription("促销活动推送");
        logManager = logManagerService.logBegin(iRequest, logManager);
        try {
            Map<String, String> map = new HashMap<String, String>();
            logger.info("-------getSynActivity Uri--------" + baseUri + modelUri);
            String jsonString = getActivityData(baseUri, modelUri);
            logger.info("------------josnString---------------- \n" + jsonString);
            if (StringUtils.isEmpty(jsonString)) {
                logManager.setProcessStatus("S");
                logManager.setReturnMessage("没有需要推送的促销活动数据");
                logManagerService.logEnd(iRequest, logManager);
                return;
            } else {
                String token = Auth.md5(SecretKey.KEY + jsonString);
                map.put("token", token);
                logger.info("----------token----------" + token);
                logManager.setMessage(jsonString + "----" + token);
                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/activity", jsonString, "json", map, null);
                JSONObject respObj = JSON.parseObject(response.body().string());
                logger.info("----------resp-----" + JSON.toJSONString(respObj));
                logManager.setProcessStatus(respObj.getString("code"));
                logManager.setReturnMessage(respObj.toJSONString());
                logManagerService.logEnd(iRequest, logManager);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logManager.setProcessStatus("E");
            logManager.setReturnMessage("exctepion info:\n" + e.getMessage());
            logManagerService.logEnd(iRequest, logManager);
        }
    }

    /**
     * 获取推送数据
     *
     * @param baseUri
     * @param modelUri
     * @return
     * @throws IOException
     */
    public String getActivityData(String baseUri, String modelUri) throws IOException {

        String url = "hmall-drools-service/h/sale/activity/queryForZmall";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, null);
        logger.info("---------------url------------" + baseUri + url);
        ResponseData responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
        List activitys = responseData.getResp();
        if (CollectionUtils.isEmpty(activitys)) {
            return null;
        } else {
            return JSON.toJSONString(activitys);

        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }

}
