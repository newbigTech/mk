package com.hand.hmall.om.service;

import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.EntryTypeMapping;

/**
 * @author 马君
 * @version 0.1
 * @name IEntryTypeMappingService
 * @description 订单行类型映射service
 * @date 2017/7/30 18:08
 */
public interface IEntryTypeMappingService extends IBaseService<EntryTypeMapping> {

    /**
     * 根据商品类型查询
     * @param productType 商品类型
     * @return EntryTypeMapping
     */
    EntryTypeMapping selectOneByProductType(String productType);
}
