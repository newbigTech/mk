package com.hand.hmall.pin.service.impl;

import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.mapper.ProfileValueMapper;
import com.hand.hap.system.service.IProfileService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinAlterInfo;
import com.hand.hmall.pin.dto.PinAlterMapping;
import com.hand.hmall.pin.mapper.PinAlterInfoMapper;
import com.hand.hmall.pin.mapper.PinAlterMappingMapper;
import com.hand.hmall.pin.service.IPinAlterInfoService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.PinAlertTimeCalculator;
import com.hand.hmall.util.StringUtils;
import com.hand.hmall.ws.client.IDreamPlatformClient;
import com.hand.hmall.ws.entities.MongateMULTIXRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: zhangzilong
 * name: IPinAlterInfoService.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
@Service
public class PinAlterInfoServiceImpl implements IPinAlterInfoService {

    @Autowired
    private PinAlterMappingMapper pinAlterMappingMapper;

    @Autowired
    private PinAlterInfoMapper pinAlterInfoMapper;

    @Autowired
    private ProfileValueMapper profileValueMapper;

    @Autowired
    private IDreamPlatformClient dreamPlatformClient;

    @Autowired
    private ILogManagerService logManagerService;

    private Logger logger = LoggerFactory.getLogger(PinAlterInfoServiceImpl.class);

