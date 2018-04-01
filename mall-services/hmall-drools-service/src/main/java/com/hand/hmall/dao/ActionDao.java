package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by hand on 2017/1/19.
 * 操作促销结果对应的处理方法数据
 */
@Repository
public class ActionDao extends BaseDao {
    public ActionDao(){
        this.hashTag = "rules";
        this.clazz = "Rules:action";
        this.idDescriptor = createFieldDescriptor("actionId", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("definitionId", FieldType.TYPE_EQUAL));
    }
}
