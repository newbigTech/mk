package com.hand.promotion.controller;

import com.hand.dto.ResponseData;
import com.hand.promotion.cache.CacheInstanceManage;
import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.util.ListUtil;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/28
 * @description 查看促销缓存的状况
 */
@RestController
@RequestMapping("/cacheManage")
public class CacheInfoController {

    @Autowired
    private CacheInstanceManage cacheInstanceManage;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 查询所有的促销缓存
     * * @return
     */
    @RequestMapping("/queryALL")
    public ResponseData queryAllCacheData() {
        Map<String, List<Object>> cacheData = new HashMap<>();


        Map<String, HepBasicDataCacheInstance> allInstance = cacheInstanceManage.getAllInstance();
        List<HepBasicDataCacheInstance> insatanceList = ListUtil.mapValueToList(allInstance);
        insatanceList.forEach(insatance -> {
            cacheData.put(insatance.getInstanceName(), insatance.getCache());
        });
        return ResponseReturnUtil.returnTrueResp(Arrays.asList(cacheData));

    }

    /**
     * 根据缓存名称，获取所有缓存
     *
     * @param instanceName 缓存实例名称
     * @return
     */
    @RequestMapping("/findByInstance")
    public ResponseData findByInstance(@RequestParam String instanceName) {
        Map<String, HepBasicDataCacheInstance> allInstance = cacheInstanceManage.getAllInstance();
        HepBasicDataCacheInstance hepBasicDataCacheInstance = allInstance.get(instanceName);
        if (hepBasicDataCacheInstance != null) {
            return ResponseReturnUtil.returnTrueResp(hepBasicDataCacheInstance.getCache());

        }
        return ResponseReturnUtil.returnResp(null, MsgMenu.NO_VALID_CACHE_INSTANCE, false);

    }

    /**
     * 根据缓存名称和缓存的主键获取缓存
     *
     * @param instanceName
     * @param cacheKey
     * @return
     */
    @RequestMapping("/findByInstanceAndKey")
    public ResponseData findByInstanceAndKey(@RequestParam String instanceName, @RequestParam String cacheKey) {
        Map<String, HepBasicDataCacheInstance> allInstance = cacheInstanceManage.getAllInstance();
        HepBasicDataCacheInstance hepBasicDataCacheInstance = allInstance.get(instanceName);
        List cache = hepBasicDataCacheInstance.getCache();
        if (CollectionUtils.isEmpty(cache)) {
            return ResponseReturnUtil.returnFalseResponse("缓存实例不存在", "INSTANCE_NOT_EXUST");
        }
        return ResponseReturnUtil.returnTrueResp(Arrays.asList(hepBasicDataCacheInstance.getByKey(cacheKey)));

    }

}
