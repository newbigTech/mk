package com.hand.promotion.dao;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.util.MongoUtil;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author XinyangMei
 * @Title BaseMongoDao
 * @Description 抽象mongoDB公共方法
 * @date 2017/12/11 11:07
 */
public abstract class BaseMongoDao<T> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 根据pojo基础字段查询数据,多字段关系为And
     *
     * @param pojo
     */
    public List<T> findByPojo(T pojo) {
        Criteria criteria = MongoUtil.pojoToCriteria(pojo);
        Query query = criteria == null ? new Query() : new Query(criteria);
        List<T> pojos = mongoTemplate.find(query, (Class<T>) pojo.getClass());
        return pojos;
    }

    /**
     * 获取表中所有数据
     *
     * @param tClass
     * @return
     */
    public List<T> findAll(Class<T> tClass) {
        List<T> all = mongoTemplate.findAll(tClass);
        return all;
    }

    /**
     * 根据主键查询pojo
     *
     * @param pk
     * @param tClass
     * @return
     */
    public T findByPK(String pk, Class<T> tClass) {
        T byId = mongoTemplate.findById(pk, tClass);
        return byId;
    }


    /**
     * 将pojo插入对应的Collection中。collection名称为pojo上的@Document 标识名称。不存在@Document 取pojo类名
     *
     * @param pojo
     */
    public void insertPojo(T pojo) {
        mongoTemplate.insert(pojo);
    }

    /**
     * 批量插入数据
     *
     * @param pojos
     */
    public void insertPojos(List<T> pojos) {
        mongoTemplate.insertAll(pojos);
    }

    /**
     * 根据主键更新pojo中不为null的字段到数据库
     *
     * @param pkName 主键对应字段
     * @param pkVal  主键值
     * @param pojo   要更新的数据
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public WriteResult updatePojoByPK(String pkName, Object pkVal, T pojo) {
        Query query = new Query(Criteria.where(pkName).is(pkVal));
        Update update = MongoUtil.pojoToUpdate(pojo);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, pojo.getClass());
        return writeResult;
    }

    /**
     * 根据字段更新pojo中不为null的字段到数据库
     *
     * @param fName 字段名称
     * @param fVal  值
     * @param pojo  要更新的数据
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public WriteResult updatePojoByField(String fName, Object fVal, T pojo) {
        Query query = new Query(Criteria.where(fName).is(fVal));
        Update update = MongoUtil.pojoToUpdate(pojo);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, pojo.getClass());
        return writeResult;
    }


    public WriteResult updatePojo(T conditionPojo, T updatePojo) {
        Criteria criteria = MongoUtil.pojoToCriteria(conditionPojo);
        Query query = criteria == null ? new Query() : new Query(criteria);
        Update update = MongoUtil.pojoToUpdate(updatePojo);
        return mongoTemplate.updateMulti(query, update, updatePojo.getClass());
    }


    /**
     * 根据pojo中的字段移除数据,只适合简单的非空字段
     *
     * @param pojo
     * @param <T>
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public <T> void removePojo(T pojo) {
        Criteria criteria = MongoUtil.pojoToCriteria(pojo);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, pojo.getClass());
    }

    /**
     * 获取数据库中满足条件的数据条数
     *
     * @param query
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> long count(Query query, Class<T> tClass) {
        return mongoTemplate.count(query, tClass);
    }

    /**
     * 获取分页pageRequest
     *
     * @param totalCount     总数据量
     * @param pageSize       单页数据量
     * @param currentPageNum 页码
     * @return
     */
    public Pageable getPageRequest(long totalCount, int pageSize, int currentPageNum) {

        //获取数据总页数
        double totalPageNum = Math.ceil(totalCount / (1.0 * pageSize));
        //校准入参，页码
        if (currentPageNum > totalPageNum) {
            currentPageNum = (int) totalPageNum;
        }
        if (currentPageNum < 1) {
            currentPageNum = 1;
        }

        Pageable pageable = new PageRequest(currentPageNum - 1, pageSize);
        return pageable;
    }


}
