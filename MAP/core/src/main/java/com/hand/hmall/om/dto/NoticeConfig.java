package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author 马君
 * @version 0.1
 * @name NoticeConfig
 * @description 通知权限配置实体类
 * @date 2017/10/19
 */

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_NOTICE_CONFIG")
public class NoticeConfig extends BaseDTO{
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long configId;

    /**
     * 通知类型块码(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    private String notificationType;

    /**
     * 岗位（id）
     */
    private Long position;

    /**
     * 员工（id）
     */
    private Long employee;

    /**
     * 员工名称
     */
    @Transient
    private String employeeName;

    /**
     * 岗位名称
     */
    @Transient
    private String positionName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    /**
     * 主键
     *
     * @return CONFIG_ID 主键
     */
    public Long getConfigId() {
        return configId;
    }

    /**
     * 主键
     *
     * @param configId 主键
     */
    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    /**
     * 通知类型块码(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     *
     * @return NOTIFICATION_TYPE 通知类型块码(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    public String getNotificationType() {
        return notificationType;
    }

    /**
     * 通知类型块码(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     *
     * @param notificationType 通知类型块码(新商品通知/产品名称变更通知/新订单通知/待工艺审核通知)
     */
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType == null ? null : notificationType.trim();
    }

    /**
     * 岗位（id）
     *
     * @return POSITION 岗位（id）
     */
    public Long getPosition() {
        return position;
    }

    /**
     * 岗位（id）
     *
     * @param position 岗位（id）
     */
    public void setPosition(Long position) {
        this.position = position;
    }

    /**
     * 员工（id）
     *
     * @return EMPLOYEE 员工（id）
     */
    public Long getEmployee() {
        return employee;
    }

    /**
     * 员工（id）
     *
     * @param employee 员工（id）
     */
    public void setEmployee(Long employee) {
        this.employee = employee;
    }
}