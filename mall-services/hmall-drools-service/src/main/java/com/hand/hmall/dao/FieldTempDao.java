package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by hand on 2017/1/13.
 * 促销条件中使用到的字段数据（如金额）
 */
@Repository
public class FieldTempDao extends BaseDao{
    {
        this.hashTag = "rules";
        this.clazz = "Rules:field";
        this.idDescriptor = createFieldDescriptor("fieldId", FieldType.TYPE_EQUAL);
        //字段对应DTO的ID
        addFieldDescriptor(createFieldDescriptor("modelId", FieldType.TYPE_EQUAL));
        //字段名称
        addFieldDescriptor(createFieldDescriptor("fieldName", FieldType.TYPE_EQUAL));
        //字段对应的促销条件的definitionId
        addFieldDescriptor(createFieldDescriptor("definitionId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("containId", FieldType.TYPE_EQUAL));
    }
}
