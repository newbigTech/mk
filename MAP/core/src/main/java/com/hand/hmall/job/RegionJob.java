package com.hand.hmall.job;/**
 * Created by peng.chen on 2017/5/10 0010.
 */

import com.alibaba.fastjson.JSON;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.util.Constants;
import com.markor.map.external.fndregionservice.dto.RegionB;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import com.markor.map.fndregionservice.dto.RegionTl;
import com.markor.map.fndregionservice.service.IRegionService;
import com.markor.map.fndregionservice.service.IRegionTlService;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import com.markor.map.framework.restclient.RestClient;
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
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name RegionJob
 * @description 地区推送JOB, 将区域相关的信息推送到ZMALL，推送成功后，会将同步标识字段 sync 置为 Y
 * @date 2017年5月26日10:52:23
 */
@RemoteJob
public class RegionJob extends AbstractJob implements RemoteJobTask {

    //job 执行过程状态，E 失败，S 成功，U Job启动
    private static final String PROCESS_STATUS_E = "E";
    private static final String PROCESS_STATUS_S = "S";
    private static final String PROCESS_STATUS_U = "U";


    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    @Autowired
    private IRegionService iRegionService;

    @Autowired
    private IRegionTlService iRegionTlService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ILogManagerService logManagerService;


    /**
     * 地区推送
     *
     * @param jobExecutionContext job上下文
     * @throws Exception 抛出代码执行过程中可能出现的所有异常
     */
    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //记录区域 region 推送过程的日志对象
        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setProcessStatus(PROCESS_STATUS_U);
        log.setMessage("no data found");
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        log = logManagerService.logBegin(iRequest, log);

        //记录区域 regionTl 推送过程的日志对象
        LogManager logt = new LogManager();
        logt.setStartTime(new Date());
        logt.setProcessStatus(PROCESS_STATUS_U);
        logt.setMessage("no data found");
        logt.setProgramName(this.getClass().getName());
        logt.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logt = logManagerService.logBegin(iRequest, logt);

        //查询要同步的数据（未同步给Zmall的数据）,其中regionBList 区域信息，regionTList 区域的描述信息，区域名称信息（中英文）
        List<RegionB> regionBList = iFndRegionsCommonExternalService.getRegionAll();
        List<RegionTl> regionTList = iRegionTlService.queryAll();

        if (CollectionUtils.isNotEmpty(regionBList)) {
            try {
                String jsonString2 = JSON.toJSONString(regionBList);
                String token = Auth.md5(SecretKey.KEY + jsonString2);

                log.setMessage("json:" + jsonString2);

                Map<String, String> map = new HashMap<>();
                map.put("token", token);

                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/regionB", jsonString2, "json", map, null);

                JSONObject jsonResult = JSONObject.fromObject(response.body().string());
                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                log.setProcessStatus(code);
                log.setReturnMessage(msg);
                if (PROCESS_STATUS_S.equals(code)) {
                    iFndRegionsCommonExternalService.updateRegionB(regionBList);
                }
                logManagerService.logEnd(iRequest, log);
            } catch (Exception e) {
                log.setProcessStatus(PROCESS_STATUS_E);
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                throw e;
            }
        }

        if (CollectionUtils.isNotEmpty(regionTList)) {
            try {
                String jsonString2 = JSON.toJSONString(regionTList);
                String token = Auth.md5(SecretKey.KEY + jsonString2);
                Map<String, String> map = new HashMap<>();
                logt.setMessage("json:" + jsonString2 + "token:" + token);
                map.put("token", token);

                Response response = restClient.postString(Constants.ZMALL, "/zmallsync/regionT", jsonString2, "json", map, null);


                JSONObject jsonResult = JSONObject.fromObject(response.body().string());
                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");

                logt.setReturnMessage(msg);
                if (PROCESS_STATUS_S.equals(code)) {
                    logt.setProcessStatus(code);
                    iFndRegionsCommonExternalService.updateRegionB(regionBList);
                }
                logManagerService.logEnd(iRequest, logt);

                iRegionTlService.updateRegionTl(regionTList);
            } catch (Exception e) {
                logt.setProcessStatus(PROCESS_STATUS_E);
                logt.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logt);
                throw e;
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
