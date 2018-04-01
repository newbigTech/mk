package com.hand.hmall.pin.service;

import com.hand.hap.core.IRequest;
import com.hand.hmall.pin.dto.Pin;

import java.text.ParseException;

/**
 * author: zhangzilong
 * name: IPinAlterInfoService.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
public interface IPinAlterInfoService {

    /**
     *  将pin码插入到pin码预警信息表
     * @param pin 待插入pin码预警信息表的pin
     */
    void insertPinAlterInfo(Pin pin) throws ParseException;

    void sendMsg(IRequest iRequest);
}
