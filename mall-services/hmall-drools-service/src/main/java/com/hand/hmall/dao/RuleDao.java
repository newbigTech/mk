package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by hand on 2017/1/4.
 * 对生成的drools文件进行CRUD操作
 */
@Repository
public class RuleDao extends BaseDao{
    {
        this.hashTag = "rules";
        this.clazz = "Rules";
        this.idDescriptor = createFieldDescriptor("ruleId", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("couponId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("activityId", FieldType.TYPE_EQUAL));
    }
}
