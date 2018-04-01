package com.hand.promotion.dao;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;
import com.hand.promotion.pojo.enums.CouponStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/10
 * @description 用户已发放优惠券DAO
 */
@Repository
public class CustomerCouponDao extends BaseMongoDao<CustomerCouponPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 分页查询优惠券用户关联关系
     *
     * @param mobileNum
     * @param cid
     * @param page
     * @param pageSize
     * @return
     */
    public ResponseData findByCondition(String mobileNum, String cid, int page, int pageSize) {
        Criteria criteria = null;
        if (!StringUtils.isEmpty(mobileNum)) {
            criteria = Criteria.where("userId").regex(mobileNum);
        }

        if (!StringUtils.isEmpty(cid)) {
            if (null == criteria) {
                criteria = Criteria.where("cid").is(cid);
            } else {
                criteria.and("cid").is(cid);
            }
        }
        Query query = null;
        if (null != criteria) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }

        long count = count(query, CustomerCouponPojo.class);
        Pageable pageRequest = getPageRequest(count, pageSize, page);
        List<CustomerCouponPojo> customerCouponPojos = mongoTemplate.find(query.with(pageRequest), CustomerCouponPojo.class);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setResp(customerCouponPojos);
        responseData.setTotal((int) count);
        return responseData;
    }

    /**
     * 更新已发放的未使用的用户优惠券状态
     *
     * @param cid
     * @param status 要更新的状态
     */
    public void updateUnusedStatus(String cid, CouponStatus status) {
        Query query = new Query(Criteria.where("cid").is(cid).and("status").ne(CouponStatus.STATUS_02));
        Update update = new Update();
        update.set("status", status.getValue());
        mongoTemplate.updateMulti(query, update, CustomerCouponPojo.class);
    }

    /**
     * 更新已发放的未使用的用户优惠券状态
     *
     * @param id     已发放优惠券主键
     * @param status 要更新的状态
     */
    public void updateStatus(String id, CouponStatus status) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("status", status.getValue());
        mongoTemplate.updateMulti(query, update, CustomerCouponPojo.class);
    }

    /**
     * 根据条件获取优惠券,条件都必输
     *
     * @param userId    发券账号
     * @param dateMills startDate<dateMills<endDate
     * @param status    优惠券状态
     * @return
     */
    public List<CustomerCouponPojo> findByCondition(String userId, Long dateMills, String status) {
        Criteria criteria = Criteria.where("userId").is(userId).
            and("status").is(status).
            and("startDate").lt(dateMills).
            and("endDate").gt(dateMills);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, CustomerCouponPojo.class);
    }


}
