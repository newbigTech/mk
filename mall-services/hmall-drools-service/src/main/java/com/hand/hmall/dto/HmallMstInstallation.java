package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "HMALL_MST_INSTALLATION")
public class HmallMstInstallation {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_INSTALLATION_S.nextval from dual")
    private Long installationId;

    /**
     * 类别
     */
    private Long categoryId;

    /**
     * 安装费
     */
    private Double installationFee;

    /**
     * 状态
     */
    private String status;

    /**
     * 版本号
     */
    private Long objectVersionNumber;

    public Long getInstallationId() {
        return installationId;
    }

    public void setInstallationId(Long installationId) {
        this.installationId = installationId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @Override
    public String toString() {
        return "{\"HmallMstInstallation\":{"
                + "                        \"installationId\":\"" + installationId + "\""
                + ",                         \"categoryId\":\"" + categoryId + "\""
                + ",                         \"installationFee\":\"" + installationFee + "\""
                + ",                         \"status\":\"" + status + "\""
                + ",                         \"objectVersionNumber\":\"" + objectVersionNumber + "\""
                + "}}";
    }
}