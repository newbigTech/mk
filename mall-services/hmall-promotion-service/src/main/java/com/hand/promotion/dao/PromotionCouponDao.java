package com.hand.promotion.dao;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.pojo.order.MallEntryPojo;
import com.mongodb.WriteResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券Dao
 */
@Repository()
public class PromotionCouponDao extends CacheDao<PromotionCouponsPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询初始化缓存需要的优惠券信息
     * 查询活动中的优惠券
     *
     * @return
     */
    @Override
    public List<PromotionCouponsPojo> findCacheInitData() {
        return findByStatus(Arrays.asList(Status.ACTIVITY.getValue()));
    }

    /**
     * 根据促销活动状态集合查询促销活动
     *
     * @param status
     * @return
     */
    public List<PromotionCouponsPojo> findByStatus(List<String> status) {
        Query query = new Query(Criteria.where("coupon.status").in(status));
        List<PromotionCouponsPojo> promotionActivitiesPojos = mongoTemplate.find(query, PromotionCouponsPojo.class);
        return promotionActivitiesPojos;
    }

    /**
     * 根据优惠券编码,isUsing标识查询优惠券
     *
     * @param couponCode 优惠券编码
     * @param isUsing    是否最新版本
     * @return
     */
    public PromotionCouponsPojo findByCouponCodeAndUsing(String couponCode, String isUsing) {
        Query query = new Query(Criteria.where("coupon.couponCode").is(couponCode).and("coupon.isUsing").is(isUsing));
        return mongoTemplate.findOne(query, PromotionCouponsPojo.class);
    }

    /**
     * 根据优惠券编码查询优惠券
     *
     * @param couponCode 优惠券编码
     * @return
     */
    public List<PromotionCouponsPojo> findByCouponCode(String couponCode) {
        Query query = new Query(Criteria.where("coupon.couponCode").is(couponCode));
        return mongoTemplate.find(query, PromotionCouponsPojo.class);
    }

    /**
     * 根据优惠券编码id 查询最新或非最新版本的优惠券数据
     *
     * @param couponId 优惠券编码id
     * @param isUsing  是否是最新版本的优惠券
     * @return
     */
    public List<PromotionCouponsPojo> findByCouponIdAndIsUsing(String couponId, String isUsing) {
        Query query = new Query(Criteria.where("coupon.couponId").is(couponId).and("coupon.isUsing").is(isUsing));
        List<PromotionCouponsPojo> promotionCouponsPojos = mongoTemplate.find(query, PromotionCouponsPojo.class);
        return promotionCouponsPojos;
    }

    /**
     * 根据pojo 中的非空字段分页查询促销
     *
     * @param condition
     * @return
     */
    public ResponseData findByCondition(PromotionCouponsPojo condition, int currentPageNum, int pageSize) {
        CouponsPojo couponPojo = condition.getCoupon();
        Criteria criteria = null;
        boolean initFalg = false;
        if (couponPojo.getStartDate() != null) {
            initFalg = true;
            criteria = Criteria.where("coupon.startDate").gt(couponPojo.getStartDate());
        }
        if (couponPojo.getEndDate() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.endDate").lt(couponPojo.getEndDate());
                initFalg = true;
            } else {
                criteria.and("coupon.endDate").lt(couponPojo.getEndDate());

            }
        }
