package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmMailtemplate;

import java.util.List;

public interface IOmMailtemplateService extends IBaseService<OmMailtemplate>, ProxySelf<IOmMailtemplateService>{

    /**
     * 根据条件查询数据
     * @param iRequest
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    List<OmMailtemplate> selectByMailTemplate(IRequest iRequest, OmMailtemplate dto, int page, int pagesize);
}