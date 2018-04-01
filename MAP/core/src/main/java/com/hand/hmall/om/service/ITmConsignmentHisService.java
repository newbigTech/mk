package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.mdm.item.dto.LookupType;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.HmallTmConsignmentHis;

import java.util.List;

/**
 * @author 刘宏玺
 * @version 0.1
 * @name ITmConsignmentHisService
 * @description 天猫订单发货信息导出结果用接口
 * @date 2017/5/22
 */
public interface ITmConsignmentHisService extends IBaseService<HmallTmConsignmentHis>, ProxySelf<ITmConsignmentHisService> {

    /**
     * 导出所有的历史记录（倒序）
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<HmallTmConsignmentHis> queryAll(IRequest request, HmallTmConsignmentHis dto, int page, int pageSize);
}