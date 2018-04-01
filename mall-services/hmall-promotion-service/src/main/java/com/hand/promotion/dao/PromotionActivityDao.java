package com.hand.promotion.dao;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.util.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author XinyangMei
 * @Title PromotionActivityDao
 * @Description 促销活动相关DAO
 * @date 2017/12/11 20:05
 */
@Repository
public class PromotionActivityDao extends CacheDao<PromotionActivitiesPojo> {

    /**
     * 查询初始化缓存需要的pojo信息
     *
     * @return
     */
    @Override
    public List<PromotionActivitiesPojo> findCacheInitData() {
        List status = Arrays.asList(Status.ACTIVITY.getValue(), Status.FAILURE.getValue());
        return findByStatus(status);
    }

    /**
     * 查询初始化商品层级缓存需要的pojo信息
     *
     * @return
     */
    public List<PromotionActivitiesPojo> findEntryCacheInitData() {
        List status = Arrays.asList(Status.ACTIVITY.getValue(), Status.FAILURE.getValue());
        return findByStatus(status);
    }

    /**
     * 查询初始化订单层级缓存需要的pojo信息
     *
     * @return
     */
    public List<PromotionActivitiesPojo> findOrderCacheInitData() {
        List status = Arrays.asList(Status.ACTIVITY.getValue(), Status.FAILURE.getValue());
        return findByStatus(status);
    }


