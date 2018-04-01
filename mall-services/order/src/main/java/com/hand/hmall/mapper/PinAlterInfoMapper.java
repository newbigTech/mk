package com.hand.hmall.mapper;


import com.hand.hmall.model.PinAlterInfo;
import tk.mybatis.mapper.common.Mapper;

/**
 * author: zhangzilong
 * name: PinAlterInfoMapper.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
public interface PinAlterInfoMapper extends Mapper<PinAlterInfo> {

    void insertOne(PinAlterInfo pinAlterInfo);

    void deleteOne(PinAlterInfo pinAlterInfo);

}
