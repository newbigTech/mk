package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesEntry;
import com.hand.hmall.as.mapper.SvsalesEntryMapper;
import com.hand.hmall.as.service.ISvsalesEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ServiceorderEntryServiceImpl
 * @description 服务单行ServiceImpl
 * @date 2017/7/19
 */
@Service
@Transactional
public class SvsalesEntryServiceImpl extends BaseServiceImpl<SvsalesEntry> implements ISvsalesEntryService {
    @Autowired
    private SvsalesEntryMapper svsalesEntryMapper;

    /**
     * 查询售后单关联的售后单行
     *
     * @param dto
     * @return
     */
    @Override
    public List<SvsalesEntry> querySvsalesEntriesInfo(IRequest iRequest, Svsales dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return svsalesEntryMapper.querySvsalesEntyiedInfo(dto);
    }

}