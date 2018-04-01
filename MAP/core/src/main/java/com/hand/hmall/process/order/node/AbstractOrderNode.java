package com.hand.hmall.process.order.node;

import com.google.gson.Gson;
import com.hand.hap.core.exception.UpdateFailedException;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.process.engine.AbstractProcessNode;
import com.hand.hmall.process.order.service.IOrderProcessService;
import com.hand.hmall.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 马君
 * @version 0.1
 * @name AbstractOrderNode
 * @description AbstractOrderNode
 * @date 2017/7/29 13:59
 */
public abstract class AbstractOrderNode extends AbstractProcessNode<Order> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected IOrderProcessService iOrderProcessService;

    public void setiOrderProcessService(IOrderProcessService iOrderProcessService) {
        this.iOrderProcessService = iOrderProcessService;
    }

    @Override
    public void errorHandle(Order order, Exception e, String message) {
        logger.error("AbstractOrderNode.errorHandle", e);
        iOrderProcessService.logError(this.getClass(), message, order.getOrderId(), e.getMessage());
        iOrderProcessService.setStatus(Constants.ORDER_STATUS_PROCESS_ERROR, order);

        // 检查是否是版本异常，如果是版本异常，则额外记录日志
        if (e.getMessage() != null && e.getMessage().contains(UpdateFailedException.MESSAGE_KEY)) {
            iOrderProcessService.logError(this.getClass(), message, order.getOrderId(), "数据版本异常，异常单据[" +
                    new Gson().toJson(order) + "]");
        }
    }
}
