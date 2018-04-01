package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/2/17.
 */
@Repository
public class GroupDao extends BaseDao {
    public GroupDao(){

        this.hashTag = "Sale";
        this.clazz = "SaleGroup";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("priority",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("name",FieldType.TYPE_MATCH));

    }

    /**
     * 根据Id（可选）、name（可选）查询促销分组
     *
     * @param data
     * @return
     */
    public List<Map<String,?>> selectByIdName(Map<String,Object> data) {
//        Map<String,Object> data= (Map<String, Object>) map.get("data");
//        int page=(int) map.get("page");
//        int pageSize=(int) map.get("pageSize");


        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String,List<Object>> equal = new HashMap<>();

        if (data.get("id") != null) {
            equal.put("id", Arrays.asList(data.get("id").toString()));
        }

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        //match模糊匹配
        Map<String, Object> matches = new HashMap<>();

        if (data.get("name") != null) {
            matches.put("name", data.get("name").toString());
        }

        if(MapUtils.isNotEmpty(matches)){
            String key3 = this.selectIdsSetByMatch(matches);
            if(key3!=null){
                keys.add(key3);
            }
        }
        if(keys.isEmpty()) {
            return this.selectAll();
        }else {
            return this.selectValuesByKeys(keys);

        }
    }

    public PagedValues selectAll(int pageSize,int pageNum) {
        BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps(createPattern());
        Set<String> set = hashOperations.keys();
        int total = set.size();
        List<String> list = new ArrayList<>(set);
        int start = ( pageNum-1)*pageSize;
        int end = start+pageSize;
        if(start>=total){
            return new PagedValues(total,Collections.emptyList());
        }
        if(end>total){
            end = total;
        }
        List<String> subList = list.subList(start, end);
        List<String> jsons = hashOperations.multiGet(subList);
        return new PagedValues(total,jsonMapper.convertToList(jsons));
    }

    public List<Map<String,?>> selectByPriority(int priority)
    {


        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String,List<Object>> equal = new HashMap<>();

        if (priority>0) {
            equal.put("priority", Arrays.asList(priority));
        }

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);

    }

    public Map<String,Object> submit(Map<String,Object> map)
    {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }

    public Map<String,Object> deleteGroup(Map<String,Object> map)
    {
        if(map.get("id")!=null)
        {
            if(!map.get("id").toString().trim().equals(""))
            {
                this.delete(map.get("id").toString());
            }
        }
        return map;
    }
}
