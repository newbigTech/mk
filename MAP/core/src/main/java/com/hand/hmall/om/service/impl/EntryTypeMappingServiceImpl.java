package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.EntryTypeMapping;
import com.hand.hmall.om.mapper.EntryTypeMappingMapper;
import com.hand.hmall.om.service.IEntryTypeMappingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name EntryTypeMappingServiceImpl
 * @description 订单行类型映射服务实现类
 * @date 2017/7/30 18:09
 */
@Service
public class EntryTypeMappingServiceImpl extends BaseServiceImpl<EntryTypeMapping> implements IEntryTypeMappingService {

    @Autowired
    private EntryTypeMappingMapper entryTypeMappingMapper;

    /**
     * 根据商品类型查询
     * @param productType 商品类型
     * @return
     */
    @Override
    public EntryTypeMapping selectOneByProductType(String productType) {
        EntryTypeMapping entryTypeMapping = new EntryTypeMapping();
        entryTypeMapping.setProductType(productType);
        List<EntryTypeMapping> entryTypeMappingList = entryTypeMappingMapper.select(entryTypeMapping);
        return CollectionUtils.isEmpty(entryTypeMappingList) ? null : entryTypeMappingList.get(0);
    }
}
