package com.hand.promotion.dubboService;

import com.alibaba.dubbo.config.annotation.Service;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleBundleService;
import com.hand.promotion.pojo.bundles.MstBundles;
import com.hand.promotion.pojo.bundles.MstBundlesMapping;
import com.hand.promotion.service.IMstBundlesService;
import com.hand.promotion.util.MapToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author changbingquan
 * @data 2018/02/04
 * 捆绑套装对象微服务service
 */
public class SaleBundleService implements ISaleBundleService {
    @Autowired
    private IMstBundlesService bundlesService;

    /**
     * 生成套装对应的促销活动
     *
     * @param bundleMap
     * @return
     */
    @Override
    public ResponseData saveBundle(Map bundleMap) throws Exception {
        ResponseData responseData = new ResponseData(false);
        //将前台参数转换成对应的DTO
        MstBundles bundles = new MstBundles();
        MapToBean.transMap2Bean(bundleMap, bundles);
        List<Map> bundlsMappings = (List) bundles.getBundlesMappings();
        List<MstBundlesMapping> bundlesMappingList = new ArrayList<>();
        for (Map bundlsMapping : bundlsMappings) {
            MstBundlesMapping mstBundlesMapping = new MstBundlesMapping();
            MapToBean.transMap2Bean(bundlsMapping, mstBundlesMapping);
            bundlesMappingList.add(mstBundlesMapping);
        }
        bundles.setBundlesMappings(bundlesMappingList);
        return bundlesService.createActivity(bundles);
    }


    /**
     * 启用套装对应的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData startUsing(List<String> promotionIds) {
        return bundlesService.startBundleActivity(promotionIds);
    }

    /**
     * 停用套装对应的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData endUsing(List<String> promotionIds) {
        return bundlesService.stopBundleActivity(promotionIds);
    }

    /**
     * 删除套装对应的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData delete(List<String> promotionIds) throws Exception {
        return bundlesService.deleteBundleActivity(promotionIds);
    }

}
