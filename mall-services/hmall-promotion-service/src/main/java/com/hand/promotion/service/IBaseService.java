package com.hand.promotion.service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name BaseService
 * @description BaseService
 * @date 2017/6/30 9:31
 */
public interface IBaseService<T> {

    /**
     * 插入实体类 只插入非空字段
     * @param t 实体类
     */
    void insertSelective(T t);

    /**
     * 根据主键更新实体类 只更新非空字段
     * @param t 实体类
     */
    void updateByPrimaryKeySelective(T t);

    /**
     * 根据主键查询实体类
     * @param t 实体类
     * @return 实体类
     */
    T selectByPrimaryKey(T t);

    /**
     * 查询实体类
     * @param t 实体类
     * @return 实体类
     */
    List<T> select(T t);

    /**
     * 无条件查询所有实体类
     * @return List<T>
     */
    List<T> selectAll();

    /**
     * 根据条件查询为一个实体类
     * 若查到多于一个，则报异常
     * @param t 实体类
     * @return 实体类
     */
    T selectOne(T t);

    /**
     * 删除一个实体类
     * @param t 实体类
     * @return 被影响的条数
     */
    int delete(T t);

    /**
     * 根据主键删除实体类
     * @param t 实体类
     * @return 被影响的条数
     */
    int deleteByPrimaryKey(T t);
}
