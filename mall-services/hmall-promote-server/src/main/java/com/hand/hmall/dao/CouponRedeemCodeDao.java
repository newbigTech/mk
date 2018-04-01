package com.hand.hmall.dao;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 优惠券兑换码表对应Dao
 *
 * @author XinyangMei
 * @Title CouponRedeemCodeDao
 * @Description desp
 * @date 2017/9/24 10:57
 */
@Repository
public class CouponRedeemCodeDao extends BaseDao {
    CouponRedeemCodeDao() {
        this.hashTag = "promotion";
        this.clazz = "coupon:couponRedeemCode";
        this.idDescriptor = createFieldDescriptor("code", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("couponCode", FieldType.TYPE_EQUAL));
        //兑换码
        //兑换码是否使用
        addFieldDescriptor(createFieldDescriptor("used", FieldType.TYPE_EQUAL));
    }

    public List<Map<String, ?>> queryUsefulRedeemCode(String couponCode) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();
        equal.put("used", Arrays.asList("N"));
        if (!StringUtils.isEmpty(couponCode)) {
            equal.put("couponCode", Arrays.asList(couponCode));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }
}
