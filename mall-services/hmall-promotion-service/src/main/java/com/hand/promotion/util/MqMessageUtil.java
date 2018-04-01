package com.hand.promotion.util;

import com.alibaba.fastjson.JSON;

import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.CacheOperater;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/14
 * @description rocketMq 促销消息工具类
 */
@Component
public class MqMessageUtil {

    @Value("${application.rocketMq.promotion.topic}")
    private String promotionTopic;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取促销Message
     *
     * @param operater
     * @param orderTag
     * @param pojo
     * @return
     * @throws UnsupportedEncodingException
     */
    public Message getOrderPromotionMessage(CacheOperater operater, String orderTag, PromotionActivitiesPojo pojo) {

        pojo.setCacheOperator(operater.name());
        Message message = new Message(promotionTopic, orderTag, JSON.toJSONBytes(pojo));
        message.setKeys(pojo.getId());
        return message;
    }

    /**
     * 获取优惠券Message
     *
     * @param operater
     * @param couponTag
     * @param pojo
     * @return
     * @throws UnsupportedEncodingException
     */
    public Message getCouponMessage(CacheOperater operater, String couponTag, PromotionCouponsPojo pojo) {

        pojo.setCacheOperator(operater.name());
        Message message = new Message(promotionTopic, couponTag, JSON.toJSONBytes(pojo));
        message.setKeys(pojo.getId());
        return message;
    }

    /**
     * 有序发送促销活动/优惠券信息
     */
    public void sendOrderly(DefaultMQProducer defaultMQProducer, Message message, String key) {
        try {
            SendResult sendResult = defaultMQProducer.send(message, (list, msg, o) -> {
                logger.info("message size:>>>>>>>>" + list.size());
                //用要发送消息的主键与队列数量取模
                int index = Math.abs(o.hashCode() % list.size());
                logger.info("message index:>>>>>>>>" + index);

                //返回消息要发送的quene
                return list.get(index);
            }, key);
            logger.info(">>>>>>>>发送结果{}",sendResult );
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
