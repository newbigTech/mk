package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ScoreRange;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cw on 2017/2/28.
 * 抽奖使用 已弃用
 */

@Repository
public class SaleDrawDao extends BaseDao{
    {
        this.clazz = "SaleDraw";
        this.hashTag = "SaleDraw";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("drawId",FieldType.TYPE_EQUAL));//基础ID
        addFieldDescriptor(createFieldDescriptor("drawName",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("drawStatus",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("drawType",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("startDate",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("endDate",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("isUsing",FieldType.TYPE_EQUAL));
    }

    public PagedValues querySaleDraw(Map<String, Object> map) throws ParseException {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pagesize");
            List<String> keys = new ArrayList<>();

            //equal搜索
            Map<String, List<Object>> equal = new HashMap<>();
            equal.put("isUsing", Arrays.asList("Y"));

            if (map.get("drawId") != null) {
                equal.put("drawId", Arrays.asList(map.get("drawId").toString()));
            }

            if (map.get("drawStatus") != null) {
                equal.put("drawStatus", Arrays.asList(map.get("drawStatus").toString()));
            } else {
                equal.put("drawStatus", Arrays.asList("ACTIVE", "INACTIVE", "EXPIRED", "STOPPED"));
            }

            if (map.get("drawType") != null) {
                equal.put("drawType", Arrays.asList(map.get("drawType").toString()));
            }

            if(MapUtils.isNotEmpty(equal)){
                String key1 = this.selectIdsSetByEqual(equal);
                if(key1 != null){
                    keys.add(key1);
                }
            }

            //range匹配
            Map<String,ScoreRange> ranges = new HashMap<>();
            if(map.get("startDate") != null && map.get("endDate") != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date validStartDate = simpleDateFormat.parse(map.get("startDate").toString());
                Long validStartTime = validStartDate.getTime();

                Date validEndDate = simpleDateFormat.parse(map.get("endDate").toString());
                Long validEndTime = validEndDate.getTime();

                ScoreRange scoreRange = new ScoreRange();
                scoreRange.setMin(validStartTime.toString());
                scoreRange.setMax(validEndTime.toString());
                ranges.put("startDate", scoreRange);
            }

            if(MapUtils.isNotEmpty(ranges)){
                String key2 = this.selectIdsSetByRange(ranges);
                if(key2 != null){
                    keys.add(key2);
                }
            }
            //match模糊匹配

            Map<String, Object> matches = new HashMap<>();

            if (map.get("drawName") != null) {
                matches.put("drawName", map.get("drawName").toString());
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


    public List<Map<String, ?>> selectByDrawId(String drawId)
    {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if(drawId != null){
            equal.put("drawId", Arrays.asList(drawId));
        }

        if(!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if(key != null){
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

}
