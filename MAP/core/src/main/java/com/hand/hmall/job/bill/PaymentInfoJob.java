package com.hand.hmall.job.bill;

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.dto.QueryData;
import com.hand.hmall.om.service.IPaymentinfoService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hmall.job
 * @Description
 * @date 2017/7/28
 */
@RemoteJob
public class PaymentInfoJob extends AbstractJob implements RemoteJobTask {

    @Autowired
    private IPaymentinfoService paymentinfoService;
    @Autowired
    private ILogManagerService logManagerService;
    @Autowired
    private RestClient restClient;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager logManager = new LogManager();
        logManager.setStartTime(new Date());
        logManager.setMessage("No Data Found.");
        logManager.setProgramName(this.getClass().getName());
        logManager.setProgramDescription("银联清算时间更新JOB");
        logManager.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        logManager = logManagerService.logBegin(iRequest, logManager);

        //获取payMode=UNION settleTime=null的数据
        List<Paymentinfo> paymentinfos = paymentinfoService.selectPaymentsByModeWithSettleTimeIsNull(new Paymentinfo("UNIONPAY"));
        HashMap<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");
        if (CollectionUtils.isNotEmpty(paymentinfos)) {
            try {
                for (Paymentinfo pi : paymentinfos) {
                    if (pi != null) {
                        String outTradeNo = pi.getOutTradeNo();
                        String mode = pi.getPayMode();
                        Date payTime = pi.getPayTime();
                        //调用hpay查询接口，查询其对账日期
                        String queryData = JSON.toJSONString(new QueryData(mode, "PAY", outTradeNo, new SimpleDateFormat("yyyyMMddHHmmss").format(payTime)));
                        logManager.setMessage("json" + queryData);
                        Response response = restClient.postString(Constants.HPAY, "/hpay/v1/Query", queryData, "json", null, contentType);
                        JSONObject jsonResult = JSONObject.fromObject(response.body().string());
                        String success = jsonResult.getString("success");
                        //更新数据库信息
                        if ("true".equalsIgnoreCase(success)) {
                            //取得银联查询到的数据
                            List<Map> list = (List<Map>) jsonResult.get("resp");
                            //获得清算日期
                            String settleDateFromUnion = (String) list.get(0).get("settleDate");
                            //将日期转换换位标准格式MMdd
                            DateFormat dateFormat = new SimpleDateFormat("MMdd");
                            Date payTimeDate = dateFormat.parse(dateFormat.format(payTime));
                            Date settleTime = dateFormat.parse(settleDateFromUnion);
                            //定义年份，默认为当年
                            Calendar calendar = Calendar.getInstance();
                            String year = String.valueOf(calendar.get(Calendar.YEAR));
                            //当支付时间大于清算时间则代表，支付时间在12.31日23：00之后，清算时间是次年的0101
                            if (payTimeDate.getTime() > settleTime.getTime()) {
                                //将年份+1,为支付年份的第二年
                                Integer yearInt = calendar.get(Calendar.YEAR) + 1;
                                year = String.valueOf(yearInt);
                            }
                            //将日期转换为年月日的格式
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            String date = year + settleDateFromUnion;
                            Date settleDate = sdf.parse(date);
                            pi.setSettleTime(settleDate);
                            //更新数据库
                            paymentinfoService.updateByPrimaryKeySelective(iRequest, pi);
                            logManager.setProcessStatus("S");
                            logManager.setMessage("更新成功");
                            logManager.setReturnMessage("成功");
                            logManagerService.logEnd(iRequest, logManager);
                        }
                    }
                }
            } catch (Exception e) {
                logManager.setMessage("更新失败");
                logManager.setProcessStatus("E");
                logManager.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, logManager);
                e.printStackTrace();
            }
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
