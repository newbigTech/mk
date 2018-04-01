package com.hand.hmall.activites.as.refund;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.mapper.AsRefundMapper;
import com.hand.hmall.hr.mapper.MarkorEmployeeMapper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author chenzhigang
 * @version 0.1
 * @name NextApproverPositionService
 * @description 获得下一个审批人逻辑
 * @date 2017/11/10
 */
@Component
public class NextApproverPositionService implements JavaDelegate, IActivitiBean {

    // 工作流配置文件
    private static final String REFUND_WORKFLOW_CFG_FILE = "refund-workflow-cfg.properties";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AsRefundMapper refundMapper;

    //MD_CS_SERVICEPRO	售后专员
    //MD_CS_SUPERVISOR	售后主管
    //MD_CS_MANAGER	    客服经理
    //MD_CS_DIRECTOR	客服总监
    //MD_CEO
    private List<String> positionList = Arrays.asList(
            new String[]{"MD_CS_SERVICEPRO", "MD_CS_SUPERVISOR", "MD_CS_MANAGER", "MD_CS_DIRECTOR", "MD_CEO"});


    @Autowired
    private MarkorEmployeeMapper employeeMapper;


    /**
     * 流程节点逻辑
     *
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {

        // 获得退款单信息
        Long asRefundOrderId = execution.getVariable("asRefundOrderId", Long.class);
        AsRefund refund = refundMapper.selectByPrimaryKey(asRefundOrderId);

        // 当前用户的岗位信息
        String positionCode = null;

        if (execution.hasVariable("_positionCode_")) {
            positionCode = execution.getVariable("_positionCode_", String.class);
        } else {
            // 获得当前用户的岗位信息
            List<Map> positions = employeeMapper.queryEmployeePositionInfo(execution.getVariable("employeeUserId", Long.class));
            breakFlag:
            for (String mdPositionCode : new String[]{"MD_CEO", "MD_CS_DIRECTOR", "MD_CS_MANAGER", "MD_CS_SUPERVISOR", "MD_CS_SERVICEPRO"}) {
                for (Map p : positions) {
                    if (mdPositionCode.equalsIgnoreCase(p.get("POSITION_CODE").toString())) {
                        positionCode = mdPositionCode;
                        break breakFlag;
                    }
                }
            }
        }

        if (positionCode == null) {
            throw new RuntimeException("当前操作员没有设置具有退款单审批权限的岗位");
        }

        // 获得当前步骤
        Integer step = execution.getVariable("_step_", Integer.class);

        // 将退款单状态更改为“处理中”
        AsRefund ar = refundMapper.selectByPrimaryKey(asRefundOrderId);
        ar.setStatus("PROC");
        refundMapper.updateByPrimaryKey(ar);

        if (step.intValue() == 1) {
            // _step_ == 1
            if (hasPermission(positionCode, refund)) {
                // 如退款单金额在当前申请者权限内，下一个审批人信息返回NONE(流程直接通过)
                execution.setVariable("_next_", "NONE");
            } else {
                // 如退款单金额超出当前申请者权限，寻找具有此权限的最低职位P
                String suitPosition = findSuitedPosition(refund);
                if (suitPosition.equals(findPositionCode(positionCode, 1))) { // 如P是当前申请者直接上级，返回P
                    execution.setVariable("_next_", suitPosition);
                    execution.setVariable("_positionCode_", suitPosition);
                } else { // 否则，返回P的直接下级职位
                    execution.setVariable("_next_", findPositionCode(suitPosition, -1));
                    execution.setVariable("_positionCode_", findPositionCode(suitPosition, -1));
                }
            }
        } else {
            // _step_ != 1
            if (hasPermission(positionCode, refund)) {
                // 如退款单金额在当前申请者权限内，下一个审批人信息返回NONE(流程直接通过)
                execution.setVariable("_next_", "NONE");
            } else {
                // 如退款单金额超出当前申请者权限，返回当前申请者的上级职位
                execution.setVariable("_next_", findPositionCode(positionCode, 1));
                execution.setVariable("_positionCode_", findPositionCode(positionCode, 1));
            }
        }
        // 返回前，_step_ = _step_ + 1
        execution.setVariable("_step_", step + 1);
    }

    /**
     * 查询比当前岗位高出指定级别的岗位代码
     *
     * @param positionCode - 岗位代码
     * @param increase     - 比当前岗位高出的级别
     * @return
     */
    private Object findPositionCode(String positionCode, int increase) {
        int i = this.positionList.indexOf(positionCode) + increase;
        if (i < this.positionList.size() && i > -1) {
            return this.positionList.get(i);
        }
        return this.positionList.get(this.positionList.size() - 1);
    }

    /**
     * 查询并返回具有此退款单金额审批权限的最低职位代码
     *
     * @param refund - 退货单
     * @return
     */
    private String findSuitedPosition(AsRefund refund) {
        for (int i = 0; i < positionList.size(); i++) {
            String p = positionList.get(i);
            if (hasPermission(p, refund)) {
                return p;
            }
        }
        return "MD_CEO";
    }

    /**
     * 根据岗位代码和退款单，判断当前用户是否具有提交审批权限
     *
     * @param positionCode - 岗位代码
     * @param refund       - 退款单
     * @return
     */
    private boolean hasPermission(String positionCode, AsRefund refund) {

        boolean permission = false;

        BigDecimal rs = refund.getRefoundSum();

        BigDecimal maxAmount;
        try {
            maxAmount = getMaxApprovalAmount(positionCode, refund.getRefundScenario());
            permission = rs.compareTo(maxAmount) <= 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }

        return permission;

    }

    @Override
    public String getBeanName() {
        return "nextApproverPositionService";
    }

    /**
     * 查询此岗位在该退款场景下的最大退款审批金额
     *
     * @param positionCode 岗位编码
     * @param refundReason 退款场景
     * @return
     */
    private BigDecimal getMaxApprovalAmount(String positionCode, String refundReason) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(REFUND_WORKFLOW_CFG_FILE));
        } catch (IOException e) {
            logger.error("读取配置文件config.properties失败", e);
            throw e;
        }
        String reasonInfo = properties.getProperty("refund.wf." + refundReason.toUpperCase());
        if (reasonInfo == null) {
            throw new Exception("配置文件中找不到退款场景[" + refundReason + "]");
        }
        for (String positionAmount : reasonInfo.split(";")) {
            String position = positionAmount.split(":")[0];
            String amount = positionAmount.split(":")[1];
            if (positionCode.equalsIgnoreCase(position)) {
                return new BigDecimal(amount);
            }
        }
        throw new Exception("配置文件中找不到[" + refundReason + "]场景下[" + positionCode + "]岗位的审批金额权限");
    }
}
