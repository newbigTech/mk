package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name Notification
 * @description 通知实体类
 * @date 2017/10/19
 */

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_TO_DO_NOTIFICATION")
public class Notification {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long notificationId;

    /**
     * 待办通知状态（块码(新通知/已处理)）
     */
    private String notificationStatus;

    /**
     * 通知类型LOV(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    private String notificationType;

    /**
     * 通知时间
     */
    private Date notificationTime;

    /**
     * 通知内容
     */
    private String notificationContent;

    /**
     * 确认人（员工ID）
     */
    private Long confirmPerson;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 关联主键ID
     */
    private Long relatedDataid;

    /*
    * 处理时间从
    * */
    @Transient
    private Date confirmTimeFrom;

    /*
    * 处理时间至
    * */
    @Transient
    private Date confirmTimeTo;

    /*
    * 通知关联用户
    * */
    @Transient
    private Long employeeId;

    /*
    * 确认通知员工编码
    * */
    @Transient
    private String employeeCode;

    /*
    * 确认通知员工名称
    * */
    @Transient
    private String employeeName;

    /*
    * 商品或者订单编码
    * */
    @Transient
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * 主键
     * @return NOTIFICATION_ID 主键
     */
    public Long getNotificationId() {
        return notificationId;
    }

    /**
     * 主键
     * @param notificationId 主键
     */
    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * 待办通知状态（块码(新通知/已处理)）
     * @return NOTIFICATION_STATUS 待办通知状态（块码(新通知/已处理)）
     */
    public String getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * 待办通知状态（块码(新通知/已处理)）
     * @param notificationStatus 待办通知状态（块码(新通知/已处理)）
     */
    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus == null ? null : notificationStatus.trim();
    }

    /**
     * 通知类型LOV(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     * @return NOTIFICATION_TYPE 通知类型LOV(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    public String getNotificationType() {
        return notificationType;
    }

    /**
     * 通知类型LOV(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     * @param notificationType 通知类型LOV(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType == null ? null : notificationType.trim();
    }

    /**
     * 通知时间
     * @return NOTIFICATION_TIME 通知时间
     */
    public Date getNotificationTime() {
        return notificationTime;
    }

    /**
     * 通知时间
     * @param notificationTime 通知时间
     */
    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    /**
     * 通知内容
     * @return NOTIFICATION_CONTENT 通知内容
     */
    public String getNotificationContent() {
        return notificationContent;
    }

    /**
     * 通知内容
     * @param notificationContent 通知内容
     */
    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent == null ? null : notificationContent.trim();
    }

    /**
     * 确认人（员工ID）
     * @return CONFIRM_PERSON 确认人（员工ID）
     */
    public Long getConfirmPerson() {
        return confirmPerson;
    }

    /**
     * 确认人（员工ID）
     * @param confirmPerson 确认人（员工ID）
     */
    public void setConfirmPerson(Long confirmPerson) {
        this.confirmPerson = confirmPerson;
    }

    /**
     * 确认时间
     * @return CONFIRM_TIME 确认时间
     */
    public Date getConfirmTime() {
        return confirmTime;
    }

    /**
     * 确认时间
     * @param confirmTime 确认时间
     */
    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 关联主键ID
     * @return RELATED_DATAID 关联主键ID
     */
    public Long getRelatedDataid() {
        return relatedDataid;
    }

    /**
     * 关联主键ID
     * @param relatedDataid 关联主键ID
     */
    public void setRelatedDataid(Long relatedDataid) {
        this.relatedDataid = relatedDataid;
    }

    public Date getConfirmTimeFrom() {
        return confirmTimeFrom;
    }

    public void setConfirmTimeFrom(Date confirmTimeFrom) {
        this.confirmTimeFrom = confirmTimeFrom;
    }

    public Date getConfirmTimeTo() {
        return confirmTimeTo;
    }

    public void setConfirmTimeTo(Date confirmTimeTo) {
        this.confirmTimeTo = confirmTimeTo;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}