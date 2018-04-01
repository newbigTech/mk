package com.hand.hmall.logistics.mapper;

import com.hand.hap.mybatis.common.BaseMapper;
import com.hand.hmall.logistics.pojo.DeliveryOrder;
import org.springframework.data.repository.query.Param;
//import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * author: 张子龙
 * name: DeliveryOrderMapper
 * discription:
 * date: 2017/11/7
 * version: 0.1
 */
public interface LgsDeliveryOrderMapper extends BaseMapper<DeliveryOrder> /*extends Mapper<DeliveryOrder>*/ {

    DeliveryOrder selectDeliveryOrderByCode(@Param("deliveryNote") String deliveryNote);

    /**
     * 根据交货单号和发货单ID查询交货单信息
     * @return
     */
    DeliveryOrder queryByConsignment(Map params);

    /**
     * 根据发货单ID查询其下关联的交货单信息
     * @param consignmentId - 发货单ID
     * @return
     */
    List<Map> queryExistDeliveryOrdersByConsignmentId(@Param("consignmentId") Long consignmentId);
}
