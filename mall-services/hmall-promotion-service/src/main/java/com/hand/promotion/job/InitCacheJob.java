package com.hand.promotion.job;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.cache.CacheInstanceManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author XinyangMei
 * @Title InitPromotionJob
 * @Description 初始化促销缓存job
 * @date 2017/12/11 16:30
 */
@Component
@Order(1)
public class InitCacheJob extends BaseJob {
    @Autowired
    private CacheInstanceManage cacheInstanceManage;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doExecute() {
        cacheInstanceManage.init();
        cacheInstanceManage.start();
        System.out.println("init job");
    }

}
