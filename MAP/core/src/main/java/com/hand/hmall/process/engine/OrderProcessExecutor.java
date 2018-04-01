package com.hand.hmall.process.engine;

import com.hand.hmall.om.dto.Order;

/**
 * @author 马君
 * @version 0.1
 * @name OrderProcessExecutor
 * @description 订单流程多线程执行对象
 * @date 2017/9/4 10:45
 */
public class OrderProcessExecutor extends AbstractProcessExecutor<Order> {

    public OrderProcessExecutor(Order order, ProcessManager<Order> processManager) {
        this.proccessData = order;
        this.processManager = processManager;
    }

    @Override
    Order execute(Order proccessData) {
        processManager.start(proccessData);
        return proccessData;
    }
}
