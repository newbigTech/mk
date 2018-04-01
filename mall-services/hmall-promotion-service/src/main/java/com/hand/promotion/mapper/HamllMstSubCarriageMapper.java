package com.hand.promotion.mapper;

import com.hand.promotion.dto.HamllMstSubCarriage;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author xinyang.Mei
 * @desp 订单支线运费Mapper
 */
public interface HamllMstSubCarriageMapper extends Mapper<HamllMstSubCarriage> {


    /**
     * 根据条件查询支线运费数据
     *
     * @param districtCode 区域编码
     * @param shipType     配送方式
     * @param status       运费信息状态 Y为使用,N为不使用
     * @return
     */
    HamllMstSubCarriage selectByDistrictCodeAndShipType(String districtCode, String shipType, String status);


}