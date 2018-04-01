package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/3/3.
 * 操作“前端可选条件结果”
 */
@Repository
public class SelectConditionDao extends BaseDao {

    public SelectConditionDao() {
        this.hashTag = "Sale";
        this.clazz = "Condition:SelectCondition";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //关联的definition表的外键，对应促销条件结果的ID
        addFieldDescriptor(createFieldDescriptor("definitionId", FieldType.TYPE_EQUAL));
        //条件/结果的文字描述
        addFieldDescriptor(createFieldDescriptor("meaning", FieldType.TYPE_MATCH));
        //用途，用于条件、结果、容器、分组
        addFieldDescriptor(createFieldDescriptor("code", FieldType.TYPE_EQUAL));
        //用于促销还是优惠券
        addFieldDescriptor(createFieldDescriptor("type", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("level", FieldType.TYPE_EQUAL));
    }

    /**
     * 根据条件查询可选条件、结果
     *
     * @param code     用途，用于条件、结果、容器、分组
     * @param type     用于促销还是优惠券
     * @param level    用于商品层级还是订单层级
     * @param page     分页起始页
     * @param pageSize 每页显示条数
     * @return
     */
    public PagedValues selectByCode(String code, String type, String level, int page, int pageSize) {
        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        equal.put("code", Arrays.asList(code));
        equal.put("type", Arrays.asList("ALL", type));
//        if (StringUtils.isNotEmpty(level) && !"undefined".equals(level)) {
//            equal.put("level", Arrays.asList(level, "ALL"));
//        }
        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }
        return this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);

    }
}
