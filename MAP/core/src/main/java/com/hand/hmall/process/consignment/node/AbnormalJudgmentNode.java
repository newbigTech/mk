package com.hand.hmall.process.consignment.node;

import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.process.engine.NodeResponse;

/**
 * @author 马君
 * @version 0.1
 * @name AbnormalJudgmentNode
 * @description 异常判定流程节点
 * @date 2017/7/29 12:35
 */
public class AbnormalJudgmentNode extends AbstractConsignmentNode {

    private static final String NODE_NAME = "异常判定流程节点";
    private static final String SYSTEM_EMPLOYEE_CODE = "system";

    @Override
    public NodeResponse<Consignment> execute(Consignment consignment) {
        iConsignmentProcessService.logTrace(this.getClass(), NODE_NAME, consignment.getConsignmentId(), "发货单[" + consignment.getConsignmentId() + "]进入" + NODE_NAME);
        try {
            MarkorEmployee system = iConsignmentProcessService.selectMarkorEmployeeByCode(SYSTEM_EMPLOYEE_CODE);
            iConsignmentProcessService.abnormalJudgment(consignment, system);
            return new NodeResponse<>(NodeResponse.Status.PASS);
        } catch (Exception e) {
            errorHandle(consignment, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.Status.FAIL);
        }
    }
}
