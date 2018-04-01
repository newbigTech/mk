package com.hand.hmall.job;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * @author XinyangMei
 * @Title CouponSynJob
 * @Description 优惠券推送商城job
 * @date 2017/7/31 10:18
 */
@RemoteJob
public class CouponSynJob extends AbstractJob implements RemoteJobTask {

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
        logManager.setProgramDescription("优惠券推送接口");
        logManager = logManagerService.logBegin(iRequest, logManager);
        try {
            Map<String, String> map = new HashMap<String, String>();
            logger.info("-------getCoupon Uri--------" + baseUri + modelUri);
            String jsonString = getCouponData(baseUri, modelUri);
            logger.info("------------josnString---------------- \n" + jsonString);
            if (StringUtils.isEmpty(jsonString)) {
                logManager.setProcessStatus("S");
                logManager.setReturnMessage("没有需要推送的优惠券数据");
                logManagerService.logEnd(iRequest, logManager);
                return;
            } else {
                String token = Auth.md5(SecretKey.KEY + jsonString);
                map.put("token", token);
                logger.info("----------token----------" + token);
                logManager.setMessage(jsonString + "----" + token);
                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/coupon", jsonString, "json", map, null);
                logger.info("----------resp-----" + JSON.toJSONString(response));
                String respStr = response.body().string();
                logger.info("----------resp.body.string-----" + respStr);
                JSONObject respObj = JSONObject.parseObject(respStr);
                String code = respObj.getString("code");
                if ("S".equalsIgnoreCase(code.trim())) {
                    JSONArray resp = respObj.getJSONArray("resp");
                    {

                        List<Map> list = new ArrayList<>();
                        JSONArray jsonArray = JSONArray.parseArray(jsonString);
                        for (Object o : jsonArray) {
                            JSONObject obj = (JSONObject) o;
                            Map objMap = new HashMap();
                            if (!resp.contains(obj.getString("couponCode") + "&&" + obj.getString("releaseId"))) {
                                objMap.put("couponCode", obj.getString("couponCode"));
                                objMap.put("releaseId", obj.getString("releaseId"));
                                list.add(objMap);
                            }
                        }
                        setIsSyn(list);
                    }
                    logger.info("--------" + JSON.toJSONString(response));
                    if (resp.isEmpty()) {
                        logManager.setProcessStatus("S");
                        logManager.setReturnMessage(respObj.toJSONString());
                    } else {
                        logManager.setProcessStatus("E");
                        logManager.setReturnMessage("部分成功" + respObj.toJSONString());
                    }

                    logManagerService.logEnd(iRequest, logManager);
                } else {
                    logger.info("--------" + JSON.toJSONString(response));
                    logManager.setProcessStatus("E");
                    logManager.setReturnMessage(respObj.toJSONString());
                    logManagerService.logEnd(iRequest, logManager);
                }

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
    public String getCouponData(String baseUri, String modelUri) throws IOException {

        String url = "/h/sale/operator/getSynCoupon";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, null);
        logger.info("---------------url------------" + baseUri + modelUri + url);
        ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
        JSONArray couponArray = new JSONArray();
        List coupons = responseData.getResp();
        for (int i = 0; i < coupons.size(); i++) {
            Map coupon = (Map) coupons.get(i);
            JSONObject couponObj = new JSONObject();
            couponObj.put("couponCode", coupon.get("couponCode"));
            couponObj.put("couponType", coupon.get("type"));
            couponObj.put("couponName", coupon.get("couponName"));
            couponObj.put("couponDescription", coupon.get("couponDes"));
            couponObj.put("allowGetStartTime", coupon.get("getStartDate"));
            couponObj.put("allowGetEndTime", coupon.get("getEndDate"));
            couponObj.put("allowUseStartTime", coupon.get("startDate"));
            couponObj.put("allowUseEndTime", coupon.get("endDate"));
            couponObj.put("discountType", coupon.get("discountType"));
            if ("COUPON_TYPE_04)".equals(coupon.get("type"))) {
                couponObj.put("allowShow", "1");
            } else {
                couponObj.put("allowShow", "0");
            }

            String status = coupon.get("status").toString();
            if ("ACTIVITY".equals(status.trim())) {
                couponObj.put("effective", "1");
            } else {
                couponObj.put("effective", "0");
            }
            couponObj.put("releaseId", coupon.get("releaseId"));
            couponObj.put("payAmount", coupon.get("payFee"));
            couponArray.add(couponObj);
        }
        if (couponArray.isEmpty()) {
            return null;
        }
        return couponArray.toJSONString();
    }

    private void setIsSyn(List<Map> list) {
        String url = "/h/sale/operator/setSynCoupon";
        HttpEntity<List<Map>> entity = new HttpEntity(list, null);
        logger.info("---------------url------------" + baseUri + modelUri + url);
        ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
