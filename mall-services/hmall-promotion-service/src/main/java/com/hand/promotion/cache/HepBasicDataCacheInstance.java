package com.hand.promotion.cache;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dao.CacheDao;
import com.hand.promotion.pojo.enums.CacheOperater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XinyangMei
 * @Title HepBasicDataCacheInstance
 * @Description 抽象缓存实例模板
 * @date 2017/12/12 9:53
 */
public abstract class HepBasicDataCacheInstance<T> implements CacheInstance<T> {
    private static final Logger LOG = LoggerFactory.getLogger(HepBasicDataCacheInstance.class);


    private final String instanceName;

    private final CacheDao<T> baseMongoDao;


    private final String initializeMessage;

    private String tag;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public HepBasicDataCacheInstance(final String instanceName, final String tag,
                                     CacheDao<T> baseMongoDao) {
        this.instanceName = instanceName.toLowerCase();
        this.tag = tag;
        this.baseMongoDao = baseMongoDao;
        this.initializeMessage = "### initialize " + instanceName + " cache [%d] complete cost time: %d ###";
    }



    /**
     * 初始化实例缓存
     */
    @Override
    public void initialize() {
        final long beginTime = System.currentTimeMillis();
        final AtomicInteger initCounter = new AtomicInteger(0);
        //查询要加载到缓存中的数据
        List<T> initDatas = baseMongoDao.findCacheInitData();
        for (T initData : initDatas) {
            insert(initData);
            initCounter.incrementAndGet();
        }

        LOG.info(String.format(this.initializeMessage, initCounter.get(), System.currentTimeMillis() - beginTime));
    }

    /**
     * 根据信息的 operate字段，对缓存进行操作
     */
    public void switchOperate(String operaterStr, T pojo) {
        CacheOperater cacheOperater = CacheOperater.valueOf(operaterStr);
        switch (cacheOperater) {
            case insert:
                insert(pojo);
                break;
            case delete:
                delete(pojo);
                break;
            case update:
                update(pojo);
                break;
            default:
                logger.error("---------Message 操作类型不存在-------------");
        }
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getTag() {
        return tag;
    }
}
