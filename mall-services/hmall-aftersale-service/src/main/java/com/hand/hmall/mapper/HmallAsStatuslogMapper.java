/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallAsStatuslog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface HmallAsStatuslogMapper extends Mapper<HmallAsStatuslog> {

    List<HmallAsStatuslog> getHmallAsStatuslogs(@Param("serviceId") Long serviceId);

}