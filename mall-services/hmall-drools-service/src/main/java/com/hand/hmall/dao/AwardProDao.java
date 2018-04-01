package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by cw on 2017/3/7.
 * 操作结果数据 已弃用
 */
@Repository
public class AwardProDao extends BaseDao{
    {
        this.clazz = "AwardPro";
        this.hashTag = "AwardPro";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("drawId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponName",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("awardProStatus",FieldType.TYPE_EQUAL));
    }

    public PagedValues queryAwardPro(Map<String, Object> map) {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        List<String> keys = new ArrayList<>();
        Map data = (Map) map.get("data");
        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        if (data.get("drawId")!=null) {
            equal.put("drawId", Arrays.asList(data.get("drawId").toString()));
        } else {
            equal.put("drawId", Arrays.asList("-1"));
        }

        if (data.get("couponId") != null) {
            equal.put("couponId", Arrays.asList(data.get("couponId").toString()));
        }

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1 != null){
                keys.add(key1);
            }
        }

        //match模糊匹配

        Map<String, Object> matches = new HashMap<>();

        if (data.get("couponName") != null) {
            matches.put("couponName", data.get("couponName").toString());
        }

        if(MapUtils.isNotEmpty(matches)){
            String key3 = this.selectIdsSetByMatch(matches);
            if(key3 != null){
                keys.add(key3);
            }
        }
        PagedValues values = null;
        if (keys.size() == 0) {
            values = this.selectAll(pageSize, page);
        } else {
            values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
        }
        return values;
    }
}
