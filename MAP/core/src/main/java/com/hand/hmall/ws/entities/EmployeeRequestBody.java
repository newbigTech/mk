package com.hand.hmall.ws.entities;

import java.util.Date;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: retail推送员工信息到hmall中的员工信息实体类
 * @date 2017/6/19 15:14
 */
public class EmployeeRequestBody {

    private Long employeeId;

    private String employeeCode;

    private String name;

    private Date bornDate;

    private String email;

    private String mobil;

    private Date joinDate;

    private String gender;

    private String certificateId;

    private String status;

    private String enabledFlag;

    public void setEmployeeId(Long employeeId){
        this.employeeId = employeeId;
    }

    public Long getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeCode(String employeeCode){
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode(){
        return employeeCode;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setBornDate(Date bornDate){
        this.bornDate = bornDate;
    }

    public Date getBornDate(){
        return bornDate;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setMobil(String mobil){
        this.mobil = mobil;
    }

    public String getMobil(){
        return mobil;
    }

    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }

    public Date getJoinDate(){
        return joinDate;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return gender;
    }

    public void setCertificateId(String certificateId){
        this.certificateId = certificateId;
    }

    public String getCertificateId(){
        return certificateId;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setEnabledFlag(String enabledFlag){
        this.enabledFlag = enabledFlag;
    }

    public String getEnabledFlag(){
        return enabledFlag;
    }
}
