package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/3/22.
 * 该类未使用
 */
@Repository
public class DroolsUserDao extends BaseDao {
    {
        this.hashTag = "Sale";
        this.clazz = "Excel:User";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("userId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("name", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("excelId", FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("isSuccess", FieldType.TYPE_EQUAL));

    }

    public PagedValues queryByExcelId(String excelId,int page,int pageSize){
        List<String> keys = new ArrayList<>();

        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("excelId", Arrays.asList(excelId));

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
    }

    /**
     * 根据excel主键查询 从该excel中成功导入的用户id 未使用
     *
     * @param excelId
     * @return
     */
    public List<Map<String,?>> queryByExcelIdAndIsSuccess(String excelId,String isSuccess){
        List<String> keys = new ArrayList<>();

        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("excelId", Arrays.asList(excelId));
        equal.put("isSuccess", Arrays.asList(isSuccess));

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);
    }

}
