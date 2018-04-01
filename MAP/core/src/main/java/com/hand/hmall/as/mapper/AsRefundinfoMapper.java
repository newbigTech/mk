package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsRefundinfo;

import java.util.List;

public interface AsRefundinfoMapper extends Mapper<AsRefundinfo>{

    /**
     * 查询符合推送条件的退款信息明细
     *
     * @param refundinfo
     * @return
     */
    List<AsRefundinfo> getSuitSyncInfo(AsRefundinfo refundinfo);

    /**
     * 手工上载界面查询
     * @param dto
     * @return
     */
    List <AsRefundinfo> getInfoForBalance(AsRefundinfo dto);
}