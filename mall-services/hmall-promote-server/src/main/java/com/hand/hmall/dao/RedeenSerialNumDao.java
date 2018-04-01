package com.hand.hmall.dao;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author XinyangMei
 * @Title RedeenSerialNumDao
 * @Description desp
 * @date 2017/9/28 9:43
 */
@Repository

public class  RedeenSerialNumDao extends BaseDao {
     RedeenSerialNumDao() {
        this.hashTag = "promotion";
        this.clazz = "coupon:redeenSerialNum";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("date", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("serialNum",FieldType.TYPE_EQUAL));
    }
}
