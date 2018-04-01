package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.HamllMstMainCarriage;
import com.hand.promotion.mapper.HamllMstMainCarriageMapper;
import com.hand.promotion.service.IMainCarriageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XinyangMei
 * @Title IMainCarriageServiceImpl
 * @Description 主线运费基础数据查询
 * @date 2017/6/28 19:03
 */
@Service
public class IMainCarriageServiceImpl implements IMainCarriageService {
    @Resource
    private HamllMstMainCarriageMapper hamllMstMainCarriageMapper;

    /**
     * 根据条件查询主干运费
     *
     * @param cityCode 城市编码
     * @param shipType 配送方式
     * @param status   主干运费状态,Y为启用,N为停用
     * @return
     */
    @Override
    public HamllMstMainCarriage getMainCarriageByCityCodeAndShipType(String cityCode, String shipType, String status) {
        return hamllMstMainCarriageMapper.selectByCityCodeAndShipType(cityCode, shipType, status);
    }

    /**
     * 根据条件查询支干运费
     *
     * @param cityCode   城市编码
     * @param regionCode 门店地区编码
     * @param shipType   配送方式
     * @param status     主干运费状态,Y为启用,N为停用
     * @return
     */
    @Override
    public HamllMstMainCarriage getMainCarriageByCityRegionAndShipType(String cityCode, String regionCode, String shipType, String status) {
        HamllMstMainCarriage hmallMstMainCarriage = new HamllMstMainCarriage();
        hmallMstMainCarriage.setCityCode(cityCode);
        hmallMstMainCarriage.setOrigin(regionCode);
        hmallMstMainCarriage.setShippingType(shipType);
        hmallMstMainCarriage.setStatus(status);
        List<HamllMstMainCarriage> mainCarriages = hamllMstMainCarriageMapper.select(hmallMstMainCarriage);
        return CollectionUtils.isEmpty(mainCarriages) ? null : mainCarriages.get(0);
    }
}
