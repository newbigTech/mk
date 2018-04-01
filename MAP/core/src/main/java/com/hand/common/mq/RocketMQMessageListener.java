package com.hand.common.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.hand.hmall.logistics.mq.service.RRSOrderMQListener;
import com.hand.hmall.mq.service.BOMReviewResultMQListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Rocket MQ 消息监听类
 *
 * @author alaowan
 * Created at 2017/12/27 13:10
 * @description
 */
public class RocketMQMessageListener implements MessageListenerConcurrently {

    @Autowired
    private RRSOrderMQListener rrsOrderMQListener;

    @Autowired
    private BOMReviewResultMQListener bomReviewResultMQListener;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessageExt msg = list.get(0);
        if (msg.getTopic().equals("logistics")) {
            return rrsOrderMQListener.consumeMessage(msg);
        } else if (msg.getTopic().equals("MAP_BOM_REVIEW")) {
            try {
                return bomReviewResultMQListener.consumeMessage(msg);
            } catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
