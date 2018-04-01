package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.OmPromoteRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hmall.dto.OmPromoteRecord;
import com.hand.hmall.service.IOmPromoteRecordService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmPromoteRecordServiceImpl implements IOmPromoteRecordService{
    @Autowired
    private OmPromoteRecordMapper omPromoteRecordMapper;

    @Override
    public long insert(OmPromoteRecord omPromoteRecord) {
        omPromoteRecordMapper.createRecord(omPromoteRecord);
        return omPromoteRecord.getRecordId();
    }

    @Override
    public int countByConditon(OmPromoteRecord omPromoteRecord) {
        return omPromoteRecordMapper.countByConditon(omPromoteRecord);
    }

    @Override
    public OmPromoteRecord findByCondition(OmPromoteRecord omPromoteRecord) {
        return omPromoteRecordMapper.selectByCondition(omPromoteRecord);
    }

    @Override
    public int updateOmPromoteRecord(OmPromoteRecord omPromoteRecord) {
        return omPromoteRecordMapper.updateByPrimaryKeySelective(omPromoteRecord);
    }


}