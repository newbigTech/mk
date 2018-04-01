package com.hand.hmall.aspect;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.mapper.CodeMapper;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.StringUtils;
import com.markor.map.framework.rocketmq.RocketMQProducer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * author: zhangzilong
 * name: CodeAspect
 * discription:
 * date: 2017/12/15
 * version: 0.1
 */
@Aspect
@Component("codeAspect")
public class CodeAspect {

    @Autowired
    private RocketMQProducer producer;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Pointcut("execution(* com.hand.hap.system.service.impl.CodeServiceImpl.createCode(..)))")
    public void createCodePointCut() {

    }

    @Pointcut("execution(* com.hand.hap.system.service.impl.CodeServiceImpl.updateCode(..)))")
    public void updateCodeJoinPoint() {

    }

    @Pointcut("execution(* com.hand.hap.system.service.impl.CodeServiceImpl.batchDeleteValues(..)))")
    public void batchDeleteValuesJoinPoint() {

    }

    @Pointcut("execution(* com.hand.hap.system.service.impl.CodeServiceImpl.batchDelete(..)))")
    public void batchDeleteJoinPoint() {

    }

    @AfterReturning("createCodePointCut() || updateCodeJoinPoint()")
    public void afterCreateOrUpdateCodeReturn(JoinPoint joinPoint) {
        for (Object o : joinPoint.getArgs()) {
            if (o instanceof Code) {
                sendCode(((Code) o).getCode());
            }
        }
    }

    @AfterReturning("batchDeleteJoinPoint()")
    public void afterDeleteCodeReturn(JoinPoint joinPoint) {
        for (Object o : joinPoint.getArgs()) {
            if (o instanceof List) {
                for (Code code : (List<Code>) o) {
                    sendCode(code.getCode());
                }
            }
        }
    }

    @AfterReturning("batchDeleteValuesJoinPoint()")
    public void afterDeleteCodeValueReturn(JoinPoint joinPoint) {
        for (Object o : joinPoint.getArgs()) {
            if (o instanceof List) {
                com.hand.hap.system.dto.CodeValue codeValue = (com.hand.hap.system.dto.CodeValue) ((List) o).get(0);
                Code code = new Code();
                code.setCodeId(codeValue.getCodeId());
                String codeName = codeMapper.selectCodes(code).get(0).getCode();
                sendCode(codeName);
            }
        }
    }

    private void sendCode(String codeName) {
        try {
            Message message = new Message(Constants.MQ_TOPIC_SYS_CODE_SYNC, Constants.MQ_TAG_PREFIX_SYS_CODE_SYNC + codeName, codeName + System.currentTimeMillis() + new Random().nextInt(Integer.MAX_VALUE), "CHANGED".getBytes("UTF-8"));
            producer.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | MQClientException | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }
    }

}
