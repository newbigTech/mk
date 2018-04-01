package com.hand.hmall.process.order.node;

import com.hand.hmall.om.dto.Order;
import com.hand.hmall.process.engine.NodeResponse;

/**
 * @author 马君
 * @version 1.0
 * @name:SizeCalculateNode
 * @Description: 主推款包装尺寸计算
 * @date 2017/10/11 18:34
 */
public class SizeCalculateNode extends AbstractOrderNode {

    private static final String NODE_NAME = "尺寸计算流程节点";

    @Override
    public NodeResponse<Order> execute(Order order) {
        iOrderProcessService.logTrace(this.getClass(), NODE_NAME, order.getOrderId(), "订单[" + order.getOrderId() + "]进入" + NODE_NAME);
        try {
            iOrderProcessService.calSizeForRegular(order);
            return new NodeResponse<>(NodeResponse.PASS);
        } catch (Exception e) {
            errorHandle(order, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.FAIL);
        }
    }
}
