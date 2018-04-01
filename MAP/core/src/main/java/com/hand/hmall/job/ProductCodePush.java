package com.hand.hmall.job;

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.ProductCodeDto;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.Constants;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shoupeng.wei@hang-china.com
 * @version 0.1
 * @name ProductCodePush
 * @description 商品块码推送job , 向商城推送商城需要的块码值
 * @date 2017年5月26日10:52:23
 */
public class ProductCodePush extends AbstractJob{

    //job 执行过程状态，E 失败，S 成功，U Job启动
    private static final String PROCESS_STATUS_E = "E";
    private static final String PROCESS_STATUS_U = "U";

    @Autowired
    private IProductService productService;

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private RestClient restClient;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //启动日志管理
        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setProcessStatus(PROCESS_STATUS_U);
        logManager.setMessage("No Data Found.");
        logManager.setProgramName(this.getClass().getName());
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager.setProgramDescription("商品枚举值推送");
        logManager = logManagerService.logBegin(iRequest, logManager);

        //获取要推送的块码信息（根据商城需要）
        List<ProductCodeDto> productCodeDtoList = productService.selectProductCode();
        if(CollectionUtils.isNotEmpty(productCodeDtoList)){
            try {
                for(ProductCodeDto dto:productCodeDtoList){
                    dto.setType("order");
                }
                String jsonString = JSON.toJSONString(productCodeDtoList);
                String token = Auth.md5(SecretKey.KEY + jsonString);

                logManager.setMessage("json"+jsonString);
                Map<String, String> map = new HashMap<>();
                map.put("token",token);

                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/enum", jsonString, "json", map, null);

                JSONObject jsonResult = JSONObject.fromObject(response.body().string());
                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");

                logManager.setProcessStatus(code);
                logManager.setReturnMessage(msg);

                logManagerService.logEnd(iRequest, logManager);
            } catch (Exception e) {
                logManager.setProcessStatus(PROCESS_STATUS_E);
                logManager.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logManager);
                throw e;
            }

        }
    }
}
