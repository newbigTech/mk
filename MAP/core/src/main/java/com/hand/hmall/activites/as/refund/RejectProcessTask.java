package com.hand.hmall.activites.as.refund;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.mapper.AsRefundMapper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenzhigang
 * @version 0.1
 * @name RejectProcessTask
 * @description 退款单工作流，审批拒绝节点
 * @date 2017/11/10
 */
@Component
public class RejectProcessTask implements JavaDelegate, IActivitiBean {

    @Autowired
    private AsRefundMapper mapper;

    /**
     * 节点执行逻辑
     * 修改退款单状态
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        Long refundOrderId = execution.getVariable("asRefundOrderId", Long.class);
        AsRefund ar = mapper.selectByPrimaryKey(refundOrderId);
        ar.setStatus("REJECT");
        mapper.updateByPrimaryKey(ar);
    }

    @Override
    public String getBeanName() {
        return "asRefundReject";
    }
}
