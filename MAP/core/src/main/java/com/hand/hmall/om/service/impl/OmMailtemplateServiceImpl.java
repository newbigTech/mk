package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OmMailtemplate;
import com.hand.hmall.om.mapper.OmMailtemplateMapper;
import com.hand.hmall.om.service.IOmMailtemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OmMailtemplateServiceImpl extends BaseServiceImpl<OmMailtemplate> implements IOmMailtemplateService{

    @Autowired
    private OmMailtemplateMapper mailtemplateMapper;

    /**
     * 根据条件查询数据
     * @param iRequest
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OmMailtemplate> selectByMailTemplate(IRequest iRequest, OmMailtemplate dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mailtemplateMapper.selectByMailTemplate(dto);
    }
}