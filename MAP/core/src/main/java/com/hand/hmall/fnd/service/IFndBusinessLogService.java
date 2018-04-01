package com.hand.hmall.fnd.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.fnd.dto.FndBusinessLog;

import java.util.List;

public interface IFndBusinessLogService extends IBaseService<FndBusinessLog>, ProxySelf<IFndBusinessLogService>{

    /**
     * 操作日志标签页根据订单ID查询数据
     * @param dto   请求参数，包含订单ID
     * @param page  显示数据页数
     * @param pageSize     每页显示数据条数
     * @param requestContext   请求体
     * @return  查询结果
     */
    List<FndBusinessLog> query(IRequest requestContext, FndBusinessLog dto, int page, int pageSize);

}