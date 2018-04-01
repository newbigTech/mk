package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HamllMstSubCarriage;
import com.hand.hmall.mapper.HamllMstSubCarriageMapper;
import com.hand.hmall.service.ISubCarriageService;
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

    public HamllMstSubCarriage getSubCarriageByDistinctAndShipType(String distinctCode, String shipType, String status) {
        return hamllMstSubCarriageMapper.selectByDistrictCodeAndShipType(distinctCode, shipType, status);
    }

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
