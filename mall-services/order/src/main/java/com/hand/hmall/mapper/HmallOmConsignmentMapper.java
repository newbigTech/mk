/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;


import com.hand.hmall.model.HmallOmConsignment;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

public interface HmallOmConsignmentMapper extends Mapper<HmallOmConsignment> {

    /**
     * 根据orderId获取发货单
     * @param orderId
     * @return
     */
   List<HmallOmConsignment> getConsignmentByOrderId(Long orderId);


    /**
     * 根据orderId 获取订单关联的所有“确认收货时间”【consignment.trade_finished_time】值为空且【consignment.status】=WAIT_BUYER_CONFIRM的发货单
     * @param orderId
     * @return
     */
   List<HmallOmConsignment> queryConsignmentByOrderId(Long orderId);
}