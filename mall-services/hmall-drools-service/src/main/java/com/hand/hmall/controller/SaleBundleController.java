package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.MstBundles;
import com.hand.hmall.dto.MstBundlesMapping;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IMstBundlesService;
import com.hand.hmall.util.MapToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title SaleBundleController
 * @Description 套装处理入口
 * @date 2017/8/29 20:28
 */
@RestController
@RequestMapping("/h/sale/bundle")
public class SaleBundleController {
    @Autowired
    private IMstBundlesService bundlesService;

    /**
     * 生成套装对应的促销活动
     *
     * @param bundleMap
     * @return
     */
    @PostMapping("/saveBundle")
    @ResponseBody
    public ResponseData saveBundle(@RequestBody Map bundleMap) {
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
     * @param promotionIds
     * @return
     */
    @PostMapping("/startUsing")
    public ResponseData startUsing(@RequestBody List<String> promotionIds) {
        return bundlesService.startBundleActivity(promotionIds);
    }

    /**
     * 停用套装对应的促销活动
     * @param promotionIds
     * @return
     */
    @PostMapping("/endUsing")
    public ResponseData endUsing(@RequestBody List<String> promotionIds) {
        return bundlesService.stopBundleActivity(promotionIds);
    }

    /**
     * 删除套装对应的促销活动
     * @param promotionIds
     * @return
     */
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody List<String> promotionIds){
        return bundlesService.deleteBundleActivity(promotionIds);
    }

}
