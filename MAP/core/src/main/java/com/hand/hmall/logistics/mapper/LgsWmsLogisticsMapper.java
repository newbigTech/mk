package com.hand.hmall.logistics.mapper;

import com.hand.hmall.logistics.pojo.WmsConsignment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WmsLogisticsMapper
 * @description WMS推送物流信息持久层接口
 * @date 2017/8/10
 */
public interface LgsWmsLogisticsMapper {

    /**
     * 根据SAP_CODE查询送货单
     *
     * @param sapCode
     * @return
     */
    List<WmsConsignment> queryConsignmentsBySAPCode(@Param("sapCode") String sapCode);

    /**
     * 更新送货单信息
     *
     * @param consignment
     */
    void updateConsignment(WmsConsignment consignment);

    /**
     * 根据发货单ID和发货单行序号查询发货单行
     *
     * @param consignmentId - 发货单ID
     * @param lineNumber    - 发货单行（订单行）序号
     * @return
     */
    List<Map> queryLogisticsOrderEntries(@Param("consignmentId") Long consignmentId, @Param("lineNumber") String lineNumber);

    /**
     * 更新发货单行（订单行）信息
     *
     * @param orderEntry
     */
    void updateConsignmentEntry(Map orderEntry);

    /**
     * 根据物流公司编码查询其对应的ID
     *
     * @param logisticsCode - 物流公司编码
     * @return
     */
    Long selectLogisticsIdByCode(@Param("logisticsCode") String logisticsCode);

    /**
     * 根据订单id查询订单
     * @param orderId 订单id
     * @return Map
     */
    Map selectOrderByOrderId(Long orderId);

    /**
     * 更新订单状态
     * @param order 订单
     */
    void updateOrderStatus(Map order);

    /**
     * 根据物流公司代码查询相关信息
     * @param transCode
     * @return
     */
    Map queryMstLogisticsoByCode(@Param("transCode") String transCode);
}
