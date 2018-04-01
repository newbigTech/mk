package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */


import com.hand.promotion.dto.HamllMstMainCarriage;

/**
 * @author XinyangMei
 * @Title IMainCarriageService
 * @Description 主干运费查询接口
 * @date 2017/6/28 19:02
 */
public interface IMainCarriageService {

    /**
     * 根据条件查询主干运费
     *
     * @param cityCode 城市编码
     * @param shipType 配送方式
     * @param status   主干运费状态,Y为启用,N为停用
     * @return
     */
    HamllMstMainCarriage getMainCarriageByCityCodeAndShipType(String cityCode, String shipType, String status);

    /**
     * 根据条件查询支干运费
     *
     * @param cityCode   城市编码
     * @param regionCode 门店地区编码
     * @param shipType   配送方式
     * @param status     主干运费状态,Y为启用,N为停用
     * @return
     */
    HamllMstMainCarriage getMainCarriageByCityRegionAndShipType(String cityCode, String regionCode, String shipType, String status);
}
