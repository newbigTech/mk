package com.hand.promotion.dao;

import com.hand.promotion.pojo.coupon.CustomerRedeptionPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 客户单张优惠券最大兑换数DAO
 */
@Repository
public class CustomerRedeptionDao extends BaseMongoDao<CustomerRedeptionPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

}
