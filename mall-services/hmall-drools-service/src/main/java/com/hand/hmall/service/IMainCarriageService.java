package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HamllMstMainCarriage;

import java.util.List;

/**
 * @author XinyangMei
 * @Title IMainCarriageService
 * @Description desp
 * @date 2017/6/28 19:02
 */
public interface IMainCarriageService {

    HamllMstMainCarriage getMainCarriageByCityCodeAndShipType(String cityCode,String shipType,String status);

    HamllMstMainCarriage getMainCarriageByCityRegionAndShipType(String cityCode, String regionCode, String shipType, String status);
}
