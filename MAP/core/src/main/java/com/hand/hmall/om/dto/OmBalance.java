package com.hand.hmall.om.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_BALANCE")
public class OmBalance extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long balanceId;

    @Column
    private Long infoId;

    @Column
    private Long accountId;

    @Column
    private String type;

    @Column
    private String isBalance;


    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setIsBalance(String isBalance) {
        this.isBalance = isBalance;
    }

    public String getIsBalance() {
        return isBalance;
    }

}
