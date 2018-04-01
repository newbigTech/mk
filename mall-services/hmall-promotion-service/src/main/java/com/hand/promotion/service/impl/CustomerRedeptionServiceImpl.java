package com.hand.promotion.service.impl;

import com.hand.promotion.dao.CustomerRedeptionDao;
import com.hand.promotion.pojo.coupon.CustomerRedeptionPojo;
import com.hand.promotion.service.ICustomerRedeptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description 客户单张优惠券还能兑换的次数Service
 */
@Service
public class CustomerRedeptionServiceImpl implements ICustomerRedeptionService {

    @Autowired
    private CustomerRedeptionDao customerRedeptionDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<CustomerRedeptionPojo> queryByCouponIdAndUid(String cid, String customerId) {
        CustomerRedeptionPojo condition = new CustomerRedeptionPojo();
        condition.setCid(cid);
        condition.setUserId(customerId);
        return customerRedeptionDao.findByPojo(condition);
    }

    @Override
    public void insert(CustomerRedeptionPojo customerRedeptionPojo) {
        customerRedeptionDao.insertPojo(customerRedeptionPojo);
    }

    /**
     * 兑换记录减去兑换数量
     */
    @Override
    public void subCount(CustomerRedeptionPojo customerRedeptionPojo, int count) {
        customerRedeptionPojo.setNumber(customerRedeptionPojo.getNumber() - count);
        customerRedeptionDao.updatePojoByPK("id", customerRedeptionPojo.getId(), customerRedeptionPojo);
    }
}
