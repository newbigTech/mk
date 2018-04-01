package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.MstFabric;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name MstFabricMapper
 * @description 面料等级mapper
 * @date 2017年5月26日10:52:23
 */

public interface MstFabricMapper extends Mapper<MstFabric> {
    void updateByFabricCode(MstFabric mstFabric);
}