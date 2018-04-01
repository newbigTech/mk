package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesEntry;

import java.util.List;

/**
 * @author zhangzilong
 * @version 0.1
 * @name ISvsalesEntryService
 * @description 服务销售单行Service接口
 * @date 2017/7/28
 */
public interface ISvsalesEntryService extends IBaseService<SvsalesEntry>, ProxySelf<ISvsalesEntryService> {
    /**
     * 查询售后单关联的售后单行
     *
     * @param dto
     * @return
     */
    List<SvsalesEntry> querySvsalesEntriesInfo(IRequest iRequest, Svsales dto, int page, int pagesize);


}