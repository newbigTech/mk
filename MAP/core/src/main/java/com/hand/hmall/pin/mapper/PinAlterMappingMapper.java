package com.hand.hmall.pin.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinAlterMapping;

/**
 * author: zhangzilong
 * name: PinAlterMappingMapper.java
 * discription: pin码预警映射表
 * date: 2017/11/17
 * version: 0.1
 */
public interface PinAlterMappingMapper extends Mapper<PinAlterMapping> {

    PinAlterMapping selectByPin(Pin pin);
}
