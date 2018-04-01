package com.hand.hmall.dao;


import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

@Repository
public class CustomerCouponDao extends BaseDao {
    CustomerCouponDao() {
        this.hashTag = "user";
        this.clazz = "User:customerCoupon";
        this.idDescriptor = createFieldDescriptor("couponId", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("userId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponCode", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("status", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("range", FieldType.TYPE_EQUAL));
        //定义规则的主键作为该表外键
        addFieldDescriptor(createFieldDescriptor("cid", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("sendDate", FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("orderId", FieldType.TYPE_RANGE));

    }

    public List<Map<String, ?>> queryUserIdAndStatus(String userId, String status) {
        Map map = new HashMap();
        map.put("customerId", userId);
        map.put("status", status);
        return queryUserIdAndStatus(map);
    }

    public List<Map<String, ?>> queryUserIdAndStatus(Map map) {
        String userId = (String) map.get("customerId");
        String status = (String) map.get("status");
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();
        equal.put("userId", Arrays.asList(userId));
        if (!StringUtils.isEmpty(status)) {

            equal.put("status", Arrays.asList(status));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据用户登陆账号、优惠券状态、优惠券使用渠道查询优惠券
     *
     * @return
     */
    public List<Map<String, ?>> queryUserIdAndStatusAndRange(String userId, String status, String range) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (status != null) {
            equal.put("userId", Arrays.asList(userId));
            equal.put("status", Arrays.asList(status));
            equal.put("range", Arrays.asList(range, "ALL"));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    public List<Map<String, ?>> getByCouponId(String couponId) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (couponId != null && !couponId.trim().equals("")) {
            equal.put("couponId", Arrays.asList(couponId));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    public long countPagesByPagingField(String fieldName, String fieldValue) {
        String key = createPattern(fieldName, fieldValue);
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 根据  couponId  userId 查出 优惠券
     */
    public Map<String, ?> selectCouponById(String couponId, String userId) {
        // TODO Auto-generated method stub
//	   		Map<String, ?> customerCoupon= this.select(couponId);
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        equal.put("couponId", Arrays.asList(couponId));

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        List<Map<String, ?>> mapList = this.selectValuesByKeys(keys);
        return mapList.get(0);
    }

    public PagedValues queryByCidAndUserId(String cid, List<Object> userIds, int page, int pageSize) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        equal.put("cid", Arrays.asList(cid));

        if (userIds != null) {
            if (!userIds.isEmpty()) {
                equal.put("userId", userIds);
            } else {
                equal.put("userId", Arrays.asList("NULL"));
            }
        }
        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
    }

}
