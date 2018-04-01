package com.hand.hmall.logistics.pojo;

import java.io.Serializable;

/**
 * @author liuhongxi
 * @version 0.1
 * @name ConsignmentInfo
 * @description 日日顺发货信息接口DTO
 * @date 2017/6/3 14:12
 */
public class ConsignmentInfo implements Serializable {
    // 合作伙伴
    private String partner;
    // 消息通知ID
    private String notifyId;
    // 消息通知类型，固定值update_tms_orders_nodes
    private String notifyType;
    // 消息通知时间 YYYY-MM-DD hh:mm:ss
    private String notifyTime;
    // 内容
    private String content;

    // 服务单号
    private String serviceCode;
    // 日日顺服务单号
    private String thirdServiceCode;
    // 服务状态
    private String serviceStatus;
    // 图片地址
    private String pictureUrl;
    // 操作人
    private String operator;
    // 操作人电话
    private String operatorPhone;
    // 操作时间 YYYY-MM-DD hh:mm:ss
    private String operatorDate;
    // 操作描述
    private String operatorDesc;
    // 收货人（接单时必填，其他节点无需填写）
    private String receivePeople;
    // 收货电话（接单时必填，其他节点无需填写）
    private String receiveTel;
    // 收货人地址（接单时必填，其他节点无需填写）
    private String receiveAddress;
    // 预约上门时间（预约\改约时必填，其他节点无需填写）
    // YYYY-MM-DD hh:mm:ss
    private String appointDate;
    // 提货时间（提货时必填，其他节点无需填写）
    // YYYY-MM-DD hh:mm:ss
    private String deliveryDate;
    // 签收时间（签收时必填，其他节点无需填写）
    // YYYY-MM-DD hh:mm:ss
    private String signDate;
    // 改约时间（改约时必填，其他节点无需填写）
    // YYYY-MM-DD hh:mm:ss
    private String changeAppointDate;

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorDate() {
        return operatorDate;
    }

    public void setOperatorDate(String operatorDate) {
        this.operatorDate = operatorDate;
    }

    public String getOperatorDesc() {
        return operatorDesc;
    }

    public void setOperatorDesc(String operatorDesc) {
        this.operatorDesc = operatorDesc;
    }

    public String getReceivePeople() {
        return receivePeople;
    }

    public void setReceivePeople(String receivePeople) {
        this.receivePeople = receivePeople;
    }

    public String getReceiveTel() {
        return receiveTel;
    }

    public void setReceiveTel(String receiveTel) {
        this.receiveTel = receiveTel;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getChangeAppointDate() {
        return changeAppointDate;
    }

    public void setChangeAppointDate(String changeAppointDate) {
        this.changeAppointDate = changeAppointDate;
    }

    public String getThirdServiceCode() {
        return thirdServiceCode;
    }

    public void setThirdServiceCode(String thirdServiceCode) {
        this.thirdServiceCode = thirdServiceCode;
    }
}
