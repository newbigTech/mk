package com.hand.hmall.mapper;

import com.hand.hmall.dto.HamllMstSubCarriage;
import tk.mybatis.mapper.common.Mapper;

public interface HamllMstSubCarriageMapper extends Mapper<HamllMstSubCarriage> {

    /**
     * @mbggenerated 2017-06-28
     */
    HamllMstSubCarriage selectByDistrictCodeAndShipType(String districtCode, String shipType, String status);


}