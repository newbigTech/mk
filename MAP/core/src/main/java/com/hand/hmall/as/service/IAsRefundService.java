package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.om.dto.Paymentinfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * author: weishoupeng
 * name: IAsRefundService.java
 * discription: 退款单Service
 * date: 2017/7/21
 * version: 0.1
 */
public interface IAsRefundService extends IBaseService<AsRefund>, ProxySelf<IAsRefundService> {

    /**
     * 根据退款单ID获取对应的退款单行信息
     *
     * @param requestContext
     * @param orderId
     * @return
     */
    List<Paymentinfo> selectRefundOrderEntry(IRequest requestContext, Long orderId);

    /**
     * 退款单界面保存按钮，点击保存，存储退款单信息
     *
     * @param iRequest
     * @param dto
     * @return
     */
    List<AsRefund> saveRefundOrderInfo(IRequest iRequest, AsRefund dto) throws Exception;

    /**
     * 根据退款单ID查询对应的信息
     *
     * @param asRefundId
     * @return
     */
    AsRefund getRefundOrderInfo(Long asRefundId);

    /**
     * 根据订单ID查询退款单信息
     *
     * @param asRefund
     * @return
     */
    List<AsRefund> queryRefundOrderByOrderId(AsRefund asRefund);

    /**
     * 根据退货单ID查询退款单信息
     *
     * @param asRefund
     * @return
     */
    List<AsRefund> queryRefundOrderByReturnId(AsRefund asRefund);

    /**
     * 计算该订单可用于退款的金额
     *
     * @param orderId - 订单ID
     * @return
     */
    BigDecimal howMuchCanBeUsedRefund(String code, Long orderId, IRequest iRequest);

    /**
     * 根据订单ID获取对应退款单建议退款金额和
     *
     * @param orderId
     * @return
     */
    BigDecimal getReferenceSumByOrderId(Long orderId);

    /**
     * 工作流相关
     * 执行退款审批流程
     *
     * @param asRefundOrderId - 退款单ID
     * @return
     */
    void approvalRefund(IRequest iRequest, Long asRefundOrderId) throws Exception;


    /**
     * 更新状态位为PROC
     *
     * @param asRefundId
     * @return
     */
    Boolean updateStatusToProc(Long asRefundId);

    /**
     * 更新状态位为FINI
     *
     * @param asRefundId
     * @return
     */
    Boolean updateStatusToFini(Long asRefundId);

    /**
     * 更新状态位为CANC
     *
     * @param asRefundId
     * @return
     */
    ResponseData updateStatusToCanc(IRequest request, Long asRefundId);

    /**
     * 根据当前登录着ID查询其岗位信息
     * 可能具有多个岗位
     *
     * @param userId - 当前登录者ID
     * @return
     */
    List<Map> queryEmployeePositionInfo(Long userId);

    /**
     * 将流程实例ID关联的退款单撤回
     *
     * @param iRequest
     * @param procInsId
     */
    void repealRefundProcIns(IRequest iRequest, Long procInsId);

    /**
     * 查询退款单列表
     *
     * @param page
     * @param pagesize
     * @param code
     * @param serviceCode
     * @param orderCode
     * @param customerid
     * @param mobile
     * @param creationDateStart
     * @param creationDateEnd
     * @param finishTimeStart
     * @param finishTimeEnd
     * @return
     */
    List<AsRefund> selectRufundList(IRequest requestCtx, int page, int pagesize, String code, String serviceCode, String orderCode, String customerid, String mobile, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String[] strRefundStatus, String[] strRefundScenario, String[] strRefundReason);

    /**
     * 查询退款单列表(不带分页)
     *
     * @param requestCtx
     * @param code
     * @param serviceCode
     * @param orderCode
     * @param customerid
     * @param mobile
     * @param creationDateStart
     * @param creationDateEnd
     * @param finishTimeStart
     * @param finishTimeEnd
     * @param strRefundStatus
     * @param strRefundScenar
     * @param strRefundReason
     * @return
     */
    List<AsRefund> selectRufundList(IRequest requestCtx, String code, String serviceCode, String orderCode, String customerid, String mobile, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String[] strRefundStatus, String[] strRefundScenar, String[] strRefundReason);
}