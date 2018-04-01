package com.hand.hmall.dao;

import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/2/22.
 */
@Repository
public class RedemptionDao extends BaseDao {

    public RedemptionDao(){
        this.hashTag = "promotion";
        this.clazz = "coupon:redemption";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("redemptionId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("type",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("number",FieldType.TYPE_EQUAL));
    }

    public Map<String,Object> submitRedemption(Map<String,Object> map)
    {
        if(map.get("id")==null||map.get("id").toString().trim().equals(""))
        {
            map.put("id", UUID.randomUUID());
            this.add(map);
        }
        return map;
    }

    public boolean deleteRedemptionCount(String id,String type,int deleteCount)
    {
        Map<String,?> dbData=selectByIdAndType(id,type);
        if(dbData!=null&&Integer.parseInt(dbData.get("number").toString())>0
                && Integer.parseInt(dbData.get("number").toString())-deleteCount>=0
                )
        {
            Map<String,Object> map=new HashMap<>();
            map.put("id",dbData.get("id"));
            map.put("type",type);
            map.put("redemptionId",id);
            map.put("number",Integer.parseInt(dbData.get("number").toString())-deleteCount);
            this.update(map);
            return true;
        }else
        {
            return false;
        }
    }

    public Map<String,?> selectByIdAndType(String redemptionId,String type)
    {
        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("redemptionId",Arrays.asList(redemptionId));
        equal.put("type",Arrays.asList(type));

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }
       List<Map<String,?>> maps= this.selectValuesByKeys(keys);
        if(!maps.isEmpty())
        {
            return maps.get(0);
        }
        return null;
    }

}
