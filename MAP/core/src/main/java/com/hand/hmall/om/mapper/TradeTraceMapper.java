package com.hand.hmall.om.mapper;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.TradeTrace;
import com.hand.hmall.om.dto.TradeTraceToZmall;

import java.util.List;
/**
 * @author xuxiaoxue
 * @version 0.1
 * @name TradeTraceMapper
 * @description 物流跟踪表 Mapper接口
 * @date 2017/8/11
 */
public interface TradeTraceMapper extends Mapper<TradeTrace>{

    List<TradeTraceToZmall> queryForZmall();

    void updateSyncFlag(List<TradeTraceToZmall> result);

    List<TradeTrace> getTradeTraceInfo(TradeTrace dto);

    List<TradeTrace> getTradeTraceInfoByDelivery(TradeTrace dto);
}