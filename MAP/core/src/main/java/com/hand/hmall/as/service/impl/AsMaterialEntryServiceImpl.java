package com.hand.hmall.as.service.impl;
import com.hand.hmall.as.dto.AsMaterialEntry;
import com.hand.hmall.as.mapper.AsMaterialEntryMapper;
import com.hand.hmall.as.service.IAsMaterialEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AsMaterialEntryServiceImpl extends BaseServiceImpl<AsMaterialEntry> implements IAsMaterialEntryService {
    @Autowired
    private AsMaterialEntryMapper asMaterialEntryMapper;
    @Override
    public List<AsMaterialEntry> getAllEntryByMaterialId(AsMaterialEntry dto) {
        return asMaterialEntryMapper.getAllEntryByMaterialId(dto);
    }
}