package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * 操作促销规则文件中使用到的FACT对象（如订单、订单行）
 */
@Repository
public class ModelTempDao extends BaseDao{
    {
        this.hashTag = "rules";
        this.clazz = "Rules:model";
        this.idDescriptor = createFieldDescriptor("modelId", FieldType.TYPE_EQUAL);
    }
}
