package com.hand.hmall.dto;

import java.util.Date;
/**
 * @author 阳赳
 * @name:UserPojo
 * @Description:保存前台进行数据传输的dto
 * @date 2017/6/1 16:11
 */
public class UserPojo {
    /**
     * 会员编码手机号
     */
    private String customerid;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String mobileNumber;

    /**
     * 昵称
     */
    private String name;

    /**
     * 是否在黑名单
     */
    private String isBlackList;

    /**
     * 标签
     */
    private String label;

    /**
     * 备注
     */
    private String remark;

    /**
     * 邮件
     */
    private String email;


    /**
     * null
     */
    private Date creationDate;

    /**
     * 密码
     */
    private String password;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String region;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 用户等级
     */
    private String userLevel;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsBlackList() {
        return isBlackList;
    }

    public void setIsBlackList(String isBlackList) {
        this.isBlackList = isBlackList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