    /**
     * 根据促销分组查询 还在使用的促销（非删除）促销
     *
     * @param groupId
     * @return
     */
    public List<PromotionActivitiesPojo> findByGroupId(String groupId) {
        List status = Arrays.asList(Status.EXPR);
        Criteria criteria = Criteria.where("activity.groupId").is(groupId);
        criteria.and("activityStatus").nin(status);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, PromotionActivitiesPojo.class);
    }

    /**
     * 根据促销活动状态集合查询促销活动
     *
     * @param status
     * @return
     */
    public List<PromotionActivitiesPojo> findByStatus(List<String> status) {
        Query query = new Query(Criteria.where("activity.status").in(status));
        List<PromotionActivitiesPojo> promotionActivitiesPojos = mongoTemplate.find(query, PromotionActivitiesPojo.class);
        return promotionActivitiesPojos;
    }

    /**
     * 根据促销活动编码查询出最新版本的促销活动
     *
     * @param activityId 促销活动编码id
     * @param isUsing    促销是否是最新版本,Y为是,N为不是
     * @return
     */
    public PromotionActivitiesPojo findLatestPromotion(String activityId, String isUsing) {
        Criteria criteria = Criteria.where("activity.activityId").is(activityId).and("activity.isUsing").is(isUsing);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, PromotionActivitiesPojo.class);
    }

    /**
     * 根据pojo 中的非空字段分页查询促销
     *
     * @param condition
     * @return
     */
    public ResponseData findByCondition(PromotionActivitiesPojo condition, int currentPageNum, int pageSize) {
        ActivityPojo activityPojo = condition.getActivity();
        Criteria criteria = null;
        boolean initFalg = false;
        if (activityPojo.getStartDate() != null) {
            initFalg = true;
            criteria = Criteria.where("activity.startDate").gt(activityPojo.getStartDate());
        }
        if (activityPojo.getEndDate() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.endDate").lt(activityPojo.getEndDate());
                initFalg = true;
            } else {
                criteria.and("activity.endDate").lt(activityPojo.getEndDate());

            }
        }
        if (activityPojo.getIsOverlay() != null) {

            if (!"ALL".equals(activityPojo.getIsOverlay())) {
                if (!initFalg) {
                    criteria = Criteria.where("activity.isOverlay").is(activityPojo.getIsOverlay());
                    initFalg = true;
                } else {
                    criteria.and("activity.isOverlay").is(activityPojo.getIsOverlay());
                }
            }


        }
        if (activityPojo.getStatus() != null) {
            if (!initFalg) {
                if ("ALL".equalsIgnoreCase(activityPojo.getStatus())) {
                    criteria = Criteria.where("activity.status").ne(Status.EXPR.name());
                } else {
                    criteria = Criteria.where("activity.status").is(activityPojo.getStatus());
                }
                initFalg = true;
            } else {
                criteria.and("activity.status").is(activityPojo.getStatus());
            }


        }
        if (activityPojo.getId() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.activityId").regex(activityPojo.getId());
                initFalg = true;
            } else {
                criteria.and("activity.activityId").regex(activityPojo.getId());

            }

        }
        if (activityPojo.getPriority() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.priority").is(activityPojo.getPriority());
                initFalg = true;
            } else {
                criteria.and("activity.priority").is(activityPojo.getPriority());

            }

        }
        if (activityPojo.getActivityName() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.activityName").regex(activityPojo.getActivityName());
                initFalg = true;
            } else {
                criteria.and("activity.activityName").regex(activityPojo.getActivityName());

            }

        }
        if (activityPojo.getIsExcludeShow() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.isExcludeShow").is(activityPojo.getIsExcludeShow());
                initFalg = true;
            } else {
                criteria.and("activity.isExcludeShow").is(activityPojo.getIsExcludeShow());

            }

        }
        if (activityPojo.getPageShowMes() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.pageShowMes").regex(activityPojo.getPageShowMes());
                initFalg = true;
            } else {
                criteria.and("activity.pageShowMes").regex(activityPojo.getPageShowMes());
            }

        }
        if (activityPojo.getGroup() != null) {
            if (!"ALL".equals(activityPojo.getGroup())) {
                if (!initFalg) {
                    criteria = Criteria.where("activity.group").is(activityPojo.getGroup());
                    initFalg = true;
                } else {
                    criteria.and("activity.group").is(activityPojo.getGroup());
                }
            }
        }
        if (activityPojo.getType() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.type").is(activityPojo.getType());
                initFalg = true;
            } else {
                criteria.and("activity.type").is(activityPojo.getType());

            }

        }
        if (activityPojo.getActivityDes() != null) {
            if (!initFalg) {
                criteria = Criteria.where("activity.activityDes").regex(activityPojo.getActivityDes());
                initFalg = true;
            } else {
                criteria.and("activity.activityDes").regex(activityPojo.getActivityDes());
            }

        }
        Query query = null;
        if (initFalg) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }

        //获取数据总条数
        long totalCount = count(query, PromotionActivitiesPojo.class);
        Pageable pageable = getPageRequest(totalCount, pageSize, currentPageNum);
        List<PromotionActivitiesPojo> promotionActivitiesPojos = mongoTemplate.find(query.with(pageable), PromotionActivitiesPojo.class);
        List<ActivityPojo> collect = promotionActivitiesPojos.stream().map(promotionActivitiesPojo -> {
            return promotionActivitiesPojo.getActivity();
        }).collect(Collectors.toList());
        ResponseData responseData = new ResponseData(collect);
        responseData.setTotal((int) totalCount);
        return responseData;
    }

    /**
     * 根据促销活动编码id 和isUsing 更新促销数据
     *
     * @param activityId 促销活动编码id
     * @param isUsing    是否是最新版本促销
     * @param updateData 要更新的数据
     */
    public void updateByActivityIdAndIsUsing(String activityId, String isUsing, PromotionActivitiesPojo updateData)  {
        Query query = new Query(Criteria.where("activity.activityId").is(activityId).and("activity.isUsing").is(isUsing));
        Update update = MongoUtil.pojoToUpdate(updateData);
        mongoTemplate.updateFirst(query, update, PromotionActivitiesPojo.class);
    }


    /**
     * 根据以下字段查询促销
     *
     * @param status  促销状态
     * @param isUsing 促销活动现在是否可用
     * @return
     */
    public List<PromotionActivitiesPojo> selectByStatusAndIsUsing(List status, String isUsing){
        Criteria criteria = Criteria.where("activity.isUsing").is(isUsing).and("activity.status").in(status);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, PromotionActivitiesPojo.class);
    }

    /**
     * 查询条件结果详细信息
     * @param id 关联的优惠券或者促销的主键
     * @param type 用于优惠券还是促销活动
     * @return
     */
    public PromotionActivitiesPojo selectByDetailIdAndType(String id, String type){
        Criteria criteria = Criteria.where("activity.activityId").is(id).and("activity.type").is(type);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query,PromotionActivitiesPojo.class);
    }




}
