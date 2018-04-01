package com.hand.hap.cloud.hpay.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.hap.cloud.hpay.entities.OutBoundLogs;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.utils.LogsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: zhangzilong
 * name: MessageSender
 * discription: 消息生产者
 * date: 2017/8/20
 * version: 0.1
 */
@Component
public class MessageSender {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitConfiguration rabbitConfiguration;

    private ExecutorService pool = Executors.newFixedThreadPool(1000);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMsg(ThirdPartyApiLogs thirdPartyApiLogs) {
        String filePath = generateFilePath();
        if (thirdPartyApiLogs.getRequestBody() != null) {
            pool.submit(new WriteFileJob("request", filePath, new String(thirdPartyApiLogs.getRequestBody())));
            thirdPartyApiLogs.setRequestBody(filePath + "request");
        }
        if (thirdPartyApiLogs.getResponseBody() != null) {
            pool.submit(new WriteFileJob("response", filePath, new String(thirdPartyApiLogs.getResponseBody())));
            thirdPartyApiLogs.setResponseBody(filePath + "response");
        }
        if (thirdPartyApiLogs.getExceptionStack() != null) {
            pool.submit(new WriteFileJob("exceptionStack", filePath, new String(thirdPartyApiLogs.getExceptionStack())));
            thirdPartyApiLogs.setExceptionStack(filePath + "exceptionStack");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        String content = null;
        try {
            objectMapper.writeValue(stringWriter, thirdPartyApiLogs);
            content = stringWriter.toString();
        } catch (IOException e) {
            content = "Object Map Error";
            logger.error(this.getClass().getName(), e);
        }
        MessageProperties p = new MessageProperties();
        p.setHeader("logType", rabbitConfiguration.getQueue());
        Message message = new Message(content.getBytes(), p);
        rabbitTemplate.convertAndSend(rabbitConfiguration.getExchangeName(), rabbitConfiguration.getQueue(), message);
    }

    public void sendMsg(OutBoundLogs outBoundLogs) {
        String filePath = generateFilePath();

        if (outBoundLogs.getRequestBody() != null) {
            pool.submit(new WriteFileJob("request", filePath, new String(outBoundLogs.getRequestBody())));
            outBoundLogs.setRequestBody(filePath + "request");
        }
        if (outBoundLogs.getResponseBody() != null) {
            pool.submit(new WriteFileJob("response", filePath, new String(outBoundLogs.getResponseBody())));
            outBoundLogs.setResponseBody(filePath + "response");
        }
        if (outBoundLogs.getExceptionStack() != null){
            pool.submit(new WriteFileJob("exceptionStack", filePath, new String(outBoundLogs.getExceptionStack())));
            outBoundLogs.setResponseBody(filePath + "exceptionStack");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        String content = null;
        try {
            objectMapper.writeValue(stringWriter, outBoundLogs);
            content = stringWriter.toString();
        } catch (IOException e) {
            content = "Object Map Error";
            logger.error(this.getClass().getName(), e);
        }
        MessageProperties p = new MessageProperties();
        p.setHeader("logType", rabbitConfiguration.getOutQueue());
        Message message = new Message(content.getBytes(), p);
        rabbitTemplate.convertAndSend(rabbitConfiguration.getExchangeName(), rabbitConfiguration.getOutQueue(), message);
    }
    private String generateFilePath() {
        Calendar now = Calendar.getInstance();
        String fullPath = getDirPathByTime(now) + UUID.randomUUID().toString().replace("-", "") + File.separator;
        File dir = new File(fullPath);
        if (!dir.exists())
            dir.mkdirs();
        // 自动创建下一个小时的文件目录
        autoMakeNextHourDir(now);
        return fullPath;
    }

    /**
     * 自动创建下一个小时的文件目录
     *
     * @param now
     */
    private void autoMakeNextHourDir(Calendar now) {
        now.add(Calendar.HOUR, 1);
        File dir = new File(getDirPathByTime(now));
        if (!dir.exists())
            dir.mkdirs();
    }

    private String getDirPathByTime(Calendar now) {
        return rabbitConfiguration.getLogFilePath() + new SimpleDateFormat("yyyyMMddhh").format(now.getTime()) + File.separator;
    }

    class WriteFileJob implements Runnable {

        private String filePath;

        private String content;

        private String type;

        public WriteFileJob(String type, String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
            this.type = type;
        }

        @Override
        public void run() {
            LogsUtils.saveMsg(content, type, filePath);
        }
    }
}
