package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OmAccountsRecord;
import com.hand.hmall.om.mapper.OmAccountsRecordMapper;
import com.hand.hmall.om.service.IOmAccountsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmAccountsRecordServiceImpl extends BaseServiceImpl<OmAccountsRecord> implements IOmAccountsRecordService {

    @Autowired
    private OmAccountsRecordMapper omAccountsRecordMapper;

    /**
     * 根据条件查找对应的账单记录
     *
     * @param condition
     * @return
     */
    @Override
    public OmAccountsRecord getOmAccountsRecord(Map<String, String> condition) {
        return omAccountsRecordMapper.getRecordByCondition(condition);
    }
}