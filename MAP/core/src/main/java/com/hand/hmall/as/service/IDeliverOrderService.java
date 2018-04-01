package com.hand.hmall.as.service;

import com.hand.hmall.as.dto.DeliverOrder;
import com.hand.hmall.mst.dto.Product;

import java.util.List;

/**
 * Created by chenzhigang on 2017/7/19.
 */
public interface IDeliverOrderService {

    /**
     * 根据订单ID查询其对应的全部商品
     * @param orderId - 订单ID
     * @return
     */
    List<Product> queryProductsByOrderId(long orderId);

    /**
     * 保存送货单对象
     * @param deliverOrder - 送货单对象
     * @return
     */
    void save(DeliverOrder deliverOrder);
}
