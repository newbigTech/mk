package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsCompensateEntry;
import com.hand.hmall.as.mapper.AsCompensateEntryMapper;
import com.hand.hmall.as.service.IAsCompensateEntryService;
import com.hand.hmall.as.service.IAsCompensateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateEntryServiceImpl
 * @description 销售赔付单行表Service实现类
 * @date 2017/10/11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AsCompensateEntryServiceImpl extends BaseServiceImpl<AsCompensateEntry> implements IAsCompensateEntryService {

    @Autowired
    private AsCompensateEntryMapper asCompensateEntryMapper;
    @Autowired
    private IAsCompensateService asCompensateService;

    @Override
    public List<AsCompensateEntry> selectCompensateEntryById(int page, int pageSize, AsCompensateEntry dto) {
        PageHelper.startPage(page, pageSize);
        return asCompensateEntryMapper.selectCompensateEntryById(dto);
    }

    @Override
    public ResponseData deleteCompensateEntryById(List<AsCompensateEntry> dto) {
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {
            for (AsCompensateEntry asCompensateEntry : dto) {
                asCompensateEntryMapper.deleteByPrimaryKey(asCompensateEntry);
            }
            responseData.setSuccess(true);
        } else {
            responseData.setSuccess(false);
        }
        return responseData;
    }
}