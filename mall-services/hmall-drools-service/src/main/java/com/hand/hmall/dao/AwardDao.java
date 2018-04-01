package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by cw on 2017/3/9.
 * 操作奖品数据 已弃用
 */

@Repository
public class AwardDao extends BaseDao{

    {
        this.clazz = "Award";
        this.hashTag = "Award";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("drawId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("mobileNumber",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("clientName",FieldType.TYPE_EQUAL));
    }

    public PagedValues queryAward(Map<String, Object> map) {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        List<String> keys = new ArrayList<>();
        Map data = (Map) map.get("data");
        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        if (data.get("drawId") != null) {
            equal.put("drawId", Arrays.asList(data.get("drawId").toString()));
        }

        if (data.get("clientName") != null) {
            equal.put("clientName", Arrays.asList(data.get("clientName").toString()));
        }

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1 != null){
                keys.add(key1);
            }
        }

        //match

        Map<String, Object> matches = new HashMap<>();

        if (data.get("mobileNumber") != null) {
            matches.put("mobileNumber", data.get("mobileNumber").toString());
        }

        if(MapUtils.isNotEmpty(matches)){
            String key3 = this.selectIdsSetByMatch(matches);
            if(key3 != null){
                keys.add(key3);
            }
        }
        PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
        return values;
    }
}
