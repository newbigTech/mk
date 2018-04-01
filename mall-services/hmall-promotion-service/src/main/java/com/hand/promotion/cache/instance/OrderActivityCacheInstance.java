package com.hand.promotion.cache.instance;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.alibaba.fastjson.JSON;

import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.dao.CacheDao;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XinyangMei
 * @Title OrderActivityCacheInstance
 * @Description 促销规则缓存实例
 * @date 2017/12/12 10:59
 */
public class OrderActivityCacheInstance extends HepBasicDataCacheInstance<PromotionActivitiesPojo> {


    private Map<String, PromotionActivitiesPojo> activityCache;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * @param itemType     缓存名称
     * @param tag          mq tag
     * @param baseMongoDao mongodb实例
     */
    public OrderActivityCacheInstance(String itemType, String tag, CacheDao<PromotionActivitiesPojo> baseMongoDao) {
        super(itemType, tag, baseMongoDao);
        activityCache = new ConcurrentHashMap();
    }


    /**
     * 向缓存中新增记录
     *
     * @param pojo
     */
    @Override
    public void insert(PromotionActivitiesPojo pojo) {
        activityCache.put(pojo.getActivity().getId(), pojo);
    }

    /**
     * 更新缓存记录
     *
     * @param pojo
     */
    @Override
    public void update(PromotionActivitiesPojo pojo) {
        activityCache.put(pojo.getActivity().getId(), pojo);
    }

    /**
     * 删除缓存记录
     *
     * @param pojo
     */
    @Override
    public void delete(PromotionActivitiesPojo pojo) {
        activityCache.remove(pojo.getActivity().getId());
    }

    /**
     * 处理消息队列的消息
     *
     * @param msgStr
     * @return
     */
    @Override
    public PromotionActivitiesPojo dealData(String msgStr) {
        logger.info(">>>>>>>>>>>>>>>>{}收到消息", getInstanceName(), msgStr);
        PromotionActivitiesPojo promotionActivitiesPojo = JSON.parseObject(msgStr, PromotionActivitiesPojo.class);
        switchOperate(promotionActivitiesPojo.getCacheOperator(), promotionActivitiesPojo);
        return promotionActivitiesPojo;
    }

    @Override
    public List<PromotionActivitiesPojo> getCache() {
        return ListUtil.mapValueToList(activityCache);
    }

    /**
     * 根据缓存的第一个key获取对应的value
     *
     * @param key
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> getListByKey(String key) {
        return Arrays.asList(activityCache.get(key));
    }

    @Override
    public PromotionActivitiesPojo getByKey(String key) {
        return activityCache.get(key);
    }


}
