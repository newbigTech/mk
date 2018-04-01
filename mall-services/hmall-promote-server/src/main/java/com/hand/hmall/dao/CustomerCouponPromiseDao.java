package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by hand on 2017/3/22.
 */
@Repository
public class CustomerCouponPromiseDao extends BaseDao{
    CustomerCouponPromiseDao(){
        this.hashTag = "user";
        this.clazz = "User:customerCouponTemp";
        this.idDescriptor =createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("tempId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("userId", FieldType.TYPE_EQUAL));
    }
}
