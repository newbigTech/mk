package com.hand.hmall.process.order.node;

import com.hand.hmall.om.dto.Order;
import com.hand.hmall.process.engine.NodeResponse;

/**
 * @author 马君
 * @version 0.1
 * @name SuiteComponentsGenerateNode
 * @description 套件组件生成流程节点
 * @date 2017/7/29 13:59
 */
public class SuiteComponentsGenerateNode extends AbstractOrderNode {

    private static final String NODE_NAME = "套件组件生成流程节点";

    @Override
    public NodeResponse<Order> execute(Order order) {
        iOrderProcessService.logTrace(this.getClass(), NODE_NAME, order.getOrderId(), "订单[" + order.getOrderId() + "]进入" + NODE_NAME);
        try {
            iOrderProcessService.generateSuiteComponents(order);
            return new NodeResponse<>(NodeResponse.PASS);
        } catch (Exception e) {
            errorHandle(order, e, NODE_NAME);
            return new NodeResponse<>(NodeResponse.FAIL);
        }
    }
}
