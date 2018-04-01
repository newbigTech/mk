package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

/**
 * Created by shanks on 2017/3/20.
 */
@Repository
public class CouponTempDao extends BaseDao {

    CouponTempDao(){
        this.hashTag = "user";
        this.clazz = "User:couponTemp";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("tempId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("distributionId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("userId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("creationTime", FieldType.TYPE_EQUAL));
    }

    public void submit(Map<String,Object> map){
        if(map.get("id")==null){
            map.put("id", UUID.randomUUID());
            this.add(map);
        }
    }
}
