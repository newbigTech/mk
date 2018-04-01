package com.hand.common.configurations;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zhangwantao
 * @version 0.1
 * @name RedissonConfiguration
 * @description 读取config.properties文件，并初始化Redisson客户端
 * @date 2017/8/7
 */
@Configuration
public class RedissonConfiguration {

    private Properties properties;

    /**
     * 获取Rbucket对象
     *
     * @param redissonClient
     * @param objectName
     * @return RBucket<T>
     */
    public static <T> RBucket<T> getBucket(RedissonClient redissonClient, String objectName) {
        RBucket<T> rBucket = redissonClient.getBucket(objectName);
        return rBucket;
    }

    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        boolean useSentinel = Boolean.valueOf(getProperty("redis.useSentinel", "false"));
        int db = Integer.parseInt(getProperty("redis.db", "0"));
        String password = getProperty("redis.password", null);
        if (useSentinel) { // 哨兵模式
            String sentinel = getProperty("redis.sentinel", null);
            String[] sentinelAddresses = sentinel.split(",");
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setDatabase(db);
            if (!"".equals(password))
                sentinelServersConfig.setPassword(password);
            for (int i = 0; i < sentinelAddresses.length; i++) {
                sentinelServersConfig.addSentinelAddress(sentinelAddresses[i]);
            }
        } else { // 单机模式
            String connectionString = getProperty("redis.ip", null) + ":" + getProperty("redis.port", "6379");
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(connectionString)
                    .setDatabase(db);
            if (!"".equals(password))
                singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    private String getProperty(String key, String defaultValue) throws IOException {
        return getProperties().getProperty(key, defaultValue);
    }

    private Properties getProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        }
        return properties;
    }
}
