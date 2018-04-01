package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.TradeTrace;

import java.util.List;
import java.util.Map;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ITradeTraceService
 * @description 物流跟踪表 Service类
 * @date 2017/8/11
 */
public interface ITradeTraceService extends IBaseService<TradeTrace>, ProxySelf<ITradeTraceService> {
    /**
     * 推送物流信息到zmall商城
     *
     * @return Map<String,String>
     * @throws Exception
     */
    Map<String, String> sendToZmall() throws Exception;

    /**
     * 发货单对应物流信息查询
     *
     * @param request   请求对象
     * @param dto       发货单，请求参数
     * @param page      页数
     * @param pagesize      页面显示数
     * @return
     */
    List<TradeTrace> getTradeTraceInfo(IRequest request, TradeTrace dto, int page, int pagesize);

    /**
     * 交货单对应物流信息查询
     *
     * @param request   请求对象
     * @param dto       发货单，请求参数
     * @param page      页数
     * @param pagesize      页面显示数
     * @return
     */
    List<TradeTrace> getTradeTraceInfoByDelivery(IRequest request, TradeTrace dto, int page, int pagesize);
}