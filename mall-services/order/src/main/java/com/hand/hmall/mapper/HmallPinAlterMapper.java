/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallPinAlter;
import tk.mybatis.mapper.common.Mapper;

public interface HmallPinAlterMapper extends Mapper<HmallPinAlter> {

    /**
     * 根据事件编号获取事件
     * @param eventCode
     * @return
     */
    HmallPinAlter getHmallPinAlter(String eventCode);
}