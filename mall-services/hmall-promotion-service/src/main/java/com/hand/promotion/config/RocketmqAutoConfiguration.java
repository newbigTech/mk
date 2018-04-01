package com.hand.promotion.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/14
 * @description
 */
@Configuration()
public class RocketmqAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${spring.application.name}")
    private String producerGroupName;

    @Value("${spring.application.name}")
    private String consumerGroupName;

    @Value("${application.rocketMq.producer.instanceName}")
    private String producerInsName;

    @Value("${application.rocketMq.consumer.instanceName}")
    private String consumerInsName;

    @Value("${application.rocketMq.vipChannelEnable}")
    private boolean vipChannelEnable;

    @Value("${application.rocketMq.nameSrvAddress}")
    private String nameSrvAddress;

    @Value("${application.rocketMq.promotion.topic}")
    private String promotionTopic;

    @Value("${application.rocketMq.promotion.tags}")
    private String tags;

    /**
     * 初始化向rocketmq发送普通消息的生产者
     */
    @Bean
    public DefaultMQProducer defaultProducer() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(nameSrvAddress);
        producer.setInstanceName(producerInsName);
        producer.setVipChannelEnabled(vipChannelEnable);
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        logger.info("RocketMq defaultProducer Started.");
        return producer;
    }


    /**
     * 初始化rocketmq消息监听方式的消费者
     */
    @Bean
    public DefaultMQPushConsumer pushConsumer() throws MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(nameSrvAddress);
        consumer.setInstanceName(consumerInsName);
        //设置消费为广播
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setVipChannelEnabled(vipChannelEnable);
        //设置批量消费，以提升消费吞吐量，默认是1
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.subscribe(promotionTopic, tags);
        return consumer;
    }
}
