package com.hand.hmall.process.engine;

import com.hand.hmall.om.dto.Consignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentProcessDriver
 * @description 发货单多线程流程管理器
 * @date 2017/9/4 9:23
 */
@Component
public class ConsignmentProcessDriver {

    @Autowired
    @Qualifier("consignmentProcessManager")
    private ProcessManager<Consignment> consignmentProcessManager;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);


    public Consignment startProcess(Consignment consignment) {
        executorService.submit(new ConsignmentProcessExecutor(consignment, consignmentProcessManager));
        return consignment;
    }
}
