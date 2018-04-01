package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Logisticsco;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 承运商对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface LogisticscoMapper extends Mapper<Logisticsco> {

    /**
     * 运费当中的承运商查询
     *
     * @param dto
     * @return
     */
    List<Logisticsco> logisticsoLov(Logisticsco dto);
}