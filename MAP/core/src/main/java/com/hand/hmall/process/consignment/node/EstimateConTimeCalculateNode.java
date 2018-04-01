package com.hand.hmall.process.consignment.node;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.process.engine.NodeResponse;

import static com.hand.hmall.process.engine.NodeResponse.Status.PASS;

/**
 * @author 马君
 * @version 0.1
 * @name EstimateConTimeCalculateNode
 * @description 预计发货时间计算节点
 * @date 2017/8/26 16:04
 */
public class EstimateConTimeCalculateNode extends AbstractConsignmentNode {

    private static final String NODE_NAME = "预计发货时间计算节点";

    @Override
    public NodeResponse<Consignment> execute(Consignment consignment) {
        iConsignmentProcessService.logTrace(this.getClass(), NODE_NAME, consignment.getConsignmentId(), "发货单[" + consignment.getConsignmentId() + "]进入" + NODE_NAME);
        try {
            iConsignmentProcessService.calculateEstimateConTime(consignment);
            return new NodeResponse<>(NodeResponse.Status.PASS, consignment);
        } catch (Exception e) {
            errorHandle(consignment, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.Status.FAIL, consignment);
        }
    }
}
