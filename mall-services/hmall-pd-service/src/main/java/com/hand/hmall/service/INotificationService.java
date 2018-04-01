package com.hand.hmall.service;

import com.hand.hmall.model.Notification;
import com.hand.hmall.model.Product;

/**
 * @author 马君
 * @version 0.1
 * @name INotificationService
 * @description 通知Service
 * @date 2017/10/24 16:49
 */
public interface INotificationService extends IBaseService<Notification> {

    /**
     * 添加新增商品通知
     * @param product 商品
     */
    void addProductNewNotice(Product product);

    /**
     * 添加商品名称变更通知
     * @param target 修改后
     * @param source 修改前
     */
    void addProductChangeNotice(Product target, Product source);
}
