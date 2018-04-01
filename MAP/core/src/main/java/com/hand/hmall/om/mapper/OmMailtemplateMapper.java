package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmMailtemplate;

import java.util.List;

public interface OmMailtemplateMapper extends Mapper<OmMailtemplate>{

    /**
     * 根据条件查询数据
     * @param dto
     * @return
     */
    List<OmMailtemplate> selectByMailTemplate(OmMailtemplate dto);

}