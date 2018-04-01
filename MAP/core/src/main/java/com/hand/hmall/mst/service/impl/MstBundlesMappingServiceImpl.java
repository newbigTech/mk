package com.hand.hmall.mst.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.MstBundles;
import com.hand.hmall.mst.dto.MstBundlesMapping;
import com.hand.hmall.mst.mapper.MstBundlesMappingMapper;
import com.hand.hmall.mst.service.IMstBundlesMappingService;
import com.hand.hmall.mst.service.IMstBundlesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @descrption 商品套装映射service
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MstBundlesMappingServiceImpl extends BaseServiceImpl<MstBundlesMapping> implements IMstBundlesMappingService {

    @Autowired
    private MstBundlesMappingMapper mstBundlesMappingMapper;
    @Autowired
    private IMstBundlesService bundlesService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ArrayList<MstBundlesMapping> selectBundlesMappingByBundlesId(MstBundlesMapping mstBundlesMapping) {
        ArrayList<MstBundlesMapping> mstBundlesMappingList = mstBundlesMappingMapper.selectBundlesMappingByBundlesId(mstBundlesMapping);
        if (!mstBundlesMappingList.isEmpty()) {
            return mstBundlesMappingList;
        }
        return null;
    }

    /**
     * 查找商品套装中对应数据
     *
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<MstBundlesMapping> selectProduct(IRequest requestContext, MstBundlesMapping dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mstBundlesMappingMapper.selectProduct(dto);
    }

    /**
     * 套装组成的提交
     *
     * @param dto
     * @param requestCtx
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public List<MstBundlesMapping> batchUpdateMappingData(List<MstBundlesMapping> dto, IRequest requestCtx) throws Exception {
        ResponseData responseData = new ResponseData(this.batchUpdate(requestCtx, dto));
        StringBuffer errB = new StringBuffer("");
        if (responseData.isSuccess()) {
            //调用微服务接口

            String url = "/hmall-drools-service/h/sale/bundle/saveBundle";
            //传参
            JSONObject bundleObj = getSaveParmObj(dto);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bundleObj, null);
            try {
                com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                if (bundleActivityResp.isSuccess()) {
                    Map respMap = (Map) bundleActivityResp.getResp().get(0);
                    String promotionCode = (String) respMap.get("promotionCode");
                    MstBundles bundles = new MstBundles();
                    bundles.setBundlesId(Long.parseLong(bundleObj.get("bundlesId").toString()));
                    bundles = bundlesService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
                    bundles.setPromotionCode(promotionCode);
                    bundles.setSyncFlag("N");
                    bundlesService.updateByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
                } else {
                    errB.append("套装规则生成异常:" + bundleActivityResp.getMsg());
                }
            } catch (Exception e) {
                errB.append("调用接口过程异常，请联系管理员：" + e.getMessage());
            }
        } else {
            errB.append("提交失败！");
        }

        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }
        return dto;
    }

    /**
     * 批量删除
     *
     * @param dto
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public void batchDeleteMappnigData(List<MstBundlesMapping> dto) throws Exception {
        StringBuffer errB = new StringBuffer("");
        int i = this.batchDelete(dto);
        if (i > 0) {
            String url = "/hmall-drools-service/h/sale/bundle/saveBundle";
            //传参
            JSONObject bundleObj = getSaveParmObj(dto);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bundleObj, null);
            try {
                com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                if (!bundleActivityResp.isSuccess()) {
                    errB.append("更新套装促销活动失败" + bundleActivityResp.getMsg());
                }
                MstBundles bundles = new MstBundles();
                bundles.setBundlesId(Long.parseLong(bundleObj.get("bundlesId").toString()));
                bundles = bundlesService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
                Map respMap = (Map) bundleActivityResp.getResp().get(0);
                String promotionCode = (String) respMap.get("promotionCode");
                bundles.setPromotionCode(promotionCode);
                bundles.setSyncFlag("N");
                bundlesService.updateByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
            } catch (Exception e) {
                errB.append("调用接口出现异常，请联系管理员" + e.getMessage());
            }
        } else {
            errB.append("删除失败!");
        }
        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }
    }


    /**
     * 获取生成套装促销的参数
     *
     * @param dto
     * @return
     */
    JSONObject getSaveParmObj(List<MstBundlesMapping> dto) {
        MstBundles bundles = new MstBundles();
        bundles.setBundlesId(dto.get(0).getBundlesId());
        List<MstBundlesMapping> mstBundlesMappings = this.selectProduct(RequestHelper.newEmptyRequest(), dto.get(0), 1, 100);
        bundles = bundlesService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
        JSONObject bundleObj = JSON.parseObject(JSON.toJSONString(bundles));
        JSONArray bundleMapping = JSONArray.parseArray(JSON.toJSONString(mstBundlesMappings));
        bundleObj.put("bundlesMappings", bundleMapping);
        return bundleObj;
    }
}