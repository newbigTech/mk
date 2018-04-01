package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.mapper.CatalogversionMapper;
import com.hand.hmall.mst.service.ICatalogversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CatalogversionServiceImpl
 * @description 实现类
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class CatalogversionServiceImpl extends BaseServiceImpl<Catalogversion> implements ICatalogversionService {

    @Autowired
    private CatalogversionMapper catalogversionMapper;

    /**
     * 关联两张版本目录表查询组合版本目录
     *
     * @return
     */
    @Override
    public List<Catalogversion> selectCatalogVersion() {
        return catalogversionMapper.selectCatalogVersion();
    }

    /**
     * 查询ID
     *
     * @param dto
     * @return
     */
    @Override
    public Long selectCatalogversionId(Catalogversion dto) {
        return catalogversionMapper.selectCatalogversionId(dto);
    }

    /**
     * 获取目录版本
     *
     * @param paramMap
     * @return
     */
    @Override
    public Long getOnlineCatalogversionId(Map<String, String> paramMap) {
        return catalogversionMapper.getOnlineCatalogversionId(paramMap);
    }

    @Override
    public Long getCatalogversionIdByNameZhAndCatalogversion(Catalogversion catalogversion) {
        return catalogversionMapper.getCatalogversionIdByNameZhAndCatalogversion(catalogversion);
    }


}