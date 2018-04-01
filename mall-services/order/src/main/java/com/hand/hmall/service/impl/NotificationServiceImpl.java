package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.NotificationMapper;
import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.model.Notification;
import com.hand.hmall.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name NotificationServiceImpl
 * @description 通知Service实现类
 * @date 2017/10/24 16:49
 */
@Service
@Transactional
public class NotificationServiceImpl implements INotificationService{

    private static final String NOTICE_TYPE_ORDER_NEW = "ORDER_NEW"; // 新增订单
    private static final String NOTICE_STATUS_PENDDING = "PENDDING"; // 待处理状态

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void addOrderNewNotice(HmallOmOrder order) {
        Notification notification = new Notification();
        notification.setNotificationContent("有新的平台订单" + order.getEscOrderCode() + "下载到中台");
        notification.setNotificationStatus(NOTICE_STATUS_PENDDING);
        notification.setNotificationTime(new Date());
        notification.setNotificationType(NOTICE_TYPE_ORDER_NEW);
        notification.setRelatedDataid(order.getOrderId());
        notificationMapper.insertSelective(notification);
    }
}
