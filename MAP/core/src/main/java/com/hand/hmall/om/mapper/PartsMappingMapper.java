package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.PartsMapping;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name PartsMappingMapper
 * @description 配件mapper
 * @date 2017年5月26日10:52:23
 */
public interface PartsMappingMapper extends Mapper<PartsMapping> {
    /**
     * 查询配件
     *
     * @param dto
     * @return
     */
    List<PartsMapping> selectParts(PartsMapping dto);
}