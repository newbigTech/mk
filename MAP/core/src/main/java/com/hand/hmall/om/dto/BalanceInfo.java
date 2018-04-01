package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.*;

import java.math.BigDecimal;

import static com.hand.hap.core.BaseConstants.GENERATOR_TYPE;

/**
 * @author shoupeng.wei
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/11/1 19:49
 */
@ExtensionAttribute(disable=true)
@Table(name = "HMALL_OM_BALANCE")
public class BalanceInfo {

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

    @Transient
    private String transaction;

    @Transient
    private String serialNum;

    @Transient
    private String outTradeNo;

    @Transient
    private String accountsType;

    @Transient
    private String channel;

    @Transient
    private String userCode;

    @Transient
    private String accountsDate;

    @Transient
    private String accountsDateFrom;

    @Transient
    private String accountsDateTo;

    @Transient
    private String accountsTime;

    @Transient
    private BigDecimal accountsAmount;

    @Transient
    private String voucherno;

    @Transient
    private String escOrderCode;

    @Transient
    private String infoCode;

    @Transient
    private String infoNumberCode;

    @Transient
    private String infoOutTradeCode;

    @Transient
    private String infoTime;

    @Transient
    private BigDecimal infoAmount;

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsBalance() {
        return isBalance;
    }

    public void setIsBalance(String isBalance) {
        this.isBalance = isBalance;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAccountsType() {
        return accountsType;
    }

    public void setAccountsType(String accountsType) {
        this.accountsType = accountsType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAccountsDate() {
        return accountsDate;
    }

    public void setAccountsDate(String accountsDate) {
        this.accountsDate = accountsDate;
    }

    public String getAccountsTime() {
        return accountsTime;
    }

    public void setAccountsTime(String accountsTime) {
        this.accountsTime = accountsTime;
    }

    public BigDecimal getAccountsAmount() {
        return accountsAmount;
    }

    public void setAccountsAmount(BigDecimal accountsAmount) {
        this.accountsAmount = accountsAmount;
    }

    public String getVoucherno() {
        return voucherno;
    }

    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public String getInfoNumberCode() {
        return infoNumberCode;
    }

    public void setInfoNumberCode(String infoNumberCode) {
        this.infoNumberCode = infoNumberCode;
    }

    public String getInfoOutTradeCode() {
        return infoOutTradeCode;
    }

    public void setInfoOutTradeCode(String infoOutTradeCode) {
        this.infoOutTradeCode = infoOutTradeCode;
    }

    public String getInfoTime() {
        return infoTime;
    }

    public void setInfoTime(String infoTime) {
        this.infoTime = infoTime;
    }

    public BigDecimal getInfoAmount() {
        return infoAmount;
    }

    public void setInfoAmount(BigDecimal infoAmount) {
        this.infoAmount = infoAmount;
    }

    public String getAccountsDateFrom() {
        return accountsDateFrom;
    }

    public void setAccountsDateFrom(String accountsDateFrom) {
        this.accountsDateFrom = accountsDateFrom;
    }

    public String getAccountsDateTo() {
        return accountsDateTo;
    }

    public void setAccountsDateTo(String accountsDateTo) {
        this.accountsDateTo = accountsDateTo;
    }
}
