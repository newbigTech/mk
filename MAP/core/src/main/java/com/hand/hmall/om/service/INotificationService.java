package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.Notification;
import com.hand.hmall.om.dto.OrderEntry;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name INotificationService
 * @description 通知Service
 * @date 2017/10/19 14:48
 */
public interface INotificationService extends IBaseService<Notification> {

    /**
     * 根据查询条件查询通知
     * @param iRequest iRequest
     * @param notification 查询条件
     * @return List<Notification>
     */
    List<Notification> listNotification(IRequest iRequest, Notification notification,int page,int pageSize);

    /**
     * 确认通知
     * @param iRequest iRequest
     * @param notification 通知
     */
    void confirm(IRequest iRequest, Notification notification);


    /**
     * 添加BOM审核通知
     * @param pin pin码
     * @param pinInfoId pinInfoId
     */
    void addBomApprovedNotice(String pin, Long pinInfoId);
}
