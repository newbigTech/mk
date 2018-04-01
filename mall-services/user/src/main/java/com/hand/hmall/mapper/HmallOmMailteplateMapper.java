/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallOmMailteplate;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 李伟
 * @version 1.0
 * @name:HmallOmMailteplateMapper
 * @Description:
 * @date 2017/8/2 17:26
 */
public interface HmallOmMailteplateMapper extends Mapper<HmallOmMailteplate> {

    HmallOmMailteplate selectBySendType(String sendType);

}
