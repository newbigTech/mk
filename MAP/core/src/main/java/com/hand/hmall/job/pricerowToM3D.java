package com.hand.hmall.job;

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.PricerowDto;
import com.hand.hmall.mst.service.IPricerowService;
import com.hand.hmall.util.Constants;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送定制商品下的价格行信息到M3D
 * @date 2017/7/4 15:50
 */
public class pricerowToM3D extends AbstractJob {

    @Autowired
    ILogManagerService logManagerService;

    @Autowired
    private IPricerowService pricerowService;

    @Autowired
    private RestClient client;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //推送定制类型的商品价格行信息接口
        LogManager pricerowlog = new LogManager();
        pricerowlog.setStartTime(new Date());
        pricerowlog.setMessage("no data found");
        pricerowlog.setProgramName(this.getClass().getName());
        pricerowlog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        pricerowlog.setProgramDescription("商品价格行推送M3D");
        pricerowlog = logManagerService.logBegin(iRequest, pricerowlog);

        //获取符合规定的原价类型的价格行信息
        List<PricerowDto> priceRowList = pricerowService.pushPricerowToM3D();
        if (priceRowList != null && priceRowList.size() > 0) {
            for (PricerowDto pricerowDto : priceRowList) {
                //遍历每个原价类型的价格行，通过productId去查找是否含有促销价的价格行信息
                List<PricerowDto> list = pricerowService.selectDiscountPricerow(pricerowDto);
                if (list != null && list.size() > 0) {
                    //若存在促销价格
                    pricerowDto.setActivefy_price(list.get(0).getBasePrice());
                    pricerowDto.setActivefy_start_time(list.get(0).getPriceStartTime());
                    pricerowDto.setActivefy_end_time(list.get(0).getPriceEndTime());
                } else {
                    pricerowDto.setActivefy_price(pricerowDto.getBase_price());
                }

            }

            try {
                String jsonString2 = JSON.toJSONString(priceRowList);

                pricerowlog.setMessage("json:" + jsonString2);

                Response response = client.postString(Constants.M3D, "/modules/strawberry/webservice/priceAccept.php", jsonString2, "json", null, null);
                JSONObject jsonResult = JSONObject.fromObject(response.body().string());

                int err = jsonResult.getInt("err");
                String errmsg = jsonResult.getString("errmsg");
                //String time = jsonResult.getString("time");

                if (err == 200) {
                    pricerowlog.setProcessStatus("成功");
                } else {
                    pricerowlog.setProcessStatus("失败");
                }
                pricerowlog.setReturnMessage(errmsg);
                logManagerService.logEnd(iRequest, pricerowlog);
            } catch (Exception e) {
                pricerowlog.setProcessStatus("E");
                pricerowlog.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, pricerowlog);
                throw e;
            }

        }

    }
}
