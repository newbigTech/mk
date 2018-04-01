/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.servie.impl;

import com.hand.hmall.dto.HmallAsStatuslog;
import com.hand.hmall.mapper.HmallAsStatuslogMapper;
import com.hand.hmall.servie.IStatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 李伟
 * @version 1.0
 * @name:StatusUpdateServiceImpl
 * @Description: 根据服务单id返回状态日志信息实现类
 * @date 2017/8/17 20:32
 */
@Service
public class StatusUpdateServiceImpl implements IStatusUpdateService {

    @Autowired
    private HmallAsStatuslogMapper hmallAsStatuslogMapper;

    @Override
    public List<HmallAsStatuslog> queryByServiceId(Long serviceId)
    {
        return hmallAsStatuslogMapper.getHmallAsStatuslogs(serviceId);
    }
}
