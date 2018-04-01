package com.hand.promotion.dao;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/18
 * @description 缓存 操作mongDB 特定接口
 */

public abstract class CacheDao<T> extends BaseMongoDao<T> {
    /**
     * 查询初始化缓存所需数据
     *
     * @return
     */
    public abstract List<T> findCacheInitData();
}
