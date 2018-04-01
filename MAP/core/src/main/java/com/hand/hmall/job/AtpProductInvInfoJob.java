package com.hand.hmall.job;

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.atp.dto.AtpProductInvInfo;
import com.hand.hap.atp.dto.AtpProductInvInfoDto;
import com.hand.hap.atp.service.IAtpProductInvInfoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Basestore;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IBasestoreService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @packgeName: com.hand.hmall.job
 * @ClassName: AtpProductInvInfoJob
 * @copyright: Copyright 2016-2027 Markor Investment Group Co. LTD. All Rights Reserved.
 * @description:<描述>
 * @author: litongjie
 * @date: 2017/6/21-16:34
 * @version: 1.0
 * @since: JDK 1.8
 */
@RemoteJob
public class AtpProductInvInfoJob extends AbstractJob implements RemoteJobTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ILogManagerService logManagerService;
    @Value("#{configProperties['hmall.syncData.uri']}")
    private String baseUri;
    @Autowired
    private RestClient client;
    @Autowired
    private IProductService productService;
    @Autowired
    private IAtpProductInvInfoService service;
    @Autowired
    private IBasestoreService basestoreService;

    /**
     * 运行时所有的异常将会被自动处理.
     *
     * @param context Job运行时的上下文。
     * @throws Exception 运行时异常或实现子类主动抛出的异常
     */
    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setMessage("no data found");
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        log.setProgramDescription("常规品库存推送商城");
        log = logManagerService.logBegin(iRequest, log);

        //查找online版本并且为appvove状态商品
        String catalogVersion = Constants.CATALOG_VERSION;
        String catalog = Constants.CATALOG;
        String approval_status = Constants.APPROVAL_STATUS_APPROVED;
        Map<String, Object> proMap = new HashMap<>();
        proMap.put("catalog", catalog);
        proMap.put("catalogVersion", catalogVersion);
        proMap.put("approval_status", approval_status);

        List<String> customType = new ArrayList<String>();
        customType.add("A4");
        proMap.put("customType", customType);

        List<Product> productList = productService.selectProductByOptionMap(proMap);

        //查询商品库存
        Map<String, Object> productCodes = new HashMap<>();
        List<String> codes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productList)) {
            for (Product product : productList) {
                codes.add(product.getCode());
            }
        }
        productCodes.put("productCodes", codes);
        List<AtpProductInvInfo> stockList = service.selectAtpProductInvInfoByMap(productCodes);

        if (CollectionUtils.isNotEmpty(stockList)) {
            List<AtpProductInvInfoDto> result = converterDTO(iRequest, stockList);
            try {
                String jsonString = JSON.toJSONString(result);
                log.setMessage("json:" + jsonString);

                String token = Auth.md5(SecretKey.KEY + jsonString);
                //请求参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("token", token);
                System.out.println("token: ====" + token);

                //请求头信息
                Map<String, String> head = new HashMap<String, String>();

                Response response = client.postString(Constants.ZMALL, "/zmallsync/productStock", jsonString, "json", map, head);
                JSONObject jsonResult = JSONObject.fromObject(response.body().string());

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<String> errorCodes = new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(errorList)) {
                    for (Object c : errorList) {
                        errorCodes.add(String.valueOf(c));
                    }
                }
                //获取成功推送商城的库存code
                List<AtpProductInvInfoDto> successCode = result.stream()
                        .filter(atpProductInvInfoDTO -> !errorCodes.contains(atpProductInvInfoDTO.getCode()))
                        .collect(Collectors.toList());

                msg = msg + ",返回错误code为" + errorCodes.toString();
                log.setProcessStatus(code);
                log.setReturnMessage(msg);
                logManagerService.logEnd(iRequest, log);
            } catch (Exception e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                logger.error(e.getMessage());
            }
        }

    }

    /**
     * 转换dto便于商城使用
     *
     * @param list
     * @return
     */
    private List<AtpProductInvInfoDto> converterDTO(IRequest iRequest, List<AtpProductInvInfo> list) {
        List<AtpProductInvInfoDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            //查询商城对应店铺的库存分配比例和安全库存阈值
            //因目前店铺只有京东一家，故只查出第一个
            Basestore basestore = basestoreService.selectAll(iRequest).get(0);

            for (AtpProductInvInfo atp : list) {
                AtpProductInvInfoDto dto = new AtpProductInvInfoDto();
                dto.setCode(atp.getMatnr());
                if (atp.getTotalAvailableQuantity() != null) {
                    double d = atp.getTotalAvailableQuantity().doubleValue() * basestore.getInventoryRatio().longValue();
                    if (d > basestore.getStockTheshold().doubleValue()) {
                        dto.setStockNum(Double.valueOf(atp.getTotalAvailableQuantity()));
                    } else {
                        dto.setStockNum(0D);
                    }
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
