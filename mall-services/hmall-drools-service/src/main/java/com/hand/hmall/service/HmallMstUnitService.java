package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.model.HmallMstUnit;

/**
 * @author XinyangMei
 * @Title HmallMstUnitService
 * @Description 产品单位逻辑接口
 * @date 2017/7/17 10:17
 */
public interface HmallMstUnitService {
    HmallMstUnit getUnitByCode(String code);
}