    /**
     * 将pin码插入到pin码预警信息表
     *
     * @param pin 待插入pin码预警信息表的pin
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void insertPinAlterInfo(Pin pin) throws ParseException {

        // 插入pin码预警信息表：开始
        // 第一步：查出预警映射表中是否存在该事件的预警映射
        PinAlterMapping mapping = pinAlterMappingMapper.selectByPin(pin);
        // 第二步：删除当前PIN码在预警信息表中的记录
        PinAlterInfo pinAlterInfo = new PinAlterInfo();
        pinAlterInfo.setPin(pin.getCode());
        pinAlterInfoMapper.deleteOne(pinAlterInfo);

        // 判断：若存在该事件的预警映射，则将发送短信的时间计算出来，插入预警信息表
        if (mapping != null) {
            // ProfileValueMapper 获取SYS_PROFILE中设置的预警忽略时间区间
            String ignoreTime = profileValueMapper.selectPriorityValues("PIN_ALERT_IGNORE_TIME").get(0).getProfileValue();

            PinAlterInfo pai = new PinAlterInfo();
            pai.setPin(pin.getCode());
            pai.setNextEventCode(mapping.getNextEventCode());
            pai.setLevel1Time(PinAlertTimeCalculator.getAlertTime(pin.getOperationTime(), ignoreTime, mapping.getLevel1Time()));
            pai.setLevel2Time(PinAlertTimeCalculator.getAlertTime(pin.getOperationTime(), ignoreTime, mapping.getLevel2Time()));
            pai.setLevel3Time(PinAlertTimeCalculator.getAlertTime(pin.getOperationTime(), ignoreTime, mapping.getLevel3Time()));
            pai.setEventCode(pin.getEventCode());
            pinAlterInfoMapper.insertOne(pai);
        }
        // 结束
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sendMsg(IRequest iRequest) {

        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform("梦网短信平台");
        log.setProgramDescription("PIN码预警短信发送梦网");
        log = logManagerService.logBegin(iRequest, log);

        StringBuilder sb = new StringBuilder();

        // 第一步：查出3-2-1级预警应发
        List<PinAlterInfo> list3rd = pinAlterInfoMapper.queryDelay3rd();
        List<PinAlterInfo> list2nd = pinAlterInfoMapper.queryDelay2nd();
        List<PinAlterInfo> list1st = pinAlterInfoMapper.queryDelay1st();
        // 从2级和1级预警中移除3级，从1级预警中移除2级
        if (!CollectionUtils.isEmpty(list3rd) && !CollectionUtils.isEmpty(list2nd)) {
            removeSame(list2nd, list3rd);
        }
        if (!CollectionUtils.isEmpty(list3rd) && !CollectionUtils.isEmpty(list1st)) {
            removeSame(list1st, list3rd);
        }
        if (!CollectionUtils.isEmpty(list2nd) && !CollectionUtils.isEmpty(list1st)) {
            removeSame(list1st, list2nd);
        }
        if (CollectionUtils.isEmpty(list2nd) && CollectionUtils.isEmpty(list1st) && CollectionUtils.isEmpty(list3rd)) {
            log.setMessage("S");
            log.setReturnMessage("无预警PIN码。");
            return;
        }
        // 拼接短信内容
        try {
            if (!CollectionUtils.isEmpty(list3rd)) {
                appendMessages(sb, list3rd);
            }
            if (!CollectionUtils.isEmpty(list2nd)) {
                appendMessages(sb, list2nd);
            }
            if (!CollectionUtils.isEmpty(list1st)) {
                appendMessages(sb, list1st);
            }
        } catch (UnsupportedEncodingException e) {
            // 转码失败
            log.setProcessStatus("E");
            log.setReturnMessage(e.getMessage());
            logManagerService.logEnd(iRequest, log);
        }
        // 移除第一个逗号
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        log.setMessage(sb.toString());
        // 每100条短信发送一次，每100条分隔一次
        List<String> msgForSend = splitStringBy100(sb.toString());
        // 拼装请求
        MongateMULTIXRequest request = new MongateMULTIXRequest();
        Properties properties = getProperties();
        request.setUserId(properties.getProperty("dream.userId"));
        request.setPassword(properties.getProperty("dream.password"));
        StringBuilder resultStr = new StringBuilder();
        // 调用接口发送短信
        for (String msg : msgForSend) {
            request.setMultixmt(msg);
            try {
                resultStr.append(dreamPlatformClient.sendMultixMsg(request).getMongateMULTIXSendResult());
            } catch (WSCallException e) {
                // 短信发送失败
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
            }
        }
        // 更新数据库
        if (!CollectionUtils.isEmpty(list3rd)) {
            pinAlterInfoMapper.deleteSent(list3rd);
        }
        if (!CollectionUtils.isEmpty(list2nd)) {
            pinAlterInfoMapper.updateSent2nd(list2nd);
        }
        if (!CollectionUtils.isEmpty(list1st)) {
            pinAlterInfoMapper.updateSent1st(list1st);
        }

        log.setProcessStatus("S");
        log.setReturnMessage(resultStr.toString());

        logManagerService.logEnd(iRequest, log);
    }

    private void appendMessages(StringBuilder sb, List<PinAlterInfo> list) throws UnsupportedEncodingException {
        String header = ",0|*|";
        String split = "|";
        for (PinAlterInfo pin : list) {
            if (!StringUtils.isEmpty(pin.getMobile()) && Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$").matcher(pin.getMobile()).matches()) {
                String hours;
                if (pin.getLevel3DelayHours() != null) {
                    hours = pin.getLevel3DelayHours().toString();
                } else if (pin.getLevel2DelayHours() != null) {
                    hours = pin.getLevel2DelayHours().toString();
                } else {
                    hours = pin.getLevel1DelayHours().toString();
                }
                String s = "PIN码" + pin.getPin() + "(" + pin.getVcode() + ")在" + pin.getNextEventCode().substring(0, 3) + "发起" + pin.getNextEventDes() + "已超过" + hours + "小时未解决。";
                sb.append(header);
                sb.append(pin.getMobile());
                sb.append(split);
                logger.info("短信内容>>>>>>>>>>" + s);
                sb.append(new String(Base64.encodeBase64(s.getBytes("GBK"))));
            }
        }
    }

    private void removeSame(List<PinAlterInfo> listLower, List<PinAlterInfo> listHigh) {
        Iterator<PinAlterInfo> it = listLower.iterator();
        for (PinAlterInfo pin : listHigh) {
            while (it.hasNext()) {
                if (it.next().getPin().equals(pin.getPin())) {
                    it.remove();
                    break;
                }
            }
            it = listLower.iterator();
        }
    }

    private List<String> splitStringBy100(String longText) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("[,]").matcher(longText);
        int index = 0;
        while (matcher.find()) {
            index++;
            if (index == 100) {
                result.add(longText.substring(0, matcher.start()));
                longText = longText.substring(matcher.start() + 1, longText.length());
                matcher = Pattern.compile("[,]").matcher(longText);
                index = 0;
            }
        }
        result.add(longText);
        return result;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
