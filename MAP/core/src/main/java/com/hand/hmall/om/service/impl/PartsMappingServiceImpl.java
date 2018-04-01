package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.PartsMapping;
import com.hand.hmall.om.mapper.PartsMappingMapper;
import com.hand.hmall.om.service.IPartsMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name PartsMappingServiceImpl
 * @description 配件实现类
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class PartsMappingServiceImpl extends BaseServiceImpl<PartsMapping> implements IPartsMappingService {

    @Autowired
    PartsMappingMapper mapper;

    /**
     * 查询配件
     *
     * @param dto
     * @return
     */
    @Override
    public List<PartsMapping> selectParts(PartsMapping dto) {
        return mapper.selectParts(dto);
    }
}