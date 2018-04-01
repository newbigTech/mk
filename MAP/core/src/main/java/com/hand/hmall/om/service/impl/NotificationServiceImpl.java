package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.hr.service.IMarkorEmployeeService;
import com.hand.hmall.om.dto.Notification;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.mapper.NotificationMapper;
import com.hand.hmall.om.service.INoticeConfigService;
import com.hand.hmall.om.service.INotificationService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name NotificationServiceImpl
 * @description 通知Service实现类
 * @date 2017/10/19 14:48
 */
@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification> implements INotificationService {

    @Autowired
    private IMarkorEmployeeService iMarkorEmployeeService;

    @Autowired
    private INoticeConfigService iNoticeConfigService;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 根据查询条件查询通知
     * @param iRequest iRequest
     * @param notification 查询条件
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Notification> listNotification(IRequest iRequest, Notification notification,int page,int pageSize) {

        List<String> noticeTypes = new ArrayList<>();
        // 获取当前系统用户
        String employeeCode = iRequest.getEmployeeCode();
        // 查询当前用户可查看的通知类型
        noticeTypes.addAll(iNoticeConfigService.selectNoticeTypesOfEmployee(employeeCode));
        if (CollectionUtils.isEmpty(noticeTypes)) {
            // 当前登录用户没有配置权限返回空集合
            return Collections.emptyList();
        }
        if (notification != null && notification.getNotificationType() != null) {
            if(noticeTypes.contains(notification.getNotificationType())){
                noticeTypes = Collections.singletonList(notification.getNotificationType());
            }else{
                return Collections.emptyList();
            }
        }
        PageHelper.startPage(page,pageSize);
        return notificationMapper.selectByNotification(notification, noticeTypes);
    }

    /**
     * 确认通知
     * @param iRequest iRequest
     * @param notification 通知
     */
    @Override
    public void confirm(IRequest iRequest, Notification notification) {
        Assert.notNull(iRequest.getEmployeeCode(),"本登录用户未关联员工编号!");
        MarkorEmployee employee = iMarkorEmployeeService.selectByEmployeeCode(iRequest.getEmployeeCode());
        notification.setNotificationStatus(Constants.NOTICE_STATUS_PROCCESSED);
        Assert.notNull(employee.getEmployeeId(),"没有找到本用户关联的员工编号!");
        notification.setConfirmPerson(employee.getEmployeeId());
        notification.setConfirmTime(new Date());
        this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), notification);
    }

    /**
     * 添加BOM审核通知
     * @param pin pin码
     * @param pinInfoId pinInfoId
     */
    @Override
    public void addBomApprovedNotice(String pin, Long pinInfoId) {
        Notification notification = new Notification();
        notification.setNotificationContent("有新的PIN码行" + pin + "需要人工审核");
        notification.setNotificationStatus(Constants.NOTICE_STATUS_PENDDING);
        notification.setNotificationTime(new Date());
        notification.setNotificationType(Constants.NOTICE_TYPE_CRAFT_CHECKING);
        notification.setRelatedDataid(pinInfoId);
        this.insertSelective(RequestHelper.newEmptyRequest(), notification);
    }
}
