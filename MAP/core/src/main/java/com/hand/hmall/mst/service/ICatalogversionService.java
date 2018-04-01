package com.hand.hmall.mst.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Catalogversion;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 版本目录service接口
 * @date 2017年6月7日 10:00:04
 */
public interface ICatalogversionService extends IBaseService<Catalogversion>, ProxySelf<ICatalogversionService> {

    /**
     * @return
     * @description 关联两张版本目录表查询组合版本目录
     */
    public List<Catalogversion> selectCatalogVersion();

    /**
     * @param dto
     * @return
     * @description 查询ID
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