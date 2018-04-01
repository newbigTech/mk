package com.hand.hmall.job;/**
 * Created by peng.chen on 2017/5/16 0016.
 */

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Category;
import com.hand.hmall.mst.dto.SubCategoryDto;
import com.hand.hmall.mst.service.IProductCategoryService;
import com.hand.hmall.mst.service.ISubcategoryService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CategoryToMk
 * @description 商品类别推送
 * @date 2017年5月26日10:52:23
 */
@RemoteJob
public class CategoryToMk extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ISubcategoryService subcategoryService;
    @Autowired
    ILogManagerService logManagerService;
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private RestClient restClient;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager categorylog = new LogManager();
        categorylog.setStartTime(new Date());
        categorylog.setMessage("no data found");
        categorylog.setProgramName(this.getClass().getName());
        categorylog.setProgramDescription("类别推送商城");
        categorylog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        categorylog = logManagerService.logBegin(iRequest, categorylog);

        List<Category> mstProductCategoryList = productCategoryService.getInfo();
        if (mstProductCategoryList != null && mstProductCategoryList.size() > 0) {
            try {
                String jsonString2 = JSON.toJSONString(mstProductCategoryList);
                String token = Auth.md5(SecretKey.KEY + jsonString2);

                categorylog.setMessage("json:" + jsonString2);

                Map<String, String> map = new HashMap<String, String>();
                map.put("token", token);

                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/category", jsonString2, "json", map, null);

                JSONObject jsonResult = JSONObject.fromObject(response.body().string());
                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object category : errorList) {
                        errorIds.add(Long.valueOf(category.toString()));
                    }
                }

                msg = msg + ",返回错误id为" + errorIds.toString();
                categorylog.setProcessStatus(code);
                categorylog.setReturnMessage(msg);
                logManagerService.logEnd(iRequest, categorylog);
            } catch (Exception e) {
                categorylog.setProcessStatus("E");
                categorylog.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, categorylog);
                logger.error(e.getMessage());
            }
        }

        //子类别&类别映射
        LogManager logsub = new LogManager();
        logsub.setStartTime(new Date());
        logsub.setMessage("no data found");
        logsub.setProgramName(this.getClass().getName());
        logsub.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logsub.setProgramDescription("子类别&类别映射推送商城");
        logsub = logManagerService.logBegin(iRequest, logsub);

        List<SubCategoryDto> subCategories = subcategoryService.querySyncData();
        if (subCategories != null && subCategories.size() > 0) {
            try {
                String jsonString2 = JSON.toJSONString(subCategories);
                String token = Auth.md5(SecretKey.KEY + jsonString2);

                logsub.setMessage("json:" + jsonString2);

                Map<String, String> map = new HashMap<String, String>();
                map.put("token", token);

                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/subCategory", jsonString2, "json", map, null);
                JSONObject jsonResult = JSONObject.fromObject(response.body().string());

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object subCategory : errorList) {
                        errorIds.add(Long.valueOf(subCategory.toString()));
                    }
                }

                msg = msg + ",返回错误id为" + errorIds.toString();
                logsub.setProcessStatus(code);
                logsub.setReturnMessage(msg);
                logManagerService.logEnd(iRequest, logsub);
            } catch (Exception e) {
                logsub.setProcessStatus("E");
                logsub.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logsub);
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
