package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Paymentinfo;

import java.util.List;

/**
 * @author peng.chen
 * @version 0.1
 * @name PaymentinfoMapper
 * @description
 * @date 2017年5月25日18:53:32
 */
public interface PaymentinfoMapper extends Mapper<Paymentinfo> {

    /**
     * 手工对账界面弹窗、查询信息
     * @param dto
     * @return
     */
    List <Paymentinfo> getInfoForBalance(Paymentinfo dto);

    /**
     * 根据支付类型查询订单
     *
     * @param dto
     * @return
     */
    List<Paymentinfo> selectPaymentsByModeWithSettleTimeIsNull(Paymentinfo dto);

    /**
     * 根据订单ID查询所有相关的支付信息（售后退款单界面）
     *
     * @param orderId
     * @return
     */
    List<Paymentinfo> getPaymentinfoByOrderId(Long orderId);


    void updateRefundAmount(Paymentinfo dto);

    void insertSvsalesPaymentInfo(Paymentinfo dto);
}