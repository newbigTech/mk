package com.hand.promotion.cache;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description
 */
public interface CacheInstance<T> {
    /**
     * 向缓存中插入数据
     *
     * @param pojo
     */
    void insert(final T pojo);

    void update(final T data);

    void delete(final T data);

//    void subscribe() throws MQClientException;

    /**
     * 启动加载数据
     */
    void initialize();

    /**
     * 处理接受到的数据
     *
     * @param msgStr
     * @return
     */
    T dealData(String msgStr);

    /**
     * 获取缓存
     *
     * @return
     */
    List<T> getCache();

    /**
     * 根据缓存的第一个key获取对应的value
     *
     * @param key
     * @return
     */
    List<T> getListByKey(String key);

    /**
     * 根据促销活动或优惠券主键获取对应的促销活动或主键
     *
     * @param key
     * @return
     */
    T getByKey(String key);
}
