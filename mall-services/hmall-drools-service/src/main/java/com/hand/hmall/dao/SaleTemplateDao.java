package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/1/4.
 */
@Repository
public class SaleTemplateDao extends BaseDao{



    public SaleTemplateDao(){
        this.hashTag = "Sale";
        this.clazz = "SaleTemplate";

        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("templateId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("templateName",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("templateDes",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("group",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("pageShowMes",FieldType.TYPE_MATCH));
        addFieldDescriptor(createFieldDescriptor("creationTime", FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("lastCreationTime", FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("releaseId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("isUsing", FieldType.TYPE_EQUAL));

    }
    public PagedValues querySaleTemplate(Map<String,Object> map)
    {

        Map<String,String> data= (Map<String, String>) map.get("data");
        if(!data.isEmpty()){

            List<String> keys = new ArrayList<>();

            int page = Integer.parseInt(map.get("page").toString());
            int pageSize = Integer.parseInt(map.get("pageSize").toString());

            Map<String,List<Object>> equal = new HashMap<>();
//            if(data.get("id")!=null){
//                equal.put("id", Arrays.asList(data.get("id")));
//            }
            if(data.get("templateId")!=null){
                equal.put("templateId", Arrays.asList(data.get("templateId")));
            }
            equal.put("isUsing",Arrays.asList("Y"));

            if(!equal.isEmpty())
            {
                String key = this.selectIdsSetByEqual(equal);
                if(key!=null){
                    keys.add(key);
                }
            }

            if(data.get("templateName")!=null) {
                Map<String, Object> match = new HashMap<>();
                match.put("templateName",data.get("templateName"));
                String key = this.selectIdsSetByMatch(match);
                if(key!=null){
                    keys.add(key);
                }
            }

            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;

        }else
        {
            int page = Integer.parseInt(map.get("page").toString());
            int pageSize = Integer.parseInt(map.get("pageSize").toString());

            List<String> keys = new ArrayList<>();
            Map<String,List<Object>> equal = new HashMap<>();
            equal.put("isUsing",Arrays.asList("Y"));

            String key = this.selectIdsSetByEqual(equal);
            keys.add(key);
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;

        }
    }

    public Map<String,Object> submitSaleTemplate(Map<String,Object> map)
    {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }
    public Map<String,Object> updateSaleTemplate(Map<String,Object> map)
    {
        this.update(map);
        return map;
    }
    public Map<String,Object> deleteSaleTemplate(Map<String,Object> map)
    {
        if (map.get("id") != null) {
            this.delete(map.get("id").toString());
        }
        return map;

    }

    public List<Map<String, ?>> selectByTemplateId(String templateId)
    {
        Map<String,List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if(templateId!=null){
            equal.put("templateId", Arrays.asList(templateId));
        }

        if(!equal.isEmpty())
        {
            String key = this.selectIdsSetByEqual(equal);
            if(key!=null){
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);

    }

    public Map<String,?> selectTemplateDetail(String id)
    {
        return this.select(id);
    }



}
