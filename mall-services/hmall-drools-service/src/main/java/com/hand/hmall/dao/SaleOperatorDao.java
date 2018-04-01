package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/1/19.
 * 促销、优惠券、促销模板操作人信息
 */
@Repository
public class SaleOperatorDao extends BaseDao{

    public SaleOperatorDao(){

        this.hashTag = "Sale";
        this.clazz = "Version:SaleOperator";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //操作人的uid
        addFieldDescriptor(createFieldDescriptor("operator",FieldType.TYPE_MATCH));
        //操作描述
        addFieldDescriptor(createFieldDescriptor("operation",FieldType.TYPE_MATCH));
        //修改日期
        addFieldDescriptor(createFieldDescriptor("changeDate", FieldType.TYPE_RANGE));
        //标识操作的是促销活动还是促销模板还是优惠券
        addFieldDescriptor(createFieldDescriptor("type",FieldType.TYPE_EQUAL));
        //促销活动或促销模板或优惠券的编码Id
        addFieldDescriptor(createFieldDescriptor("parentId",FieldType.TYPE_EQUAL));
        //促销活动或促销模板或优惠券的编码版本Id
        addFieldDescriptor(createFieldDescriptor("baseId",FieldType.TYPE_EQUAL));

    }

    /**
     * 新增操作记录
     *
     * @param map
     * @return
     */
    public Map<String,Object> submit(Map<String,Object> map) {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }

    /**
     * 根据编码Id分页查询操作记录
     * @param baseId
     * @param page
     * @param pageSize
     * @return
     */
    public List<Map<String,?>> queryByBaseId(String baseId, int page, int pageSize) {
        List<String> keys = new ArrayList<>();

        Map<String,List<Object>> conditions = new HashMap<>();

        conditions.put("baseId", Arrays.asList(baseId));
        if(MapUtils.isNotEmpty(conditions)){
            String key1 = this.selectIdsSetByEqual(conditions);
            if(key1!=null){
                keys.add(key1);
            }
        }
        if(keys.size()>0){
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values.getValues();
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据编码Id查询全部操作记录
     * @param baseId
     * @return
     */
    public List<Map<String,?>> selectByBaseId(String baseId) {
        Map<String,List<Object>> conditions = new HashMap<>();

        conditions.put("baseId", Arrays.asList(baseId));
        String key1 = this.selectIdsSetByEqual(conditions);
        List<Map<String, ?>> maps= this.selectValuesByKeys(Arrays.asList(key1));
        return maps;

    }


}
