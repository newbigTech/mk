package com.hand.promotion.dao;

import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.ActivityType;
import com.hand.promotion.pojo.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/28
 * @description 订单层级促销缓存初始化DAO
 */
@Repository
public class OrderPromotCacheDao extends CacheDao<PromotionActivitiesPojo> {


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 查询状态为活动中，已过期状态的订单层级促销活动
     *
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> findCacheInitData() {
        Criteria criteria = Criteria.where("activity.status").in(Arrays.asList(Status.FAILURE.getValue(), Status.ACTIVITY.getValue()))
            .and("activity.type").is(ActivityType.order.getValue());
        Query query = new Query(criteria);
        return mongoTemplate.find(query, PromotionActivitiesPojo.class);
    }
}
