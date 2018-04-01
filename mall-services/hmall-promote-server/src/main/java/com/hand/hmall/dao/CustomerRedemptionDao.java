package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/2/23.
 */
@Repository
public class CustomerRedemptionDao extends BaseDao {
    CustomerRedemptionDao() {
        this.hashTag = "promotion";
        this.clazz = "coupon:customerRedemption";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("userId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("number", FieldType.TYPE_EQUAL));
        //优惠券表的主键
        addFieldDescriptor(createFieldDescriptor("cid", FieldType.TYPE_EQUAL));

    }

    public Map<String, Object> submitCustomerRedemption(Map<String, Object> map) {
        if (map.get("id") == null || map.get("id").toString().trim().equals("")) {
            map.put("id", UUID.randomUUID());
            this.add(map);
        }
        return map;
    }

    public boolean redemptionCount(String id, String userId, int count) {
        Map<String, ?> dbData = queryByUserIdAndCId(userId, id);
        if (dbData != null && Integer.parseInt(dbData.get("number").toString()) > 0
                && Integer.parseInt(dbData.get("number").toString()) - count >= 0
                && count > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", dbData.get("id"));
            map.put("userId", userId);
            map.put("cid", id);
            map.put("number", Integer.parseInt(dbData.get("number").toString()) - count);
            this.update(map);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, ?> queryByUserIdAndCId(String userId, String cid) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();


        equal.put("cid", Arrays.asList(cid));
        equal.put("userId", Arrays.asList(userId));
        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        List<Map<String, ?>> maps = this.selectValuesByKeys(keys);

        if (maps != null && !maps.isEmpty()) {
            return maps.get(0);
        } else {
            return null;
        }
    }
}
