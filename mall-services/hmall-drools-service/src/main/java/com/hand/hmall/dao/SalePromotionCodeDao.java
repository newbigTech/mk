package com.hand.hmall.dao;

import com.hand.hmall.menu.Status;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/3/9.
 * 促销活动商品关联关系
 */
@Repository
public class SalePromotionCodeDao extends BaseDao{
    {
        this.hashTag = "Sale";
        this.clazz = "SalePromotionCode";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //关联的促销活动编码id
        addFieldDescriptor(createFieldDescriptor("activityId",FieldType.TYPE_EQUAL));
        //促销活动描述
        addFieldDescriptor(createFieldDescriptor("meaning",FieldType.TYPE_EQUAL));
        //关联的商品Code或者商品分类编码
        addFieldDescriptor(createFieldDescriptor("definedId",FieldType.TYPE_EQUAL));
        //definedId关联的是商品编码还是分类编码
        addFieldDescriptor(createFieldDescriptor("type",FieldType.TYPE_EQUAL));
        //关联关系状态
        addFieldDescriptor(createFieldDescriptor("status",FieldType.TYPE_EQUAL));
        //促销活动优先级
        addFieldDescriptor(createFieldDescriptor("priority",FieldType.TYPE_EQUAL));
    }

    /**
     * 根据促销活动编码id查询促销商品关联关系
     *
     * @param activityId 促销活动编码Id
     * @return
     */
    public List<Map<String,?>> selectByActivityId(String activityId) {

        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        equal.put("activityId", Arrays.asList(activityId));


        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }
        if(keys.isEmpty()){
            return new ArrayList<>();
        }else {
            return this.selectValuesByKeys(keys);
        }


    }

    /**
     * 根据促销活动编码id 状态 查询促销商品关联关系
     * @param activityId 促销活动编码id
     * @param status 关联关系状态
     * @return
     */
    public List<Map<String,?>> selectByActivityIdAndStatus(String activityId, String status) {

        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        equal.put("activityId", Arrays.asList(activityId));
        equal.put("status", Arrays.asList(status));

        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据商品编码或者分类编码查询促销商品关联关系
     * @param definedId
     * @param type
     * @return
     */
    public List<Map<String,?>> selectActivityByCode(String definedId, String type) {
        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        //code值productCode
        if(type.equals("CODE")) {
            equal.put("definedId", Arrays.asList(definedId));
            equal.put("type", Arrays.asList("CODE", "ALL"));
        }else if (type.equals("CATEGORY")){
            equal.put("definedId", Arrays.asList(definedId));
            equal.put("type", Arrays.asList("CATEGORY"));
        }
        equal.put("status", Arrays.asList(Status.ACTIVITY.getValue()));
        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }
        if(keys.isEmpty()){
            return new ArrayList<>();
        }else {
            return this.selectValuesByKeys(keys);
        }
    }

}
