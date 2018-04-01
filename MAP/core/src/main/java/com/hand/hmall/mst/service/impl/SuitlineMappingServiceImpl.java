package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.SuitlineMapping;
import com.hand.hmall.mst.dto.SuitlineMappingDto;
import com.hand.hmall.mst.mapper.SuitlineMappingMapper;
import com.hand.hmall.mst.service.ISuitlineMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 套件映射的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class SuitlineMappingServiceImpl extends BaseServiceImpl<SuitlineMapping> implements ISuitlineMappingService {

    @Autowired
    private SuitlineMappingMapper suitlineMappingMapper;

    @Override
    public List<SuitlineMapping> getCountByProductHeadId(Long productHeaderId) {
        return suitlineMappingMapper.getCountByProductHeadId(productHeaderId);
    }

    @Override
    public List<SuitlineMappingDto> selectPushingSuitlineMapping() {
        // TODO Auto-generated method stub
        return suitlineMappingMapper.selectPushingSuitlineMapping();
    }

    @Override
    public void updateSuitlineMappingSyncflag(List<SuitlineMappingDto> dto) {
        suitlineMappingMapper.updateSuitlineMappingSyncflag(dto);
    }

    @Override
    public List<SuitlineMapping> selectInfo(IRequest request, SuitlineMapping dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return suitlineMappingMapper.selectInfo(dto);
    }

    @Override
    public List<SuitlineMapping> selectInfo(IRequest request, SuitlineMapping dto) {
        return suitlineMappingMapper.selectInfo(dto);
    }

    @Override
    public int deleteSuitlineMapping(SuitlineMapping dto) {
        return suitlineMappingMapper.deleteSuitlineMapping(dto);
    }

    @Override
    public void deleteByProductId(List<SuitlineMapping> dto) {
        suitlineMappingMapper.deleteByProductId(dto);
    }

    @Override
    public List<SuitlineMapping> selectAllById(List<SuitlineMapping> dto) {
        return suitlineMappingMapper.selectAllById(dto);
    }

    @Override
    public Long selectBysuitlineAndProductId(SuitlineMapping dto) {
        return suitlineMappingMapper.selectBysuitlineAndProductId(dto);
    }

}