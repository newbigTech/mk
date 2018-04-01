package com.hand.promotion.cache.instance;

import com.alibaba.fastjson.JSON;

import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.dao.CacheDao;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/5
 * @description 优惠券缓存信息
 */
public class CouponInstance extends HepBasicDataCacheInstance<PromotionCouponsPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, PromotionCouponsPojo> couponCache;

    public CouponInstance(String instanceName, String tag, CacheDao<PromotionCouponsPojo> baseMongoDao) {
        super(instanceName, tag, baseMongoDao);
        couponCache = new ConcurrentHashMap<>();
    }

    @Override
    public void insert(PromotionCouponsPojo pojo) {
        couponCache.put(pojo.getCoupon().getId(), pojo);
    }

    @Override
    public void update(PromotionCouponsPojo pojo) {
        couponCache.put(pojo.getCoupon().getId(), pojo);
    }

    @Override
    public void delete(PromotionCouponsPojo data) {
        if (null != couponCache.get(data.getCoupon().getId())) {
            couponCache.remove(data.getCoupon().getId());
        }
    }

    @Override
    public PromotionCouponsPojo dealData(String msgStr) {
        logger.info(">>>>>>>>>>>>>>>>{}收到消息", getInstanceName(), msgStr);
        PromotionCouponsPojo promotionCouponsPojo = JSON.parseObject(msgStr, PromotionCouponsPojo.class);
        switchOperate(promotionCouponsPojo.getCacheOperator(), promotionCouponsPojo);
        return promotionCouponsPojo;
    }

    @Override
    public List<PromotionCouponsPojo> getCache() {

        return ListUtil.mapValueToList(couponCache);
    }

    @Override
    public List<PromotionCouponsPojo> getListByKey(String key) {
        List returnList = new ArrayList();
        returnList.add(couponCache.get(key));
        return returnList;
    }

    @Override
    public PromotionCouponsPojo getByKey(String key) {
        return couponCache.get(key);
    }
}
