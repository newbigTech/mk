package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsCompensateEntry;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IAsCompensateEntryService
 * @description 销售赔付单行Service接口
 * @date 2017/10/11
 */
public interface IAsCompensateEntryService extends IBaseService<AsCompensateEntry>, ProxySelf<IAsCompensateEntryService> {

    /**
     * 根据赔付单ID查询赔付单行信息
     *
     * @param dto
     */
    List<AsCompensateEntry> selectCompensateEntryById(int page, int pageSize, AsCompensateEntry dto);

    /**
     * 保存删除赔付单行
     *
     * @param dto
     * @return
     */
    ResponseData deleteCompensateEntryById(List<AsCompensateEntry> dto);

}