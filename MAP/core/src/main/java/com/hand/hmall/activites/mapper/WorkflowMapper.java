package com.hand.hmall.activites.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WorkflowMapper
 * @description
 * @date 2017/11/10
 */
public interface WorkflowMapper {

    /**
     * 查询退款单
     * @param asRefundOrderId - 退款单ID
     * @return
     */
    Map queryAsRefundInfo(@Param("asRefundOrderId") Long asRefundOrderId);

    /**
     * 根据流程实例ID查询退款单信息
     * @param procinstId - 流程实例ID
     * @return
     */
    Map queryAsRefundInfoByProcinstId(@Param("procinstId") Long procinstId);

    /**
     * 根据流程实例ID在历史表中查询退款单信息
     * @param procinstId - 流程实例ID
     * @return
     */
    Map queryAsRefundInfoByProcinstId_hi(@Param("procinstId") Long procinstId);

    /**
     * 查询流程实例的属性值
     * @param procInsId - 流程实例ID
     * @param attrName - 属性名称
     * @return
     */
    String queryProcInsAttribute(@Param("procInsId") Long procInsId, @Param("attrName") String attrName);
}
