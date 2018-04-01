package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.om.dto.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AsRefundMapper extends Mapper<AsRefund> {

    /**
     * 根据退款单ID查询对应的信息
     *
     * @param asRefundId
     * @return
     */
    AsRefund getRefundOrderInfo(Long asRefundId);

    /**
     * 订单确认前，新建退款单时的【建议退款金额】计算规则
     *
     * @return
     */
    BigDecimal calculateReferenceSumBeforeConfirm(Order order);

    /**
     * 订单确认后，新建退款单时的【建议退款金额】计算规则
     *
     * @return
     */
    BigDecimal calculateReferenceSumAfterConfirm(Order order);

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
     * 订单确认后reference_fee的计算规则
     *
     * @param order
     * @return
     */
    BigDecimal calculateReferenceFee(Order order);

    /**
     * 根据订单ID获取对应退款单建议退款金额和
     *
     * @param orderId
     * @return
     */
    BigDecimal getReferenceSumByOrderId(Long orderId);

    /**
     * 更新状态位
     *
     * @param asRefund
     */
    void updateStatus(AsRefund asRefund);

    /**
     * 更新状态为完成并记录完成时间
     *
     * @param asRefund
     */
    void finishRefund(AsRefund asRefund);

    /**
     * 查询退款单列表
     *
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
    List<AsRefund> selectRufundList(@Param("code") String code, @Param("serviceCode") String serviceCode, @Param("orderCode") String orderCode,
                                    @Param("customerid") String customerid, @Param("mobile") String mobile, @Param("creationDateStart") String creationDateStart,
                                    @Param("creationDateEnd") String creationDateEnd, @Param("finishTimeStart") String finishTimeStart, @Param("finishTimeEnd") String finishTimeEnd,
                                    @Param("strRefundStatus") String[] strRefundStatus, @Param("strRefundScenario") String[] strRefundScenario, @Param("strRefundReason") String[] strRefundReason);

}