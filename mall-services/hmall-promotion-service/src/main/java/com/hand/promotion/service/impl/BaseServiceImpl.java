package com.hand.promotion.service.impl;

import com.hand.promotion.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name BaseServiceImpl
 * @description 公共Service
 * @date 2017/6/30 9:35
 */

public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    @Autowired
    private Mapper<T> mapper;

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#insertSelective(Object)
     */
    @Override
    public void insertSelective(T t) {
        mapper.insertSelective(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#updateByPrimaryKeySelective(Object)
     */
    @Override
    public void updateByPrimaryKeySelective(T t) {
        mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#selectByPrimaryKey(Object)
     */
    @Override
    public T selectByPrimaryKey(T t) {
        return mapper.selectByPrimaryKey(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#select(Object)
     */
    @Override
    public List<T> select(T t) {
        return mapper.select(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#selectAll()
     */
    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#selectOne(Object)
     */
    @Override
    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#delete(Object)
     */
    @Override
    public int delete(T t) {
        return mapper.delete(t);
    }

    /**
     * {@inheritDoc}
     *
     * @see IBaseService#deleteByPrimaryKey(Object)
     */
    @Override
    public int deleteByPrimaryKey(T t) {
        return mapper.deleteByPrimaryKey(t);
    }
}
