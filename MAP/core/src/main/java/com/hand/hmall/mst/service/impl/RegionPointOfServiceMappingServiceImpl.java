package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.RegionPointOfServiceMapping;
import com.hand.hmall.mst.mapper.RegionPointOfServiceMappingMapper;
import com.hand.hmall.mst.service.IRegionPointOfServiceMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author: zhangzilong
 * name: RegionPointOfServiceMappingServiceImpl.java
 * discription:
 * date: 2017/12/25
 * version: 0.1
 */
@Service
public class RegionPointOfServiceMappingServiceImpl extends BaseServiceImpl<RegionPointOfServiceMapping> implements IRegionPointOfServiceMappingService{

    @Autowired
    private RegionPointOfServiceMappingMapper mapper;

    @Override
    public RegionPointOfServiceMapping findByRegionCode(String regionCode) {
        return mapper.selectByRegion(regionCode);
    }
}
