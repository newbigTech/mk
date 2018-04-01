package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsTmrefund;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IAsTmrefundService
 * @description 天猫退款单导入Service类
 * @date 2017/9/14
 */
public interface IAsTmrefundService extends IBaseService<AsTmrefund>, ProxySelf<IAsTmrefundService> {
    /**
     * 根据code查询天猫退货单表
     *
     * @param code
     * @return
     */
    List<AsTmrefund> selectByCode(String code);

    void batchInsertAsTmrefund(IRequest iRequest, List<AsTmrefund> asTmrefundList);

    /**
     * 天猫退款单查询list
     *
     * @param asTmrefund
     * @return
     */
    List<AsTmrefund> queryAsTmrefundList(AsTmrefund asTmrefund, int page, int pageSize);

    /**
     * @param request
     * @return
     * @description TM退款单生成退货单的数据筛选
     */
    List<AsTmrefund> selectDataForTmRefundToReturnJob(IRequest request);

    /**
     * @param request
     * @param asTmrefund
     * @description TM退款单生成退货单
     */
    AsTmrefund tmRefundToReturn(IRequest request,AsTmrefund asTmrefund);
}