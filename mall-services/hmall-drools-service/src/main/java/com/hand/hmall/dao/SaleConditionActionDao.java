package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/1/9.
 * 促销、优惠券条件结果数据
 */
@Repository
public class SaleConditionActionDao extends BaseDao {

    public SaleConditionActionDao(){

        this.hashTag = "Sale";
        this.clazz = "Condition:SaleConditionActions";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //关联促销或者优惠券描述信息的主键
        addFieldDescriptor(createFieldDescriptor("detailId",FieldType.TYPE_EQUAL));
        //条件结果属于促销活动还是优惠券
        addFieldDescriptor(createFieldDescriptor("type",FieldType.TYPE_EQUAL));

    }

    /**
     * 新建条件结果信息
     *
     * @param map
     * @return
     */
    public Map<String,Object> submitSaleCondition(Map<String,Object> map) {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }

    /**
     * 更新条件结果信息
     * @param map
     * @return
     */
    public Map<String,Object> updateSaleCondition(Map<String,Object> map) {
        this.update(map);
        return map;
    }

    /**
     * 查询条件结果详细信息
     * @param id 关联的优惠券或者促销的主键
     * @param type 用于优惠券还是促销活动
     * @return
     */
    public Map<String,Object> selectByDetailIdAndType(String id, String type) {
        Map<String,List<Object>> conditions = new HashMap<>();

        conditions.put("detailId", Arrays.asList(id));
        conditions.put("type", Arrays.asList(type));
        String key1 = this.selectIdsSetByEqual(conditions);
        List<Map<String, ?>> maps = this.selectValuesByKeys(Arrays.asList(key1));
        return (Map<String, Object>) maps.get(0);

    }


}
