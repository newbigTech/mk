package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hmall.mst.dto.RegionPointOfServiceMapping;
import org.apache.ibatis.annotations.Param;

import javax.persistence.Table;

/**
 * author: zhangzilong
 * name: RegionPointOfServiceMapping.java
 * discription:
 * date: 2017/12/25
 * version: 0.1
 */
public interface RegionPointOfServiceMappingMapper extends Mapper<RegionPointOfServiceMapping> {

    RegionPointOfServiceMapping selectByRegion(@Param("regionCode") String regionCode);

}
