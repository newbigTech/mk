package com.hand.hmall.job;

import com.alibaba.fastjson.JSON;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.*;
import com.hand.hmall.mst.service.*;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaoli.yu
 * @version 创建时间：2017年5月16日 下午5:15:51
 * @description 商品推送商城job
 */
@RemoteJob
public class ProductInterfaceJob extends AbstractJob implements RemoteJobTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ICategoryMappingService iCategoryMappingService;
    @Autowired
    private ILogManagerService iLogManagerService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ISuitlineMappingService suitlineMappingService;
    @Autowired
    private IPatchlineMappingService patchlineMappingService;
    @Autowired
    private RestClient restClient;
    @Autowired
    private IOdtypeService odtypeService;
    @Autowired
    private IMediaService iMediaService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        //推送商品信息
        LogManager productlog = new LogManager();
        productlog.setStartTime(new Date());
        productlog.setMessage("no data found");
        productlog.setProgramName(this.getClass().getName());
        productlog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        productlog.setProgramDescription("商品信息推送");
        productlog = iLogManagerService.logBegin(iRequest, productlog);
        List<ProductDto> productList = iProductService.selectOnlineProduct();
        if (productList != null && productList.size() > 0) {
            //如果ESC_NAME不为空，则取ESC_NAME中的值，如ESC_NAME为空，则取name 商品名称
            for (ProductDto productDto : productList) {
                if (productDto != null) {
                    if (StringUtil.isNotEmpty(productDto.getEscName())) {
                        productDto.setName(productDto.getEscName());
                    }
                    //是否定制为空的时候，推送0
                    if(productDto.getHascustom()==null){
                        productDto.setHascustom("0");
                    }
                }
            }
            try {
                String jsonString = JSON.toJSONString(productList);
                JSONObject jsonResult = getResponse(jsonString, productlog, "/zmallsync/product");

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object product : errorList) {
                        errorIds.add(Long.valueOf(product.toString()));
                    }
                }
                //获取商城成功获得的数据
                List<ProductDto> successProducts = productList.stream()
                        .filter(product -> !errorIds.contains(product.getProductId()))
                        .collect(Collectors.toList());

                //商品&类别映射
                if (CollectionUtils.isNotEmpty(successProducts)) {
                    LogManager logMapping = new LogManager();
                    logMapping.setStartTime(new Date());
                    logMapping.setMessage("no data found");
                    logMapping.setProgramName(this.getClass().getName());
                    logMapping.setProgramDescription("类别商品关系推送商城");
                    logMapping.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
                    logMapping = iLogManagerService.logBegin(iRequest, logMapping);

                    List<CategoryMappingDto> categoryMappings = iCategoryMappingService.querySyncData(successProducts);
                    if (categoryMappings != null && categoryMappings.size() > 0) {
                        try {
                            String categoryMappingsJson = JSON.toJSONString(categoryMappings);
                            JSONObject categoryMappingJsonResult = getResponse(categoryMappingsJson, logMapping, "/zmallsync/categoryMapping");

                            String categoryMappingCode = categoryMappingJsonResult.getString("code");
                            String categoryMappingMsg = categoryMappingJsonResult.getString("message");
                            List<Object> errorCategoryList = (List<Object>) categoryMappingJsonResult.get("resp");

                            List<Long> errorCategoryIds = new ArrayList<Long>();
                            if (errorCategoryList != null && errorCategoryList.size() > 0) {
                                for (Object categoryMapping : errorCategoryList) {
                                    errorCategoryIds.add(Long.valueOf(categoryMapping.toString()));
                                }
                            }

                            categoryMappingMsg = categoryMappingMsg + ",返回错误id为" + errorCategoryIds.toString();
                            logMapping.setProcessStatus(categoryMappingCode);
                            logMapping.setReturnMessage(categoryMappingMsg);

                            iLogManagerService.logEnd(iRequest, logMapping);
                        } catch (Exception e) {
                            logMapping.setProcessStatus("E");
                            logMapping.setReturnMessage(e.getMessage());
                            iLogManagerService.logEnd(iRequest, logMapping);
                            logger.error(e.getMessage());
                        }
                    }
                }

                //推送商品基础关联关系
                Map map = null;
                if (successProducts != null && successProducts.size() > 0) {
                    LogManager productRelationLog = new LogManager();
                    productRelationLog.setStartTime(new Date());
                    productRelationLog.setMessage("no data found");
                    productRelationLog.setProgramName(this.getClass().getName());
                    productRelationLog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
                    productRelationLog.setProgramDescription("商品基础关联关系推送");
                    productRelationLog = iLogManagerService.logBegin(iRequest, productRelationLog);
                    try {
                        map = iProductService.sendProductRelationToZmall(successProducts);
                        if (map.containsKey("dataJson")) {
                            productRelationLog.setMessage("json" + map.get("dataJson"));
                        }
                        productRelationLog.setProcessStatus(map.get("code").toString());
                        productRelationLog.setReturnMessage(map.get("message").toString());
                        iLogManagerService.logEnd(iRequest, productRelationLog);
                    } catch (Exception e) {
                        productRelationLog.setProcessStatus("E");
                        productRelationLog.setReturnMessage(e.getMessage());
                        iLogManagerService.logEnd(iRequest, productRelationLog);
                        logger.error(e.getMessage());
                    }
                }
                msg = msg + ",返回错误id为" + errorIds.toString();
                productlog.setProcessStatus(code);
                productlog.setReturnMessage(msg);
                //移除商品基础关联关系推送失败的商品，然后更新标识位
                if (CollectionUtils.isNotEmpty(successProducts)) {
                    iProductService.updateProductSyncflag(removeFailed(successProducts, map));
                }
                /**
                 if (successProducts != null && successProducts.size() > 0 && "S".equals(map.get("code"))) {
                 iProductService.updateProductSyncflag(successProducts);
                 }
                 **/
                iLogManagerService.logEnd(iRequest, productlog);
            } catch (Exception e) {
                productlog.setProcessStatus("E");
                productlog.setReturnMessage(e.getMessage());
                iLogManagerService.logEnd(iRequest, productlog);
                logger.error(e.getMessage());
            }
        }

        //推送频道信息
        LogManager odtypelog = new LogManager();
        odtypelog.setStartTime(new Date());
        odtypelog.setMessage("no data found");
        odtypelog.setProgramName(this.getClass().getName());
        odtypelog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        odtypelog.setProgramDescription("频道信息推送");
        odtypelog = iLogManagerService.logBegin(iRequest, odtypelog);
        List<OdtypeDto> odtypeList = odtypeService.selectOdtypeData();
        if (CollectionUtils.isNotEmpty(odtypeList)) {
            try {
                String jsonString = JSON.toJSONString(odtypeList);
                JSONObject jsonResult = getResponse(jsonString, odtypelog, "/zmallsync/prdCustChanSrc");

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorOdTypeIds = new ArrayList<>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object odtype : errorList) {
                        errorOdTypeIds.add(Long.valueOf(odtype.toString()));
                    }
                }

                msg = msg + ",返回错误id为" + errorOdTypeIds.toString();
                odtypelog.setProcessStatus(code);
                odtypelog.setReturnMessage(msg);
                iLogManagerService.logEnd(iRequest, odtypelog);
            } catch (Exception e) {
                odtypelog.setProcessStatus("E");
                odtypelog.setReturnMessage(e.getMessage());
                iLogManagerService.logEnd(iRequest, odtypelog);
                logger.error(e.getMessage());
            }

        }

        //推送品牌信息
        LogManager brandlog = new LogManager();
        brandlog.setStartTime(new Date());
        brandlog.setMessage("no data found");
        brandlog.setProgramName(this.getClass().getName());
        brandlog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        brandlog.setProgramDescription("品牌信息推送");
        brandlog = iLogManagerService.logBegin(iRequest, brandlog);
        List<BrandDto> brandList = brandService.selectPushingBrand();
        if (brandList != null && brandList.size() > 0) {
            try {
                String jsonString = JSON.toJSONString(brandList);
                JSONObject jsonResult = getResponse(jsonString, brandlog, "/zmallsync/brand");

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object brand : errorList) {
                        errorIds.add(Long.valueOf(brand.toString()));
                    }
                }
                //获取成功的品牌信息
                List<BrandDto> successBrand = brandList.stream()
                        .filter(brand -> !errorIds.contains(brand.getBrandId()))
                        .collect(Collectors.toList());

                msg = msg + ",返回错误id为" + errorIds.toString();
                brandlog.setProcessStatus(code);
                brandlog.setReturnMessage(msg);
                if (successBrand != null && successBrand.size() > 0) {
                    brandService.updateBrandSyncflag(successBrand);
                }
                iLogManagerService.logEnd(iRequest, brandlog);
            } catch (Exception e) {
                brandlog.setProcessStatus("E");
                brandlog.setReturnMessage(e.getMessage());
                iLogManagerService.logEnd(iRequest, brandlog);
                logger.error(e.getMessage());
            }
        }

        //推送套件商品映射信息
        LogManager suitLineMappinglog = new LogManager();
        suitLineMappinglog.setStartTime(new Date());
        suitLineMappinglog.setMessage("no data found");
        suitLineMappinglog.setProgramName(this.getClass().getName());
        suitLineMappinglog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        suitLineMappinglog.setProgramDescription("套件映射信息推送");
        suitLineMappinglog = iLogManagerService.logBegin(iRequest, suitLineMappinglog);
        List<SuitlineMappingDto> suitlineMappingList = suitlineMappingService.selectPushingSuitlineMapping();
        if (suitlineMappingList != null && suitlineMappingList.size() > 0) {
            try {
                String jsonString = JSON.toJSONString(suitlineMappingList);
                JSONObject jsonResult = getResponse(jsonString, suitLineMappinglog, "/zmallsync/suitlineMapping");

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object suitlineMapping : errorList) {
                        errorIds.add(Long.valueOf(suitlineMapping.toString()));
                    }
                }
                //获取成功的套件映射信息
                List<SuitlineMappingDto> successSuitlineMapping = suitlineMappingList.stream()
                        .filter(suitlineMapping -> !errorIds.contains(suitlineMapping.getMappingId()))
                        .collect(Collectors.toList());

                msg = msg + ",返回错误id为" + errorIds.toString();
                suitLineMappinglog.setProcessStatus(code);
                suitLineMappinglog.setReturnMessage(msg);
                if (successSuitlineMapping != null && successSuitlineMapping.size() > 0) {
                    suitlineMappingService.updateSuitlineMappingSyncflag(successSuitlineMapping);
                }
                iLogManagerService.logEnd(iRequest, suitLineMappinglog);
            } catch (Exception e) {
                suitLineMappinglog.setProcessStatus("E");
                suitLineMappinglog.setReturnMessage(e.getMessage());
                iLogManagerService.logEnd(iRequest, suitLineMappinglog);
                logger.error(e.getMessage());
            }
        }

        //推送补件商品映射信息
        LogManager patchLineMappinglog = new LogManager();
        patchLineMappinglog.setStartTime(new Date());
        patchLineMappinglog.setMessage("no data found");
        patchLineMappinglog.setProgramName(this.getClass().getName());
        patchLineMappinglog.setSourcePlatform(Constants.SOURCE_PLATFORM_JOB);
        patchLineMappinglog.setProgramDescription("补件映射信息推送");
        patchLineMappinglog = iLogManagerService.logBegin(iRequest, patchLineMappinglog);
        List<PatchlineMappingDto> patchlineMappingList = patchlineMappingService.selectPushingPatchlineMapping();
        if (patchlineMappingList != null && patchlineMappingList.size() > 0) {
            try {
                String jsonString = JSON.toJSONString(patchlineMappingList);
                JSONObject jsonResult = getResponse(jsonString, patchLineMappinglog, "/zmallsync/patchlineMapping");

                String code = jsonResult.getString("code");
                String msg = jsonResult.getString("message");
                List<Object> errorList = (List<Object>) jsonResult.get("resp");

                List<Long> errorIds = new ArrayList<Long>();
                if (errorList != null && errorList.size() > 0) {
                    for (Object patchlineMapping : errorList) {
                        errorIds.add(Long.valueOf(patchlineMapping.toString()));
                    }
                }
                //获取成功的补件映射信息
                List<PatchlineMappingDto> successPatchlineMapping = patchlineMappingList.stream()
                        .filter(patchlineMapping -> !errorIds.contains(patchlineMapping.getMappingId()))
                        .collect(Collectors.toList());

                msg = msg + ",返回错误id为" + errorIds.toString();
                patchLineMappinglog.setProcessStatus(code);
                patchLineMappinglog.setReturnMessage(msg);
                if (successPatchlineMapping != null && successPatchlineMapping.size() > 0) {
                    patchlineMappingService.updatePatchlineMappingSyncflag(successPatchlineMapping);
                }
                iLogManagerService.logEnd(iRequest, patchLineMappinglog);
            } catch (Exception e) {
                patchLineMappinglog.setProcessStatus("E");
                patchLineMappinglog.setReturnMessage(e.getMessage());
                iLogManagerService.logEnd(iRequest, patchLineMappinglog);
                logger.error(e.getMessage());
            }
        }

        // 推送多媒体到Zmall
        iMediaService.syncToZmall();
    }


    /**
     * @param jsonString
     * @param logManager
     * @param url
     * @return
     * @throws Exception
     * @description 向商城发布的服务发送请求并得到响应结果
     */
    public JSONObject getResponse(String jsonString, LogManager logManager, String url) throws Exception {
        String token = Auth.md5(SecretKey.KEY + jsonString);
        logManager.setMessage("json:" + jsonString);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        logger.info(jsonString);
        Response response = restClient.postString(Constants.ZMALL, url, jsonString, "json", map, null);
        JSONObject jsonResult = JSONObject.fromObject(response.body().string());
        return jsonResult;
    }

    /**
     * 从成功列表中移除商品基础关联关系推送失败的部分
     * Author: Zhang Zilong
     *
     * @param successProducts
     * @param result
     * @return
     */
    private List<ProductDto> removeFailed(List<ProductDto> successProducts, Map result) {
        ArrayList<String> productIds = (ArrayList<String>) result.get("resp");
        Iterator it = successProducts.iterator();
        if (CollectionUtils.isNotEmpty(productIds)) {
            for (String productId : productIds) {
                while (it.hasNext()) {
                    ProductDto dto = (ProductDto) it.next();
                    if (dto.getProductId().toString().equals(productId)) {
                        it.remove();
                        break;
                    }
                }
                it = successProducts.iterator();
            }
        }
        return successProducts;
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}

