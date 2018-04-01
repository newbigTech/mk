package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */


import com.hand.promotion.dto.HamllMstSubCarriage;

/**
 * @author XinyangMei
 * @Title ISubCarriageService
 * @Description 支线运费基础数据查询接口
 * @date 2017/6/28 19:35
 */
public interface ISubCarriageService {

    /**
     * 根据区域编码,配送方式查询支线基础运费数据
     *
     * @param distinctCode 地区编码
     * @param shipType     配送方式
     * @param status       运费状态,Y为启用,N为停用
     * @return
     */
    HamllMstSubCarriage getSubCarriageByDistinctAndShipType(String distinctCode, String shipType, String status);

    /**
     * 根据区域编码,配送方式查询支线基础运费数据
     *
     * @param distinctCode 地区编码
     * @param regionCode   门店地区编码
     * @param shipType     配送方式
     * @param status       运费状态,Y为启用,N为停用
     * @return
     */
    HamllMstSubCarriage getSubCarriageByDistinctRegionAndShipType(String distinctCode, String regionCode, String shipType, String status);

}