//        if (couponPojo.getIsOverlay() != null) {
//            if (!initFalg) {
//                criteria = Criteria.where("coupon.isOverlay").is(couponPojo.getIsOverlay());
//                initFalg = true;
//            } else {
//                criteria.and("coupon.isOverlay").is(couponPojo.getIsOverlay());
//            }
//
//        }
        if (couponPojo.getStatus() != null) {
            if (!initFalg) {
                if ("ALL".equalsIgnoreCase(couponPojo.getStatus())) {
                    criteria = Criteria.where("coupon.status").ne(Status.EXPR.name());
                } else {
                    criteria = Criteria.where("coupon.status").is(couponPojo.getStatus());
                }
                initFalg = true;
            } else {
                criteria.and("coupon.status").is(couponPojo.getStatus());
            }


        }
        if (couponPojo.getId() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.id").regex(couponPojo.getId());
                initFalg = true;
            } else {
                criteria.and("coupon.id").regex(couponPojo.getId());

            }

        }
        if (couponPojo.getCouponId() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.couponId").regex(couponPojo.getCouponId());
                initFalg = true;
            } else {
                criteria.and("coupon.couponId").regex(couponPojo.getCouponId());

            }

        }

        if (couponPojo.getCouponName() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.couponName").regex(couponPojo.getCouponName());
                initFalg = true;
            } else {
                criteria.and("coupon.couponName").regex(couponPojo.getCouponName());

            }

        }

        if (couponPojo.getCouponCode() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.couponCode").regex(couponPojo.getCouponCode());
                initFalg = true;
            } else {
                criteria.and("coupon.couponCode").regex(couponPojo.getCouponCode());
            }

        }

        if (couponPojo.getType() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.type").is(couponPojo.getType());
                initFalg = true;
            } else {
                criteria.and("coupon.type").is(couponPojo.getType());

            }

        }
        if (couponPojo.getCouponDes() != null) {
            if (!initFalg) {
                criteria = Criteria.where("coupon.couponDes").regex(couponPojo.getCouponDes());
                initFalg = true;
            } else {
                criteria.and("coupon.couponDes").regex(couponPojo.getCouponDes());
            }

        }
        Query query = null;
        if (initFalg) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }

        //获取数据总条数
        long totalCount = count(query, PromotionCouponsPojo.class);
        Pageable pageable = getPageRequest(totalCount, pageSize, currentPageNum);
        List<PromotionCouponsPojo> promotionCouponsPojos = mongoTemplate.find(query.with(pageable), PromotionCouponsPojo.class);
        List<CouponsPojo> collect = promotionCouponsPojos.stream().map(promotionActivitiesPojo -> {
            return promotionActivitiesPojo.getCoupon();
        }).collect(Collectors.toList());
        ResponseData responseData = new ResponseData(collect);
        responseData.setTotal((int) totalCount);
        return responseData;
    }

    public void removeByIds(List<String> ids) {
        Query query = new Query(Criteria.where("id").in(ids));
        mongoTemplate.remove(query, PromotionCouponsPojo.class);
    }

    /**
     * */
    public boolean setUsed(Map map) {
        return true;
    }

    /**
     * 查询需要同步的Coupon
     */
    public List<CouponsPojo> queryCouponBySyn() {
        Criteria criteria = Criteria.where("coupon.isSyn").is("N").and("coupon.isUsing").is("Y");
        Query query = new Query();
        query.addCriteria(criteria);
        List<PromotionCouponsPojo> promotionCouponsPojos = mongoTemplate.find(query, PromotionCouponsPojo.class);
        List<CouponsPojo> couponsPojos = new ArrayList<CouponsPojo>();
        for (PromotionCouponsPojo item : promotionCouponsPojos) {
            couponsPojos.add(item.getCoupon());
        }
        return couponsPojos;
    }

    /**
     * 更新优惠券信息
     * 设置优惠卷同步标志，设置为同步
     *
     * @param couponCodes
     * @return
     */
    public void setCouponSyn(List<String> couponCodes) {
        Criteria criteria = Criteria.where("coupon.couponCode").in(couponCodes).and("coupon.isUsing").is(PromotionConstants.Y);
        Query query = new Query();
        query.addCriteria(criteria);
        Update update = Update.update("coupon.isSyn", PromotionConstants.Y);
        WriteResult result = mongoTemplate.updateMulti(query, update, PromotionCouponsPojo.class);
    }

    public void insert(List<PromotionCouponsPojo> promotionCouponsPojos) {
        mongoTemplate.insert(promotionCouponsPojos, PromotionCouponsPojo.class);
    }

    private void remove() {
        Criteria criteria = Criteria.where("id").is("1313");
        Query query = new Query();
        mongoTemplate.remove(query, PromotionCouponsPojo.class);
        Criteria criteria1=Criteria.where("id").is("12").and("name").is("qcb");

    }
}
