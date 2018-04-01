package com.hand.hmall.services.order.service;

import com.hand.hmall.services.order.dto.OrderEntry;

import java.util.List;

/**
 * HMall订单服务
 *
 * @author alaowan
 * Created at 2017/12/26 14:18
 * @description
 */
public interface IHMallOrderService {

    /**
     * 根据pin码查询关联的套件子件订单行信息（子行的v码和pin码）
     *
     * @param pin
     * @return
     */
    List<OrderEntry> getSubOrderEntriesByPinCode(String pin);


    /**
     * 根据pin码和V码查询套件头V码（如果传入的V码不是套件，则返回的是本身）
     *
     * @param pin
     * @param vode
     * @return
     */
    String getParentVCode(String pin, String vode);
}
