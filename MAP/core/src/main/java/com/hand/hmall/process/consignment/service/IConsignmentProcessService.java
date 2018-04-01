package com.hand.hmall.process.consignment.service;

import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.process.consignment.pojo.SplitHeader;
import com.hand.hmall.process.engine.IProcessService;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IConsignmentProcessService
 * @description 服务点的流程服务
 * @date 2017/6/26 11:30
 */
public interface IConsignmentProcessService extends IProcessService<Consignment> {

    /**
     * 发货单异常判定
     * @param consignment
     */
    boolean abnormalJudgment(Consignment consignment, MarkorEmployee employee);

    /**
     * 承运商选择
     * @param consignment
     */
    void selectCarrier(Consignment consignment);

    /**
     * 发货单拆单，流程拆单
     * @param sourceConsignment 原发货单
     * @param splitReason 拆单原因
     * @return
     */
    List<Consignment> splitConsignment(Consignment sourceConsignment, String splitReason);

    /**
     * 手工拆单
     * @param splitHeader
     * @return
     */
    List<Consignment> splitConsignment(SplitHeader splitHeader);

    /**
     * 根据用户编号查询
     * @param code 编号
     * @return MarkorEmployee
     */
    MarkorEmployee selectMarkorEmployeeByCode(String code);


    /**
     * 预计发货时间计算
     * @param consignment 发货单
     */
    void calculateEstimateConTime(Consignment consignment);
}
