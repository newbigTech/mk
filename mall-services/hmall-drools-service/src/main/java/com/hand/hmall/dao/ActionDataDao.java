package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 保存促销结果的复杂的（包含的字段>1）优惠数据
 */
@Repository
public class ActionDataDao extends BaseDao{
    {
        this.hashTag = "Sale";
        this.clazz = "ActionData";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("activityId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("definitionId", FieldType.TYPE_EQUAL));
    }

    public Map<String,Map> selectByQuery(Map<String, Object> query){
        List queryList =  this.selectByMutilEqField(query);
        if (queryList!=null && !queryList.isEmpty()) {
            Map<String, Map> map = (Map<String, Map>) queryList.get(0);
            return map;
        }else {
            return null;
        }
    }
}
