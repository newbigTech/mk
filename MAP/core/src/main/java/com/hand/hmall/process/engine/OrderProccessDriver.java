package com.hand.hmall.process.engine;

import com.hand.hmall.om.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 马君
 * @version 0.1
 * @name OrderProccessDriver
 * @description 订单多线程流程管理器
 * @date 2017/9/4 10:50
 */
@Component
public class OrderProccessDriver {

    @Autowired
    @Qualifier("orderProcessManager")
    private ProcessManager<Order> orderProcessManager;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    public Order startProcess(Order order) {
        executorService.submit(new OrderProcessExecutor(order, orderProcessManager));
        return order;
    }

}
