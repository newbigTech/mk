package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HamllMstSubCarriage;

import java.util.List;

/**
 * @author XinyangMei
 * @Title ISubCarriageService
 * @Description desp
 * @date 2017/6/28 19:35
 */
public interface ISubCarriageService {

    HamllMstSubCarriage getSubCarriageByDistinctAndShipType(String distinctCode, String shipType, String status);

    HamllMstSubCarriage getSubCarriageByDistinctRegionAndShipType(String distinctCode, String regionCode, String shipType, String status);

}
