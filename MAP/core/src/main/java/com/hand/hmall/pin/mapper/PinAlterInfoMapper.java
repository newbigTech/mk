package com.hand.hmall.pin.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.pin.dto.PinAlterInfo;

import java.util.List;

/**
 * author: zhangzilong
 * name: PinAlterInfoMapper.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
public interface PinAlterInfoMapper extends Mapper<PinAlterInfo>{

    void insertOne(PinAlterInfo pinAlterInfo);

    void deleteOne(PinAlterInfo pinAlterInfo);

    List<PinAlterInfo> queryDelay1st();

    List<PinAlterInfo> queryDelay2nd();

    List<PinAlterInfo> queryDelay3rd();

    void deleteSent(List<PinAlterInfo> list);

    void updateSent1st(List<PinAlterInfo> list);

    void updateSent2nd(List<PinAlterInfo> list);
}
