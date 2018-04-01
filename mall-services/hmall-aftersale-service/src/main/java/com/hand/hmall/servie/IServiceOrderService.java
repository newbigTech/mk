package com.hand.hmall.servie;

import com.hand.hmall.dto.RefundOrder;
import com.hand.hmall.dto.ServiceOrder;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @name IServiceOrderService
 * @Describe 服务单业务逻辑接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface IServiceOrderService {

    /**
     * 创建发货单
     *
     * @param serviceOrder
     * @return
     */
    void createServiceOrder(ServiceOrder serviceOrder);

    /**
     * @param escOrderCode - esc订单编号
     * @param webDisplay   - 网站显示名称
     * @return
     */
    List<ServiceOrder> queryServiceOrders(String escOrderCode, String webDisplay, List<String> conditions);


    /**
     * 订单编号
     * @return
     */
    List<RefundOrder> findByCondition(Long orderId);

}
