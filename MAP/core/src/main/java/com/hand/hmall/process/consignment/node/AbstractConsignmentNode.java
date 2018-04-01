package com.hand.hmall.process.consignment.node;

import com.google.gson.Gson;
import com.hand.hap.core.exception.UpdateFailedException;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.process.consignment.service.IConsignmentProcessService;
import com.hand.hmall.process.engine.AbstractProcessNode;
import com.hand.hmall.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 马君
 * @version 0.1
 * @name AbstractConsignmentNode
 * @description 抽象的发货单流程节点
 * @date 2017/7/29 10:41
 */
public abstract class AbstractConsignmentNode extends AbstractProcessNode<Consignment> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected IConsignmentProcessService iConsignmentProcessService;

    public void setiConsignmentProcessService(IConsignmentProcessService iConsignmentProcessService) {
        this.iConsignmentProcessService = iConsignmentProcessService;
    }

    @Override
    public void errorHandle(Consignment consignment, Exception e, String message) {
        e.printStackTrace();
        iConsignmentProcessService.logError(this.getClass(), message, consignment.getConsignmentId(), e.getMessage());
        iConsignmentProcessService.setStatus(Constants.CON_STATUS_PROCESS_ERROR, consignment);

        // 检查是否是版本异常，如果是版本异常，则额外记录日志
        if (e.getMessage().contains(UpdateFailedException.MESSAGE_KEY)) {
            iConsignmentProcessService.logError(this.getClass(), message, consignment.getConsignmentId(), "数据版本异常，异常单据[" +
                    new Gson().toJson(consignment) + "]");
        }
    }
}
