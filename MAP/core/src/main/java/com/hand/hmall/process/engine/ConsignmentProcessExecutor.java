package com.hand.hmall.process.engine;

import com.hand.hmall.om.dto.Consignment;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentProcessExecutor
 * @description 发货单流程多线程执行对象
 * @date 2017/9/4 10:41
 */
public class ConsignmentProcessExecutor extends AbstractProcessExecutor<Consignment> {


    public ConsignmentProcessExecutor(Consignment consignment, ProcessManager<Consignment> processManager) {
        this.proccessData = consignment;
        this.processManager = processManager;
    }

    @Override
    Consignment execute(Consignment proccessData) {
        processManager.start(proccessData);
        return proccessData;
    }
}
