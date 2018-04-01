package com.hand.promotion.dao;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.pojo.group.SaleGroupPojo;
import com.hand.promotion.util.MongoUtil;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author XinyangMei
 * @Title GroupDao
 * @Description 促销活动分组对应DAO
 * @date 2017/12/11 16:21
 */
@Repository
public class GroupDao extends BaseMongoDao<SaleGroupPojo> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public void upsertByGroupId(SaleGroupPojo saleGroupPojo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Query query = new Query(Criteria.where("id").is(saleGroupPojo.getId()));
        Update update = MongoUtil.pojoToUpdate(saleGroupPojo);
        WriteResult upsert = mongoTemplate.upsert(query, update, saleGroupPojo.getClass());
        logger.info(upsert.toString());
    }


    /**
     * 根据促销分组id或模板名称模糊查询分组信息
     *
     * @param id
     * @param name
     * @return
     */
    public List<SaleGroupPojo> matchByIdAndName(String id, String name) {
        Criteria criteria = null;
        if (!StringUtils.isEmpty(id)) {
            criteria = Criteria.where("id").regex(id);
        }

        if (!StringUtils.isEmpty(name)) {
            if (criteria == null) {
                criteria = Criteria.where("name").regex(name);
            } else {
                criteria.and("name").regex(name);
            }
        }
        if (criteria == null) {
            return mongoTemplate.find(new Query(), SaleGroupPojo.class);

        } else {
            return mongoTemplate.find(new Query(criteria), SaleGroupPojo.class);

        }
    }


}
