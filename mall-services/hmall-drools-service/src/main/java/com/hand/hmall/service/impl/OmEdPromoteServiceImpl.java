package com.hand.hmall.service.impl;

import com.hand.hmall.dto.OmEdPromote;
import com.hand.hmall.mapper.OmEdPromoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hmall.service.IOmEdPromoteService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmEdPromoteServiceImpl implements IOmEdPromoteService{

    @Autowired
    private OmEdPromoteMapper omEdPromoteMapper;

    @Override
    public int insert(OmEdPromote omEdPromote) {
        return omEdPromoteMapper.insert(omEdPromote);
    }

    @Override
    public int update(OmEdPromote omEdPromote) {
        return omEdPromoteMapper.updateByPrimaryKey(omEdPromote);
    }

    @Override
    public OmEdPromote findByPrimaryKey(OmEdPromote omEdPromote) {
        return omEdPromoteMapper.selectByPrimaryKey(omEdPromote);
    }

    @Override
    public List<OmEdPromote> queryCondition(OmEdPromote omEdPromote) {
        return omEdPromoteMapper.queryCondition(omEdPromote);
    }

    @Override
    public OmEdPromote findOne(OmEdPromote omEdPromote) {
        return omEdPromoteMapper.selectOne(omEdPromote);
    }
}