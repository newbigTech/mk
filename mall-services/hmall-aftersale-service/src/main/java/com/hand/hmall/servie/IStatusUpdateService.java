/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.servie;

import com.hand.hmall.dto.HmallAsStatuslog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 李伟
 * @version 1.0
 * @name:IStatusUpdateService
 * @Description: 根据服务单id返回状态日志信息
 * @date 2017/8/17 20:30
 */
public interface IStatusUpdateService {

    /**
     * 根据服务单id返回状态日志信息
     * @param serviceId
     * @return
     */
    List<HmallAsStatuslog> queryByServiceId(@Param(value = "serviceId") Long serviceId);
}
