package com.hand.hmall.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.MstBundles;
import com.hand.hmall.mst.dto.MstBundlesMapping;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IMstBundlesMappingService;
import com.hand.hmall.mst.service.IMstBundlesService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.Constants;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hmall.job
 * @Description 推送套装至商城
 * @date 2017/8/30
 */
@RemoteJob
public class BundlesSynJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private IMstBundlesService iMstBundlesService;

    @Autowired
    private IMstBundlesMappingService iMstBundlesMappingService;

    @Autowired
    private IProductService iProductService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setProgramName(this.getClass().getName());
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager.setProgramDescription("套装推送商城");
        logManager = logManagerService.logBegin(iRequest, logManager);
        try {

            MstBundlesMapping mstBundlesMapping = new MstBundlesMapping();
            //查询hmall_mst_bundles SYNC_FLAG=N
            MstBundles mstBundles = new MstBundles();
            mstBundles.setSyncFlag("N");
            ArrayList<MstBundles> bundlesListWithSyncFlagN = iMstBundlesService.selectBundlesBySyncFlag(mstBundles);
            ArrayList<HashMap<String, Object>> requestZmallBundlesList = new ArrayList<>();
            if (bundlesListWithSyncFlagN != null) {
                for (MstBundles bundles : bundlesListWithSyncFlagN) {
                    HashMap<String, Object> requestZmallBundleMap = new HashMap<>();
                    requestZmallBundleMap.put("bundlesId", bundles.getBundlesId());
                    // 套装编号
                    requestZmallBundleMap.put("suitCode", bundles.getCode());
                    // 套装价格
                    requestZmallBundleMap.put("price", bundles.getPrice());
                    // 套装名称
                    requestZmallBundleMap.put("suitName", bundles.getName());
                    // 套装状态，0未启用 1启用
                    if ("ACTIVITY".equals(bundles.getStatus())) {
                        requestZmallBundleMap.put("status", 1);
                    } else {
                        requestZmallBundleMap.put("status", 0);
                    }
                    //遍历数据
                    mstBundlesMapping.setBundlesId(bundles.getBundlesId());
                    ArrayList<MstBundlesMapping> bundlesMappingListByBundleId = iMstBundlesMappingService.selectBundlesMappingByBundlesId(mstBundlesMapping);
                    ArrayList<HashMap<String, Object>> requestZmallSuitProductsList = new ArrayList<>();
                    if (bundlesMappingListByBundleId != null) {
                        for (MstBundlesMapping mbm : bundlesMappingListByBundleId) {
                            HashMap<String, Object> requestZmallSuitProductMap = new HashMap<>();
                            Product product = iProductService.selectByProductId(mbm.getProductId());
                            // 套装产品编号
                            requestZmallSuitProductMap.put("productCode", product.getCode());
                            // 套装产品数量
                            requestZmallSuitProductMap.put("productNumber", mbm.getQuantity());
                            requestZmallSuitProductsList.add(requestZmallSuitProductMap);
                        }
                    } else {
                        HashMap<String, Object> requestZmallSuitProductMap = new HashMap<>();
                        // 套装产品编号
                        requestZmallSuitProductMap.put("productCode", null);
                        // 套装产品数量
                        requestZmallSuitProductMap.put("productNumber", null);
                        requestZmallSuitProductsList.add(requestZmallSuitProductMap);
                    }
                    requestZmallBundleMap.put("suitProducts", requestZmallSuitProductsList);
                    requestZmallBundlesList.add(requestZmallBundleMap);
                }
            } else {
                logManager.setProcessStatus("S");
                logManager.setReturnMessage("没有需要推送的套装数据");
                logManagerService.logEnd(iRequest, logManager);
                return;
            }
            String jsonString = JSON.toJSONString(requestZmallBundlesList);
            //推送zmall
            HashMap<String, String> paramsMap = new HashMap<>();
            String token = Auth.md5(SecretKey.KEY + jsonString);
            paramsMap.put("token", token);
            logManager.setMessage(jsonString);
            logger.info(jsonString);
            logger.info(token);
            Response response = restClient.postString(Constants.ZMALL, "/zmallsync/suitProduct", jsonString, "json", paramsMap, null);
            //更新SYNC_FLAG=Y
            JSONObject responseFromZmall = JSON.parseObject(response.body().string());
            String code = responseFromZmall.getString("code");
            String msg = responseFromZmall.getString("message");
            logger.info("-------------code-" + code);
            logger.info("-------------msg-" + msg);
            //返回状态码为S
            if ("S".equalsIgnoreCase(code)) {
                //错误信息不为空，为部分成功
                if (!responseFromZmall.getJSONArray("resp").isEmpty()) {
                    JSONArray errorList = responseFromZmall.getJSONArray("resp");
                    if (CollectionUtils.isNotEmpty(bundlesListWithSyncFlagN)) {
                        for (MstBundles mstb : bundlesListWithSyncFlagN) {
                            if (!errorList.contains(mstb.getBundlesId())) {
                                mstb.setSyncFlag("Y");
                            }
                            logManager.setProcessStatus("E");
                            logManager.setReturnMessage("部分成功" + JSON.toJSONString(responseFromZmall));
//                            logManagerService.logEnd(iRequest, logManager);
                        }
                    }

                } else {
                    for (MstBundles mstb : bundlesListWithSyncFlagN) {
                        mstb.setSyncFlag("Y");
                    }
                    logManager.setProcessStatus("S");
                    logManager.setReturnMessage(JSON.toJSONString(requestZmallBundlesList));
//                    logManagerService.logEnd(iRequest, logManager);
                }
                logger.info(bundlesListWithSyncFlagN.toString());
                bundlesListWithSyncFlagN.forEach(bundles -> iMstBundlesService.updateByPrimaryKeySelective(iRequest, bundles));
                logManager.setProcessStatus("S");
                logManager.setReturnMessage(JSON.toJSONString(response));
                logManagerService.logEnd(iRequest, logManager);
            } else {
                //推送失败
                logger.info("推送失败");
                logger.info(bundlesListWithSyncFlagN.toString());
                logManager.setProcessStatus("E");
                logManager.setReturnMessage(JSON.toJSONString(responseFromZmall));
                logManagerService.logEnd(iRequest, logManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logManager.setProcessStatus("E");
            logManager.setReturnMessage("exctepion info:\n" + e.getMessage());
            logManagerService.logEnd(iRequest, logManager);
            e.printStackTrace();
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
