package com.hand.hmall.process.consignment.node;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.process.engine.NodeResponse;

/**
 * @author 马君
 * @version 0.1
 * @name CarrierChooseNode
 * @description 承运商选择流程节点
 * @date 2017/7/29 12:30
 */
public class CarrierChooseNode extends AbstractConsignmentNode {

    private static final String NODE_NAME = "承运商选择流程节点";

    @Override
    public NodeResponse<Consignment> execute(Consignment consignment) {
        iConsignmentProcessService.logTrace(this.getClass(), NODE_NAME, consignment.getConsignmentId(), "发货单[" + consignment.getConsignmentId() + "]进入" + NODE_NAME);
        try {
            iConsignmentProcessService.selectCarrier(consignment);
            return new NodeResponse<>(NodeResponse.PASS);
        } catch (Exception e) {
            errorHandle(consignment, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.FAIL);
        }
    }
}
