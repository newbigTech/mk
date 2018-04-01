package com.hand.hmall.mst.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.RegionPointOfServiceMapping;

/**
 * author: zhangzilong
 * name: IRegionPointOfServiceMappingService.java
 * discription:
 * date: 2017/12/25
 * version: 0.1
 */
public interface IRegionPointOfServiceMappingService extends IBaseService<RegionPointOfServiceMapping>,ProxySelf<RegionPointOfServiceMapping> {

    RegionPointOfServiceMapping findByRegionCode(String regionCode);

}
