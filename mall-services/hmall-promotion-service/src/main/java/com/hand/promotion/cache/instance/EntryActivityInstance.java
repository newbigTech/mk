package com.hand.promotion.cache.instance;

import com.alibaba.fastjson.JSON;

import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.dao.CacheDao;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description 商品层级促销活动缓存
 */
public class EntryActivityInstance extends HepBasicDataCacheInstance<PromotionActivitiesPojo> {


    /**
     * 商品层级促销缓存,第一个key是促销活动分组,第二个key是促销活动主键
     */
    private Map<String, Map<String, PromotionActivitiesPojo>> groupActivityCache;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public EntryActivityInstance(String itemType, String tag, CacheDao<PromotionActivitiesPojo> baseMongoDao) {
        super(itemType, tag, baseMongoDao);
        groupActivityCache = new ConcurrentHashMap<>();

    }

    /**
     * 向缓存中新增记录
     *
     * @param pojo
     */
    @Override
    public void insert(PromotionActivitiesPojo pojo) {
        ActivityPojo activity = pojo.getActivity();
        String group = activity.getGroup();
        Map<String, PromotionActivitiesPojo> groupActivities = groupActivityCache.getOrDefault(group, new HashMap<>());
        groupActivities.put(activity.getId(), pojo);
        groupActivityCache.put(group, groupActivities);
    }

    /**
     * 更新缓存记录
     *
     * @param pojo
     */
    @Override
    public void update(PromotionActivitiesPojo pojo) {
        insert(pojo);
    }

    /**
     * 删除缓存记录
     *
     * @param pojo
     */
    @Override
    public void delete(PromotionActivitiesPojo pojo) {
        ActivityPojo activity = pojo.getActivity();
        String group = activity.getGroup();
        Map<String, PromotionActivitiesPojo> groupActivities = groupActivityCache.get(group);
        if (groupActivities != null) {
            groupActivities.remove(activity.getId());
        }
    }

    /**
     * 将msgbody 转换成对应的pojo
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

    /**
     * 获取所有缓存的商品层级的促销规则
     *
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> getCache() {
        List<PromotionActivitiesPojo> list = new ArrayList<>();
        List<Map<String, PromotionActivitiesPojo>> objects = ListUtil.mapValueToList(groupActivityCache);
        objects.forEach(activitiesPojoMap -> {
            List<PromotionActivitiesPojo> groupPromotionList = ListUtil.mapValueToList(activitiesPojoMap);
            list.addAll(groupPromotionList);

        });
        return list;
    }

    /**
     * 根据 促销分组key 获取缓存中 分组是key的促销活动
     *
     * @param key 促销分组key
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> getListByKey(String key) {
        Map<String, PromotionActivitiesPojo> groupPromotionMap = groupActivityCache.getOrDefault(key, new HashMap<>());
        return ListUtil.mapValueToList(groupPromotionMap);
    }

    /**
     * 先把缓存中的groupActivityCache汇总成<activityId,PromotionPojo>格式,再根据key获取对应promotion
     *
     * @param key 促销活动主键
     * @return
     */
    @Override
    public PromotionActivitiesPojo getByKey(String key) {
        Map<String, PromotionActivitiesPojo> map = new HashMap<>();
        List<Map<String, PromotionActivitiesPojo>> objects = ListUtil.mapValueToList(groupActivityCache);
        objects.forEach(activitiesPojoMap -> {
            map.putAll(activitiesPojoMap);
        });
        return map.get(key);
    }


}
