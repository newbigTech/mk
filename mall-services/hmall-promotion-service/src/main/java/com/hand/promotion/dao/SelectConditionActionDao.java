package com.hand.promotion.dao;

import com.hand.promotion.pojo.activity.SelectConditionActionPojo;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 条件结果DAO
 */
@Repository
public class SelectConditionActionDao extends BaseMongoDao<SelectConditionActionPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据definitionId 和条件用途 插入或更新
     *
     * @param selectConditionActionPojo
     */
    public void insertOrUpdateByDefIdAndCode(SelectConditionActionPojo selectConditionActionPojo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Query query = new Query(Criteria.where("definitionId").is(selectConditionActionPojo.getDefinitionId()).and("code").is(selectConditionActionPojo.getCode()));
        Update update = MongoUtil.pojoToUpdate(selectConditionActionPojo);
        WriteResult upsert = mongoTemplate.upsert(query, update, selectConditionActionPojo.getClass());
        logger.info(upsert.toString());
    }


    /**
     * 根据code、type查询可用条件结果
     *
     * @param code 条件是基础条件（ADD_CONDITION）、还是组内可选条件（ADD_GROUP）、还是容器（ADD_CONTAINER）可选条件
     * @param type 条件、结果用于促销活动（ACTIVITY）还是优惠券（COUPON）
     * @return
     */
    public List<SelectConditionActionPojo> findByCodeAndType(String code, String type) {

        Query query = new Query(Criteria.where("type").in(Arrays.asList(type, "ALL")).and("code").is(code));
        return mongoTemplate.find(query, SelectConditionActionPojo.class);
    }


}
