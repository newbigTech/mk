package com.hand.hmall.as.mapper;


import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsMaterialEntry;

import java.util.List;

public interface AsMaterialEntryMapper extends Mapper<AsMaterialEntry> {
    /**
     * 根据物耗单id 获取所有物耗单行
     * @param dto
     * @return
     */
    List<AsMaterialEntry> getAllEntryByMaterialId(AsMaterialEntry dto);
}