package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallMstChannel;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallMstChannelMapper
 * @Description:渠道信息信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstChannelMapper extends Mapper<HmallMstChannel> {

    /**
     * 根据渠道编码查询渠道信息
     *
     * @param channelCode 渠道编码
     * @return
     */
    HmallMstChannel selectChannelByChannelCode(String channelCode);
}