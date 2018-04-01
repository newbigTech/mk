package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.PinAlterInfoMapper;
import com.hand.hmall.mapper.PinAlterMappingMapper;
import com.hand.hmall.mapper.ProfileValueMapper;
import com.hand.hmall.model.HmallPinInfo;
import com.hand.hmall.model.PinAlterInfo;
import com.hand.hmall.model.PinAlterMapping;
import com.hand.hmall.service.IPinAlterInfoService;
import com.hand.hmall.util.PinAlertTimeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

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

    /**
     * 将pin码插入到pin码预警信息表
     *
     * @param pin 待插入pin码预警信息表的pin
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void insertPinAlterInfo(HmallPinInfo pin) throws ParseException {

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
}
