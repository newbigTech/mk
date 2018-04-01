package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by shanks on 2017/3/22.
 * 对Excel:Relevance（商品组基本信息）表进行增删改查
 */
@Repository
public class DroolsExcelDao extends BaseDao {
    {
        this.hashTag = "Sale";
        this.clazz = "Excel:Relevance";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //导入的文件名称
        addFieldDescriptor(createFieldDescriptor("excelName", FieldType.TYPE_EQUAL));
        //导入的文件用途（目前只用于商品）
        addFieldDescriptor(createFieldDescriptor("type", FieldType.TYPE_EQUAL));
    }

    /**
     * 分页查询 根据type（用途）、名称查询导入的Excel文件描述
     *
     * @param map
     * @return
     */
    public PagedValues queryByCondition(Map<String, Object> map) {

        List<String> keys = new ArrayList<>();

        Map<String, Object> data = (Map<String, Object>) map.get("data");
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");

        Map<String, List<Object>> equal = new HashMap<>();
        equal.put("type", Arrays.asList(data.get("type")));

        if (data.get("excelName") != null) {
            equal.put("excelName", Arrays.asList(map.get("excelName")));
        }

        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);

    }

    /**
     * 根据excel名称查询导入的excel文件信息
     * @param map
     * @return
     */
    public List<Map<String,?>> queryByExcelName(Map<String, Object> map) {

        List<String> keys = new ArrayList<>();

//        Map<String, Object> data = (Map<String, Object>) map.get("data");
        Map<String, List<Object>> equal = new HashMap<>();
//        equal.put("type", Arrays.asList(data.get("type")));

        if (map.get("excelName") != null) {
            equal.put("excelName", Arrays.asList(map.get("excelName")));
        }

        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);

    }
}
