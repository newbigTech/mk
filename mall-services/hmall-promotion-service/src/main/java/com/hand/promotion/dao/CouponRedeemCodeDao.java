package com.hand.promotion.dao;

import com.hand.promotion.pojo.coupon.CouponRedeemCodePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description
 */
@Repository
public class CouponRedeemCodeDao extends BaseMongoDao<CouponRedeemCodePojo> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据兑换码主键更新兑换码使用状态(Y为使用,N为未使用)
     *
     * @param pk
     * @param used
     */
    public void updateUsedByPK(String pk, String used) {
        Query query = new Query(Criteria.where("id").is(pk));
        Update update = new Update();
        update.set("used", used);
        mongoTemplate.updateFirst(query,update ,CouponRedeemCodePojo.class );
    }

}
