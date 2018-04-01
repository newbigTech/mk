package com.hand.hmall.logistics.mq.service;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.hmall.logistics.pojo.ConsignmentInfo;
import com.hand.hmall.logistics.service.ILogisticsService4MS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author chenzhigang
 * @version 0.1
 * @name RRSOrderHFSListener
 * @description RRS消息监听处理
 * @date 2017/12/12
 */
@Service
public class RRSOrderMQListener {

    private static final Logger logger = LoggerFactory.getLogger(RRSOrderMQListener.class);

    @Autowired
    private ILogisticsService4MS logisticsService4MS;

    public ConsumeConcurrentlyStatus consumeMessage(MessageExt mes) {

        ConsignmentInfo consignmentInfo;

        try {
            consignmentInfo = new ObjectMapper().readValue(new String(mes.getBody(), "UTF-8"), ConsignmentInfo.class);
            logisticsService4MS.rrsOrderHfs(consignmentInfo);
        } catch (IOException e) {
            logger.error(RRSOrderMQListener.class.getCanonicalName(), e);
            throw new RuntimeException(e);
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
