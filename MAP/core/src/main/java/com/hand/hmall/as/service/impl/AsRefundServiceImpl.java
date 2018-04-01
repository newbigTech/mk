package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.activites.mapper.WorkflowMapper;
import com.hand.hmall.activites.service.IWorkflowService;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.RefundEntry;
import com.hand.hmall.as.mapper.AsRefundMapper;
import com.hand.hmall.as.mapper.AsReturnMapper;
import com.hand.hmall.as.mapper.RefundEntryMapper;
import com.hand.hmall.as.service.IAsRefundService;
import com.hand.hmall.as.service.IRefundEntryService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.hr.mapper.MarkorEmployeeMapper;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.om.service.IPaymentinfoService;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class AsRefundServiceImpl extends BaseServiceImpl<AsRefund> implements IAsRefundService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IRefundEntryService asRefundEntryService;

    @Autowired
    private IPaymentinfoService paymentinfoService;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private AsRefundMapper refundMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IOrderService orderService;

    // 工作流服务实例
    @Autowired
    private IActivitiService activitiService;

    @Autowired
    private MarkorEmployeeMapper employeeMapper;

    @Autowired
    private RefundEntryMapper refundEntryMapper;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private WorkflowMapper workflowMapper;

    @Autowired
    private ICodeService iCodeService;

    @Autowired
    private AsReturnMapper returnMapper;

    /**
     * 根据订单ID查询对应的支付信息，退款单新建时表格数据
     *
     * @param requestContext
     * @param orderId
     * @return
     */
    @Override
    public List<Paymentinfo> selectRefundOrderEntry(IRequest requestContext, Long orderId) {
        return paymentinfoService.getPaymentinfoByOrderId(orderId);
    }

    /**
     * 校验退款单如果关联了退货单，退款单行的退款合计是否与退货单金额相等
     *
     * @param refund - 退款单对象
     * @throws Exception - 校验不通过则抛出此异常
     */
    private void checkRefundReturnMatch(AsRefund refund) throws Exception {
        // 查询退款单是否关联退货单信息
        BigDecimal returnId = refund.getReturnId();
        if (returnId != null && returnId.floatValue() != 0.0F) {
            AsReturn asReturn = returnMapper.selectByPrimaryKey(returnId.longValue());
            if (asReturn == null) {
                throw new Exception("当前退款单[ code = '" + refund.getCode() + "', returnId = '" + refund.getReturnId() + "' ]关联不到退货单");
            }
            // 计算退款单行的退款金额合计，并检查是否与对应退货单的退货金额相等
            BigDecimal rfEntriesSum = new BigDecimal(0); // 保存退款单行的退款金额合计
            for (RefundEntry entry : refund.getRefundEntryList()) {
                if (entry.getPayAmount() == null) {
                    RuntimeException re = new RuntimeException("数据错误，退款单行[ code = '" + entry.getCode() + "' ]退款金额为空");
                    throw re;
                }
                rfEntriesSum = rfEntriesSum.add(new BigDecimal(entry.getPayAmount().toString()));
            }

            if (asReturn.getReferenceFee() == null) {
                throw new Exception("数据错误，退货单[ code = '" + asReturn.getCode() + "' ]应返回顾客金额为空");
            }
            // 计算退款单行的退款金额合计完成
            if (rfEntriesSum.doubleValue() != asReturn.getReturnFee()) {
                throw new Exception("退款单行退款金额合计[ " + rfEntriesSum.doubleValue() + " ]与退货单金额[ " + asReturn.getReturnFee() + " ]不匹配");
            }
            // 设置退款单“实际返回客户金额”
            refund.setReturnFee(asReturn.getReturnFee());
        }

    }

    /**
     * 保存退款单信息（对应退款界面保存按钮）
     *
     * @param iRequest
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public List<AsRefund> saveRefundOrderInfo(IRequest iRequest, AsRefund dto) throws Exception {
        List<RefundEntry> refunOrderEntryList = dto.getRefundEntryList();
        BigDecimal referenceSum;

        // 如通过校验，会将“实际返回客户金额”设置到退款单中
        checkRefundReturnMatch(dto);

        //判断新增还是修改
        if (dto.getAsRefundId() != null) { // update
            dto = this.updateByPrimaryKeySelective(iRequest, dto);
            if (refunOrderEntryList != null && refunOrderEntryList.size() > 0) {
                for (RefundEntry entry : refunOrderEntryList) {
                    asRefundEntryService.updateByPrimaryKeySelective(iRequest, entry);
                }
            }
        } else { // create new one

            // 查询退款单关联的订单
            Order order = orderMapper.selectByPrimaryKey(dto.getOrderId());
            //将订单字段【CANCEL_REFUND_UNCREATE】的值更新为“N”---2017-09-21
            order.setCancelRefundUncreate("N");
            orderMapper.updateByPrimaryKeySelective(order);

            if (!dto.getFrom().equals("asReturnOrderDetail")) {
                referenceSum = howMuchCanBeUsedRefund(order.getCode(), order.getOrderId(), iRequest);
            } else {
                referenceSum = dto.getReferenceSum();
            }


//            if (!(referenceSum.compareTo(new BigDecimal(0)) > 0)) {
//                throw new RuntimeException(/*"REFERENCE_SUM_ERROR:" +*/ "错误：退款单建议退款金额必须大于0, [当前建议退款金额为" + referenceSum.doubleValue() + "]");
//            }
            dto.setReferenceSum(referenceSum);
            // 设置当前退款单节点
            dto.setNode(order.getTradeFinishTime() == null ? "BECONFIRM" : "AFCONFIRM");
            dto.setCode(sequenceGenerateService.getNextRefundOrderCode());
            dto = this.insertSelective(iRequest, dto); // 只插入非空属性

            if (refunOrderEntryList != null && refunOrderEntryList.size() > 0) {
                for (RefundEntry entry : refunOrderEntryList) {
                    entry.setAsRefundId(dto.getAsRefundId());
                    entry.setCode(sequenceGenerateService.getNextRefundEntryCode());
                    asRefundEntryService.insertSelective(iRequest, entry);
                }
            }
            order.setRefundAmount(orderService.getTotalRefundAmount(iRequest, order).getRefundAmount());
            orderMapper.updateByPrimaryKeySelective(order);
            logger.info("*************************退款单创建或者更改同步商城*****************************");
            logger.info("" + order.getOrderId());
            //退款单创建或者更改同步商城
            orderService.orderSyncZmall(order.getOrderId());
            logger.info("*************************退款单创建或者更改同步商城同步结束*****************************");
        }


        return Arrays.asList(dto);
    }

    /**
     * 根据退款单ID查询对应的退款单那信息
     *
     * @param asRefundId
     * @return
     */
    @Override
    public AsRefund getRefundOrderInfo(Long asRefundId) {
        return refundMapper.getRefundOrderInfo(asRefundId);
    }

    @Override
    public BigDecimal howMuchCanBeUsedRefund(String code, Long orderId, IRequest iRequest) {
        // 查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new RuntimeException("错误：查不到订单[orderId=" + orderId + "]");
        }
        // 根据订单tradeFinishTime属性是否为空，分别计算新退款单的建议退款金额
        BigDecimal referenceSum;
        Date tradeFinishTime = order.getTradeFinishTime();

        if (order.getTradeFinishTime() == null) {
            referenceSum = refundMapper.calculateReferenceSumBeforeConfirm(order);
        } else {
            Order findorderById = orderService.selectByPrimaryKey(iRequest, order);
            //已支付金额
            BigDecimal payAmount = new BigDecimal(new Double(findorderById.getPaymentAmount().doubleValue()).toString());
            //当前应付金额
            BigDecimal currentAmount = new BigDecimal(new Double(findorderById.getCurrentAmount() != null ? findorderById.getCurrentAmount().doubleValue() : 0).toString());
            BigDecimal referenceMinuend = refundMapper.calculateReferenceSumAfterConfirm(order);
            if (referenceMinuend == null) {
                referenceMinuend = new BigDecimal(0);
            }
            BigDecimal referenceFee = refundMapper.calculateReferenceFee(order);
            if (referenceFee == null) {
                referenceFee = new BigDecimal(0);
            }
            BigDecimal installationFee = new BigDecimal(orderService.getInstallationFee(order).toString());
            if (installationFee == null) {
                installationFee = new BigDecimal(0);
            }
            referenceSum = payAmount.subtract(currentAmount).subtract(referenceMinuend).subtract(referenceFee).subtract(installationFee);
        }

        //order.getPaymentAmount(); // 订单已支付金额
        //order.getOrderAmount(); // 订单金额
        //order.getCurrentAmount(); // 当前应付金额
        if (referenceSum == null) {
            if (order.getTradeFinishTime() == null) {
                if (order.getPaymentAmount() == null || order.getOrderAmount() == null) {
                    throw new RuntimeException("数据错误：付款金额和订单金额有空值");
                }
                referenceSum = new BigDecimal(order.getPaymentAmount().toString()).subtract(order.getOrderAmount());
            } else {
                if (order.getOrderAmount() == null || order.getCurrentAmount() == null) {
                    throw new RuntimeException("数据错误：当前应付金额和订单金额有空值");
                }
                referenceSum = order.getOrderAmount().subtract(new BigDecimal(order.getCurrentAmount().toString()));
            }
            if (referenceSum.compareTo(new BigDecimal("0")) < 0) {
                referenceSum = new BigDecimal("0");
            }
        } else {
            if (referenceSum.compareTo(new BigDecimal("0")) < 0) {
                referenceSum = new BigDecimal("0");
            }
        }
        return referenceSum;
    }

    @Override
    public BigDecimal getReferenceSumByOrderId(Long orderId) {
        return refundMapper.getReferenceSumByOrderId(orderId);
    }

    /**
     * 根据订单ID查询退款单信息
     *
     * @param asRefund
     * @return
     */
    @Override
    public List<AsRefund> queryRefundOrderByOrderId(AsRefund asRefund) {
        return refundMapper.queryRefundOrderByOrderId(asRefund);
    }

    /**
     * 根据退货单ID查询退款单信息
     *
     * @param asRefund
     * @return
     */
    @Override
    public List<AsRefund> queryRefundOrderByReturnId(AsRefund asRefund) {
        return refundMapper.queryRefundOrderByReturnId(asRefund);
    }

    /**
     * 工作流相关
     * 执行退款审批流程
     *
     * @param asRefundOrderId - 退款单ID
     * @return
     */
    @Override
    public void approvalRefund(IRequest iRequest, Long asRefundOrderId) throws Exception {

        AsRefund refund = refundMapper.selectByPrimaryKey(asRefundOrderId);

        // 校验当前退款单金额
        if (refund.getRefoundSum() == null) {
            throw new Exception("数据错误：退款金额为空");
        }

        // 校验当前退款场景
        if (!Arrays.asList(
                new String[]{"LOGISTICS_REASON", "PRODUCTION_REASON", "SYSTEM_REASON", "CUSTOMER_REASON", "CUSTOMIZED"})
                .contains(refund.getRefundScenario())) {
            throw new Exception("退款场景不符合要求");
        }


        // 流程模板Key
        String processKey = "AS_REFUND_201710261026";

        // 创建流程实例请求
        ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();

        // 设置processDefinitionKey (流程唯一标识)
        instanceCreateRequest.setProcessDefinitionKey(processKey);

        // 设置流程初始变量
        ArrayList variables = new ArrayList();

        // 记录流程初始时的退款单状态
        RestVariable variable = new RestVariable();
        variable.setName("_INITIAL_STATUS_");
        // variable.setType("long");
        variable.setValue(refund.getStatus());
        variables.add(variable);

        // 流程发起者的USER_ID
        variable = new RestVariable();
        variable.setName("employeeUserId");
        // variable.setType("long");
        variable.setValue(iRequest.getUserId());
        variables.add(variable);

        // 退款单ID
        variable = new RestVariable();
        variable.setName("asRefundOrderId");
        // variable.setType("long");
        variable.setValue(asRefundOrderId);
        variables.add(variable);

        // 审批步骤(1 or 2 or 3，值为1时表示流程发起者自身)
        variable = new RestVariable();
        variable.setName("_step_");
        // variable.setType("long");
        variable.setValue(1);
        variables.add(variable);

        // 将变量设置到流程请求
        instanceCreateRequest.setVariables(variables);

        // 启动流程
        this.activitiService.startProcess(iRequest, instanceCreateRequest);
    }


    /**
     * 更新状态位为PROC
     *
     * @param asRefundId
     * @return
     */
    @Override
    public Boolean updateStatusToProc(Long asRefundId) {
        try {
            AsRefund asRefund = new AsRefund();
            asRefund.setAsRefundId(asRefundId);
            asRefund.setStatus("PROC");
            refundMapper.updateStatus(asRefund);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新状态位为FINI
     *
     * @param asRefundId
     * @return
     */
    @Override
    public Boolean updateStatusToFini(Long asRefundId) {
        try {
            AsRefund asRefund = new AsRefund();
            asRefund.setAsRefundId(asRefundId);
            refundMapper.finishRefund(asRefund);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 更新状态位为CANC
     *
     * @param asRefundId
     * @return
     */
    @Override
    public ResponseData updateStatusToCanc(IRequest request, Long asRefundId) {
        try {
            List<RefundEntry> list = refundEntryMapper.selectRefundOrderEntry(asRefundId);
            boolean flag = true;
            boolean allCancelFlag = true;
            for (RefundEntry entry : list) {
                if ("Y".equals(entry.getPayStatus())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                AsRefund asRefund = new AsRefund();
                asRefund.setAsRefundId(asRefundId);
                asRefund.setStatus("CANC");
                refundMapper.updateStatus(asRefund);
                AsRefund refund = refundMapper.selectByPrimaryKey(asRefund);
                Order order = new Order();
                order.setOrderId(refund.getOrderId());
                AsRefund asRefundInfo = new AsRefund();
                asRefundInfo.setAsRefundId(asRefundId);
                List<AsRefund> asRefundList = refundMapper.select(asRefundInfo);
                for (AsRefund ar : asRefundList) {
                    if (!ar.getStatus().equals("CANC")) {
                        allCancelFlag = false;
                    }
                }
                if (allCancelFlag) {
                    order.setCancelRefundUncreate("Y");
                }
                orderMapper.updateByPrimaryKeySelective(order);
                order.setRefundAmount(orderService.getTotalRefundAmount(request, order).getRefundAmount());
                orderMapper.updateByPrimaryKeySelective(order);
                logger.info("*************************退款单取消同步商城*****************************");
                logger.info("" + refund.getOrderId());
                //退款单取消同步商城
                orderService.orderSyncZmall(refund.getOrderId());
                logger.info("*************************退款单取消同步结束*****************************");
                return new ResponseData(true);
            } else {
                ResponseData responseData = new ResponseData(false);
                responseData.setMessage("已发生过退款的退款单无法取消。");
                return responseData;
            }
        } catch (Exception e) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage("数据库异常，请稍后重试或联系管理员。");
            return responseData;
        }

    }

    /**
     * 根据user id查询雇员信息
     * 可能具有多个岗位
     *
     * @param userId - 当前登录者ID
     * @return
     */
    @Override
    public List<Map> queryEmployeePositionInfo(Long userId) {
        return employeeMapper.queryEmployeePositionInfo(userId);
    }

    /**
     * 撤回退款流程，将退款单状态恢复
     *
     * @param iRequest
     * @param procInsId - 退款流程ID
     */
    @Override
    public void repealRefundProcIns(IRequest iRequest, Long procInsId) {

        AsRefund asRefund = new AsRefund();
        asRefund.setAsRefundId(Long.parseLong(workflowService.queryAsRefundInfoByProcinstId(procInsId).get("RF_ID").toString()));

        asRefund = selectByPrimaryKey(iRequest, asRefund);
        asRefund.setStatus(workflowMapper.queryProcInsAttribute(procInsId, "_INITIAL_STATUS_"));

        updateByPrimaryKey(iRequest, asRefund);
    }

    @Override
    public List<AsRefund> selectRufundList(IRequest requestCtx, int page, int pagesize, String code, String serviceCode, String orderCode, String customerid, String mobile, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String[] strRefundStatus, String[] strRefundScenario, String[] strRefundReason) {
        PageHelper.startPage(page, pagesize);
        List<AsRefund> asRefunds = refundMapper.selectRufundList(code, serviceCode, orderCode, customerid, mobile, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, strRefundStatus, strRefundScenario, strRefundReason);
        for (AsRefund asRefund : asRefunds) {
            if (!StringUtil.isEmpty(asRefund.getStatus())) {
                String status = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.AS.REFUND.STATUS", asRefund.getStatus());
                asRefund.setStatus(status);
            }
            if (!StringUtil.isEmpty(asRefund.getRefundScenario())) {
                String refundScenario = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.AS.REFUND_SCENARIO", asRefund.getRefundScenario());
                asRefund.setRefundScenario(refundScenario);
            }
            if (!StringUtil.isEmpty(asRefund.getReturnReason())) {
                String returnReason = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL_AS_REFUND_REASON", asRefund.getReturnReason());
                asRefund.setReturnReason(returnReason);
            }
            if (!StringUtil.isEmpty(asRefund.getPayMode())) {
                String payMode = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.PAYMENT_TYPE", asRefund.getPayMode());
                asRefund.setPayMode(payMode);
            }
        }
        return asRefunds;
    }

    @Override
    public List<AsRefund> selectRufundList(IRequest requestCtx, String code, String serviceCode, String orderCode, String customerid, String mobile, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String[] strRefundStatus, String[] strRefundScenar, String[] strRefundReason) {
        List<AsRefund> asRefunds = refundMapper.selectRufundList(code, serviceCode, orderCode, customerid, mobile, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, strRefundStatus, strRefundScenar, strRefundReason);
        return asRefunds;
    }
}
