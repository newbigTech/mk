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
import com.hand.hmall.mst.dto.PricerowDto;
import com.hand.hmall.mst.service.IPricerowService;
import com.hand.hmall.om.dto.OmDiscountEntry;
import com.hand.hmall.om.service.IOmDiscountEntryService;
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

import java.math.BigDecimal;
import java.util.*;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送销售类型的价格行信息至商城
 * @date 2017/6/27 13:42
 */
@RemoteJob
public class PricerowInterfaceJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IPricerowService pricerowService;

    @Autowired
    private RestClient client;

    @Autowired
    private IOmDiscountEntryService omDiscountEntryService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //推送零售类型的商品价格行信息接口
        LogManager pricerowlog = new LogManager();
        pricerowlog.setStartTime(new Date());
        pricerowlog.setMessage("no data found");
        pricerowlog.setProgramName(this.getClass().getName());
        pricerowlog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        pricerowlog.setProgramDescription("商品价格行推送商城");
        pricerowlog = logManagerService.logBegin(iRequest, pricerowlog);
        //获取符合规定的原价类型的价格行信息
        List<PricerowDto> priceRowList = pricerowService.selectPushingPricerow();
        if (priceRowList != null && priceRowList.size() > 0) {
            for (PricerowDto pricerowDto : priceRowList) {
             /*   //遍历每个原价类型的价格行，通过productId去查找是否含有促销价的价格行信息
                List<PricerowDto> list = pricerowService.selectDiscountPricerow(pricerowDto);*/

                //根据商品ID查找折扣行 是否有该商品，有且is_head=Y（启用状态），则取对应折扣金额（折扣类型为3：商品固定价格）
                OmDiscountEntry condition = new OmDiscountEntry();
                condition.setProductId(pricerowDto.getProductId());
                List<OmDiscountEntry> list = omDiscountEntryService.queryDiscountEntryByProductIdAndDiscountType(condition);
                if (list != null && list.size() > 0) {
                    //若存在促销价格
                    pricerowDto.setBottomPrice(new BigDecimal(list.get(0).getDiscount()));
                    pricerowDto.setPriceStartTime(list.get(0).getStartTime());
                    pricerowDto.setPriceEndTime(list.get(0).getEndTime());
                    pricerowDto.setPriceType("special");
                } else {
                    //若不存在促销价格
                    pricerowDto.setBottomPrice(pricerowDto.getBasePrice());
                    pricerowDto.setPriceType("");
                }
            }

            try {
                String jsonString = JSON.toJSONString(priceRowList);
                pricerowlog.setMessage("json:" + jsonString);

                String token = Auth.md5(SecretKey.KEY + jsonString);
                Map<String, String> map = new HashMap<String, String>();
                map.put("token", token);
                logger.info("价格行======================" + jsonString);
                //发送请求并得到响应信息
                Response response = client.postString(Constants.ZMALL, "/zmallsync/priceRow", jsonString, "json", map, null);
                JSONObject jsonResult = JSONObject.fromObject(response.body().string());

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object pricerow : errorList) {
                        errorIds.add(Long.valueOf(pricerow.toString()));
                    }
                }

                msg = msg + ",返回错误id为" + errorIds.toString();
                pricerowlog.setProcessStatus(code);
                pricerowlog.setReturnMessage(msg);
                logManagerService.logEnd(iRequest, pricerowlog);
            } catch (Exception e) {
                pricerowlog.setProcessStatus("E");
                pricerowlog.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, pricerowlog);
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
