package com.hand.hmall.service;

import com.hand.hmall.model.HmallOmOrder;

/**
 * @author 马君
 * @version 0.1
 * @name INotificationService
 * @description 通知Service
 * @date 2017/10/24 16:49
 */
public interface INotificationService {

    /**
     * 添加新增订单通知
     * @param order 订单
     */
    void addOrderNewNotice(HmallOmOrder order);
}
