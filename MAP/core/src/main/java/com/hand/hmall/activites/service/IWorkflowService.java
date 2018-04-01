package com.hand.hmall.activites.service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WorkflowMapper
 * @description
 * @date 2017/11/10
 */
public interface IWorkflowService {

    /**
     *
     * @param asRefundOrderId - 退款单ID
     * @return
     */
    Map queryAsRefundInfo(Long asRefundOrderId);

    /**
     *
     * @param procinstId - 流程实例ID
     * @return
     */
    Map queryAsRefundInfoByProcinstId(Long procinstId);

    /**
     * 查询匹配岗位代码的雇员代码列表
     * 非主岗位信息也进行匹配
     * @param positionCode - 岗位代码
     * @return
     */
    String getPositionEmp(String positionCode);
}
