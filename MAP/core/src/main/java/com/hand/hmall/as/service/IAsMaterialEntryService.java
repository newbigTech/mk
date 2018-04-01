package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsMaterialEntry;

import java.util.List;

public interface IAsMaterialEntryService extends IBaseService<AsMaterialEntry>, ProxySelf<IAsMaterialEntryService>{

    /**
     * 根据物耗单id 获取所有物耗单行
     * @param dto
     * @return
     */
    List<AsMaterialEntry> getAllEntryByMaterialId(AsMaterialEntry dto);
}