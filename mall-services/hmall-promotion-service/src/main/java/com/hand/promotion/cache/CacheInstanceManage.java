package com.hand.promotion.cache;

import com.hand.promotion.util.ThreadPoolUtil;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinyangMei
 * @version V1.0
 * @date 2017/12/12
 * @description 管理所有的缓存实例
 */
public class CacheInstanceManage {
    @Value("${application.rocketMq.promotion.topic}")
    private String promotionTopic;
    @Autowired
    private MQPushConsumer pushConsumer;
    private List<HepBasicDataCacheInstance> cacheInstances;
    private Map<String, HepBasicDataCacheInstance> cacheInstanceMap;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public CacheInstanceManage(List<HepBasicDataCacheInstance> cacheInstances) {
        this.cacheInstances = cacheInstances;
        cacheInstanceMap = new ConcurrentHashMap<>();
        cacheInstances.forEach(hepBasicDataCacheInstance -> {
            cacheInstanceMap.put(hepBasicDataCacheInstance.getTag(), hepBasicDataCacheInstance);
        });
    }

    /**
     * 初始化所有缓存
     */
    public void init() {
        final CountDownLatch taskCounter = new CountDownLatch(this.cacheInstances.size());
        for (HepBasicDataCacheInstance hepBasicDataCache : this.cacheInstances) {
            initializeTask(hepBasicDataCache, taskCounter);
        }
        try {
            taskCounter.await();
        } catch (final InterruptedException e) {
            logger.error("waiting initialize cache data thread happened error!", e);
        }
        logger.info("########## initialize cache instance[" + this.cacheInstances.size() + "] already completed! ##########");
    }

    /**
     * 开始监听消息队列
     */
    public void start() {
        try {
            subscribe();
            logger.info("########## subscribe rocketMQ ! ##########");

        } catch (MQClientException e) {
            e.printStackTrace();
        }
        logger.info("########## redis cache data[" + this.cacheInstances.size() + "] AbstratcCacheListener channel is running! ##########");
    }

    /**
     * 为每一个缓存实例的初始化开启一个线程
     *
     * @param hepBasicDataCache
     * @param taskCounter
     */
    private void initializeTask(final HepBasicDataCacheInstance hepBasicDataCache,
                                final CountDownLatch taskCounter) {
        ThreadPoolUtil.submit(() -> {
            try {
                hepBasicDataCache.initialize();
            } catch (final Exception e) {
                logger.error("initialize cache data happened error!", e);
            } finally {
                taskCounter.countDown();
            }
        });
    }

    /**
     * 监听rocketmq，将消息转发给对应的缓存实例
     *
     * @throws MQClientException
     */
    public void subscribe() throws MQClientException {
        logger.info("-----------------begain subscribe ，pushConsumer hashcode {}---------", pushConsumer.hashCode());
        pushConsumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {

            boolean errFlag = false;
            for (MessageExt messageExt : list) {
                try {
                    String tags = messageExt.getTags();
                    String keys = messageExt.getKeys();
                    String msgStr = new String(messageExt.getBody());
                    //转发tag到对应的缓存实例
                    HepBasicDataCacheInstance hepBasicDataCacheInstance = cacheInstanceMap.get(tags);
                    if (null != hepBasicDataCacheInstance) {
                        logger.info("----接收到tag为{}的消息,分发给{}缓存实例", tags, hepBasicDataCacheInstance.getInstanceName());
                        hepBasicDataCacheInstance.dealData(msgStr);
                    } else {
                        logger.error("!!!!!!未找到tag对应的缓存实例！！！！！");
                        errFlag = true;
                    }
                } catch (Exception e) {
                    errFlag = true;
                    logger.error("!!!!!!消息处理异常！！！！！", e);
                }

            }
            if (errFlag) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;

            } else {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


            }
        });
        try {
            Thread.sleep(500);
            pushConsumer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            pushConsumer.start();
        }

        logger.info("-----------{} 监听器启动--------------", this.getClass());
    }

    /**
     * 获取所有缓存实例
     *  @return
     */
    public Map<String,HepBasicDataCacheInstance> getAllInstance(){
        return cacheInstanceMap;
    }


}
