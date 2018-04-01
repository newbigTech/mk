package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Catalogversion;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CatalogversionMapper
 * @description 目录版本mapper
 * @date 2017年5月26日10:52:23
 */
public interface CatalogversionMapper extends Mapper<Catalogversion> {

    /**
     * 关联两张版本目录表查询组合版本目录
     *
     * @return
     */
    List<Catalogversion> selectCatalogVersion();

    /**
     * 查询ID
     *
     * @param dto
     * @return
     */
    Long selectCatalogversionId(Catalogversion dto);

    /**
     * 根据版本和目录编码获取目录版本ID
     *
     * @param paramMap
     * @return
     */
    Long getOnlineCatalogversionId(Map<String, String> paramMap);

    /**
     * 根据中文目录版本获得CatalogversionId
     *
     * @param catalogversion
     * @return
     */
    Long getCatalogversionIdByNameZhAndCatalogversion(Catalogversion catalogversion);
}