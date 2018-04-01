package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/3/22.
 */
@Repository
public class DroolsProductDao extends BaseDao{
    {
        this.hashTag = "Sale";
        this.clazz = "Excel:Product";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //产品Id
        addFieldDescriptor(createFieldDescriptor("productId", FieldType.TYPE_EQUAL));
        //产品编码
        addFieldDescriptor(createFieldDescriptor("productCode", FieldType.TYPE_EQUAL));
        //产品名称
        addFieldDescriptor(createFieldDescriptor("name", FieldType.TYPE_EQUAL));
        //导入的excel文件的Id
        addFieldDescriptor(createFieldDescriptor("excelId", FieldType.TYPE_EQUAL));
        //是否导入成功
        addFieldDescriptor(createFieldDescriptor("isSuccess", FieldType.TYPE_EQUAL));
    }

    /**
     * 根据excelId查询成功导入的产品数据
     *
     * @return
     */
    public PagedValues queryByExcelId(String excelId, int page, int pageSize){
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
     * 删除excelId 等于入参的所有商品数据
     * @param excelId
     * @return
     */
    public List<Map<String,?>> queryByExcelIdForDelete(String excelId){
        List<String> keys = new ArrayList<>();

        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("excelId", Arrays.asList(excelId));

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);
    }


    /**
     * 根据excelId isSuccess 查询导入的产品数据
     * @param excelId
     * @param isSuccess
     * @return
     */
    public List<Map<String,?>> queryByExcelIdAndIsSuccess(String excelId, String isSuccess){
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
