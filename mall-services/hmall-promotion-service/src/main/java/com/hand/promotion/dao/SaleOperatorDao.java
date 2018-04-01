package com.hand.promotion.dao;

import com.hand.promotion.pojo.SaleOperatorPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description
 */
@Repository
public class SaleOperatorDao extends BaseMongoDao<SaleOperatorPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 根据编码Id分页查询操作记录
     *
     * @param baseId
     * @param page
     * @param pageSize
     * @return
     */
    public List<SaleOperatorPojo> queryByBaseId(String baseId, int page, int pageSize) {
        Criteria criteria = Criteria.where("baseId").is(baseId);
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, SaleOperatorPojo.class);
        Pageable pageRequest = getPageRequest(count, pageSize, page);
        query.with(pageRequest);
        List<SaleOperatorPojo> saleOperatorPojos = mongoTemplate.find(query, SaleOperatorPojo.class);
        return saleOperatorPojos;
    }

    /**
     * 根据编码Id查询全部操作记录
     *
     * @param baseId
     * @return
     */
    public List<SaleOperatorPojo> selectByBaseId(String baseId) {
        Criteria criteria = Criteria.where("baseId").is(baseId);
        Query query = new Query(criteria);
        List<SaleOperatorPojo> saleOperatorPojos = mongoTemplate.find(query, SaleOperatorPojo.class);
        return saleOperatorPojos;
    }

    public void removeByBaseId(String baseId) {
        Query query = new Query(Criteria.where("baseId").is(baseId));
        mongoTemplate.remove(query,SaleOperatorPojo.class );
    }
}
