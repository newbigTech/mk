package com.hand.hmall.mapper;


import com.hand.hmall.model.HmallPinInfo;
import com.hand.hmall.model.PinAlterMapping;
import tk.mybatis.mapper.common.Mapper;

/**
 * author: zhangzilong
 * name: PinAlterMappingMapper.java
 * discription: pin码预警映射表
 * date: 2017/11/17
 * version: 0.1
 */
public interface PinAlterMappingMapper extends Mapper<PinAlterMapping> {

    PinAlterMapping selectByPin(HmallPinInfo pin);
}
