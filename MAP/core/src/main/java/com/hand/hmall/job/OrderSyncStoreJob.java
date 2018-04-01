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
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderSyncDto;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
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
 * @name OrderSyncStore
 * @description 将SYNCFLAG字段为N的订单信息同步到商城, 同步成功后将 syncflag 字段置为 Y
 * @date 2017年5月26日10:52:23
 */
@RemoteJob
public class OrderSyncStoreJob extends AbstractJob implements RemoteJobTask {

    //job 执行过程状态，E 失败，S 成功
    private static final String PROCESS_STATUS_E = "E";
    private static final String PROCESS_STATUS_S = "S";

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RestClient restClient;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setMessage("No Data Found.");
        logManager.setProgramName(this.getClass().getName());
        logManager.setProgramDescription("订单推送至商城");
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager = logManagerService.logBegin(iRequest, logManager);

        //订单同步商城信息，订单的同步状态为 N
        List<OrderSyncDto> orderDtoList = orderService.selectOrderSyncInfo();
        Map<Long, String> sucessList = new HashMap<>();
        Map<Long, String> failList = new HashMap<>();
        Order order = new Order();
        if (CollectionUtils.isNotEmpty(orderDtoList)) {
            for (OrderSyncDto orderSyncDto : orderDtoList) {
                try {
                    String jsonString = JSON.toJSONString(orderSyncDto);
                    logManager.setMessage("json" + jsonString);

                    String token = Auth.md5(SecretKey.KEY + jsonString);
                    Map<String, String> map = new HashMap<>();
                    map.put("token", token);

                   Response response = restClient.postString(Constants.ZMALL, "/zmallsync/orderSync", jsonString, "json", map, null);
                   JSONObject jsonResult = JSONObject.fromObject(response.body().string());

                    String code = jsonResult.getString("code");
                    String msg = jsonResult.getString("message");

                    if (PROCESS_STATUS_S.equals(code)) {
                        order.setOrderId(orderSyncDto.getOrderId());
                        order = orderService.selectByPrimaryKey(iRequest, order);
                        order.setSyncZmall("Y");
                        orderService.updateByPrimaryKeySelective(iRequest, order);
                        sucessList.put(orderSyncDto.getOrderId(), msg);
                    } else {
                        failList.put(orderSyncDto.getOrderId(), msg);
                    }
                } catch (Exception e) {
                    logManager.setProcessStatus(PROCESS_STATUS_E);
                    logManager.setReturnMessage(e.getMessage());
                    logManagerService.logEnd(iRequest, logManager);
                    throw e;
                }
            }
//           logManager.setProcessStatus(PROCESS_STATUS_S);
            logManager.setReturnMessage("成功：" + sucessList.keySet() + "; 失败：" + failList.entrySet());
            logManagerService.logEnd(iRequest, logManager);
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
