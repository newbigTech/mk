package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * 促销条件结果定义类
 */
@Repository
public class DefinitionDao extends BaseDao {
    {
        this.hashTag = "rules";
        this.clazz = "Rules:definition";
        this.idDescriptor = createFieldDescriptor("definitionId", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("type", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("meaning", FieldType.TYPE_MATCH));
    }
}
