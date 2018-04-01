package com.hand.promotion.dao;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.excel.DroolsExcel;
import com.hand.promotion.pojo.excel.DroolsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darkdog on 2018/2/5.
 */
@Repository
public class DroolsProductDao extends BaseMongoDao<DroolsProduct> {


    public void delete(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, DroolsProduct.class);
    }

    /**
     * @param id
     * @return
     */
    public List<DroolsProduct> queryByExcelIdForDelete(String id) {
        List<DroolsProduct> droolsProducts = new ArrayList<>();
        Criteria criteria = Criteria.where("excelId").is(id);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, DroolsProduct.class);
    }

    /**
     * @param excelId
     * @param isSuccess
     * @return
     */
    public List<DroolsProduct> queryByExcelIdAndIsSuccess(String excelId, String isSuccess) {
        Criteria criteria = Criteria.where("id").is(excelId).and("isSuccess").is(isSuccess);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, DroolsProduct.class);
    }

    public ResponseData queryByExcelId(String excelId, Integer page, Integer pageSize){
        Criteria criteria = Criteria.where("excelId").is(excelId);
        Query query = new Query(criteria);
        long count = count(query, DroolsExcel.class);
        Pageable pageRequest = getPageRequest(count, pageSize, page);
        query.with(pageRequest);
        List<DroolsProduct> droolsProducts = mongoTemplate.find(query, DroolsProduct.class);
        ResponseData responseData = new ResponseData(droolsProducts);
        responseData.setTotal((int) count);
        return responseData;
    }


}
