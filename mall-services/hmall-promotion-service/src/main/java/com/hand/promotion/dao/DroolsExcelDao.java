package com.hand.promotion.dao;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.excel.DroolsExcel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by darkdog on 2018/2/5.
 */
@Repository
public class DroolsExcelDao extends BaseMongoDao<DroolsExcel> {

    /**
     * 根据excel名称查询导入的excel文件信息
     *
     * @param map
     * @return
     */
    public List<DroolsExcel> queryByExcelName(Map<String, Object> map) {
        Criteria criteria = Criteria.where("excelName").is(map.get("excelName"));
        Query query = new Query(criteria);
        List<DroolsExcel> droolsExcels = mongoTemplate.find(query, DroolsExcel.class);
        return droolsExcels;
    }


    /**
     * 根据ID更新一条记录
     *
     * @param droolsExcel
     */
    public void update(DroolsExcel droolsExcel) {
        Criteria criteria = Criteria.where("id").is(droolsExcel.getId());
        Update update = new Update();
        update.set("excelName", droolsExcel.getExcelName());
        update.set("type", droolsExcel.getType());
        Query query = new Query(criteria);
        this.mongoTemplate.updateMulti(query, update, DroolsExcel.class);
    }

    /**
     * @param map
     * @return
     */
    public ResponseData queryByCondition(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        Criteria criteria = null;
        if (StringUtils.isNotBlank((String) data.get("excelName"))) {
            criteria = Criteria.where("type").is(data.get("type")).and("excelName").regex((String) data.get("excelName"));
        } else {
            criteria = Criteria.where("type").is(data.get("type"));
        }
        Query query = new Query(criteria);
        long count = count(query, DroolsExcel.class);
        Pageable pageRequest = getPageRequest(count, Integer.parseInt(map.get("pageSize").toString()),
                Integer.parseInt(map.get("page").toString()));
        query.with(pageRequest);
        List<DroolsExcel> droolsExcels = mongoTemplate.find(query, DroolsExcel.class);
        ResponseData responseData = new ResponseData(droolsExcels);
        responseData.setTotal((int) count);
        return responseData;
    }

}
