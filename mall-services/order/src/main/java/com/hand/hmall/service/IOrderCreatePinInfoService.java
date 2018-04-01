/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrderEntry;

/**
 * @author 李伟
 * @version 1.0
 * @name:IOrderCreatePinInfo
 * @Description: 订单行生成时，在pin码表中插入信息
 * @date 2017/8/8 19:47
 */
public interface IOrderCreatePinInfoService {

    ResponseData orderCreatePinInfo(HmallOmOrderEntry orderEntry);
}
