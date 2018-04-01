package com.hand.hmall.om.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.PartsMapping;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name IPartsMappingService
 * @description 配件service
 * @date 2017年5月26日10:52:23
 */
public interface IPartsMappingService extends IBaseService<PartsMapping>, ProxySelf<IPartsMappingService> {
    /**
     * 查询配件
     *
     * @param dto
     * @return
     */
    List<PartsMapping> selectParts(PartsMapping dto);
}