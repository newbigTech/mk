package com.hand.hmall.process.consignment.node;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.process.engine.NodeResponse;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentSplitNode
 * @description 返货单拆单服务
 * @date 2017/7/29 10:35
 */
public class ConsignmentSplitNode extends AbstractConsignmentNode {

    private static final String SPLIT_REASON = "初始拆单";
    private static final String NODE_NAME = "发货单拆分流程节点";

    @Override
    public NodeResponse<Consignment> execute(Consignment consignment) {
        iConsignmentProcessService.logTrace(this.getClass(), NODE_NAME, consignment.getConsignmentId(), "发货单[" + consignment.getConsignmentId() + "]进入" + NODE_NAME);
        try {
            List<Consignment> splitConsignmentList = iConsignmentProcessService.splitConsignment(consignment, SPLIT_REASON);
            if (CollectionUtils.isNotEmpty(splitConsignmentList)) {
                // 如果有拆出新单据，则新单据从承运行选择开始走流程
                // 拆单后跳转到预计发货时间节点 MAG-1149
                return new NodeResponse<>(NodeResponse.GOTO, splitConsignmentList, EstimateConTimeCalculateNode.class);
            }
            return new NodeResponse<>(NodeResponse.Status.PASS, consignment);
        } catch (Exception e) {
            errorHandle(consignment, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.FAIL);
        }
    }
}
