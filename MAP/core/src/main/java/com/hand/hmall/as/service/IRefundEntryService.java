package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.RefundEntry;

import java.util.HashMap;
import java.util.List;

/**
 * author: zhangzilong
 * name: IRefundEntryService.java
 * discription: 退款单行Service接口
 * date: 2017/8/7
 * version: 0.1
 */
public interface IRefundEntryService extends IBaseService<RefundEntry>,ProxySelf<IRefundEntryService>{

    HashMap sendToHPAY(RefundEntry dto) throws Exception;

    /**
     * 根据退款单头ID查询对应的退款单行
     *
     * @param asRefundId
     * @return
     */
    List<RefundEntry> selectRefundOrderEntry(IRequest iRequest,Long asRefundId, int page, int pageSize);

    /**
     * 根据退款单行ID修改退款金额
     *
     * @param dto  请求参数
     * @return  返回
     */
    int updateRefundEntry(RefundEntry dto);

}
