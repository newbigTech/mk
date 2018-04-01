package com.hand.promotion.config;


import com.hand.promotion.cache.CacheInstanceManage;
import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.cache.instance.CouponInstance;
import com.hand.promotion.cache.instance.EntryActivityInstance;
import com.hand.promotion.cache.instance.OrderActivityCacheInstance;
import com.hand.promotion.dao.CacheDao;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xinyangMei
 * @version V1.0
 * @date 2017/12/12
 * @description 促销缓存配置类
 */
@Configuration
public class CacheConfig {

    @Value("${application.rocketMq.promotion.orderTag}")
    private String orderTag;

    @Value("${application.rocketMq.promotion.entrytag}")
    private String entrytag;

    @Value("${application.rocketMq.promotion.coupontag}")
    private String couponTag;

    @Autowired
    private HepBasicDataCacheInstance orderActivityCacheInstance;

    @Autowired
    private HepBasicDataCacheInstance entryActivityCacheInstance;

    @Autowired
    private HepBasicDataCacheInstance couponCacheInstance;


    /**
     * 配置订单层级促销活动缓存实例
     *
     * @param
     * @return
     */
    @Bean
    public HepBasicDataCacheInstance<PromotionActivitiesPojo> orderActivityCacheInstance(CacheDao<PromotionActivitiesPojo> orderPromotCacheDao) {
        String itemType = "订单层级促销缓存";
        return new OrderActivityCacheInstance(itemType, orderTag, orderPromotCacheDao);
    }

    /**
     * 配置商品层级促销活动缓存实例
     *
     * @param
     * @return
     */
    @Bean
    public HepBasicDataCacheInstance<PromotionActivitiesPojo> entryActivityCacheInstance(CacheDao<PromotionActivitiesPojo> entryPromotCacheDao) {
        String itemType = "商品层级促销缓存";
        return new EntryActivityInstance(itemType, entrytag, entryPromotCacheDao);
    }

    /**
     * 配置优惠券缓存实例
     *
     * @param
     * @return
     */
    @Bean
    public HepBasicDataCacheInstance<PromotionCouponsPojo> couponCacheInstance(CacheDao<PromotionCouponsPojo> promotionCouponDao) {
        String itemType = "优惠券缓存";
        return new CouponInstance(itemType, couponTag, promotionCouponDao);
    }

    @Bean
    public CacheInstanceManage cacheInstanceManage() {
        List<HepBasicDataCacheInstance> cacheInstances = new ArrayList<>();
        cacheInstances.add(orderActivityCacheInstance);
        cacheInstances.add(entryActivityCacheInstance);
        cacheInstances.add(couponCacheInstance);
        return new CacheInstanceManage(cacheInstances);
    }
}
