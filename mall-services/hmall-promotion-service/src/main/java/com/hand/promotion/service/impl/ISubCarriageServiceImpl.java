package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.HamllMstSubCarriage;
import com.hand.promotion.mapper.HamllMstSubCarriageMapper;
import com.hand.promotion.service.ISubCarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author XinyangMei
 * @Title ISubCarriageServiceImpl
 * @Description 支线运费数据查询
 * @date 2017/6/28 19:36
 */
@Service
public class ISubCarriageServiceImpl implements ISubCarriageService {
    @Autowired
    private HamllMstSubCarriageMapper hamllMstSubCarriageMapper;

    /**
     * 根据区域编码,配送方式查询支线基础运费数据
     *
     * @param distinctCode 地区编码
     * @param shipType     配送方式
     * @param status       运费状态,Y为启用,N为停用
     * @return
     */
    @Override
    public HamllMstSubCarriage getSubCarriageByDistinctAndShipType(String distinctCode, String shipType, String status) {
        return hamllMstSubCarriageMapper.selectByDistrictCodeAndShipType(distinctCode, shipType, status);
    }

    /**
     * 根据区域编码,配送方式查询支线基础运费数据
     *
     * @param distinctCode 地区编码
     * @param regionCode   门店地区编码
     * @param shipType     配送方式
     * @param status       运费状态,Y为启用,N为停用
     * @return
     */
    @Override
    public HamllMstSubCarriage getSubCarriageByDistinctRegionAndShipType(String distinctCode, String regionCode, String shipType, String status) {
        HamllMstSubCarriage subCarriage = new HamllMstSubCarriage();
        subCarriage.setDistrictCode(distinctCode);
        subCarriage.setOrigin(regionCode);
        subCarriage.setStatus(status);
        subCarriage.setShippingType(shipType);
        List<HamllMstSubCarriage> mstSubCarriages = hamllMstSubCarriageMapper.select(subCarriage);
        return CollectionUtils.isEmpty(mstSubCarriages) ? null : mstSubCarriages.get(0);
    }
}
