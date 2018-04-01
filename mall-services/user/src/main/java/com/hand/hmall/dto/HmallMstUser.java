package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;
/**
 * @author 阳赳
 * @name:HmallMstUser
 * @Description:表HMALL_MST_USER对应实体类
 * @date 2017/6/1 16:11
 */
@Entity
@Table(name = "HMALL_MST_USER")
public class HmallMstUser {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_USER_S.nextval from dual")
    private Long userId;

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
     * 接口同步标记
     */
    private String syncflag;

    /**
     * 版本号
     */
    private Short objectVersionNumber;

    /**
     * null
     */
    private Date creationDate;

    /**
     * null
     */
    private Short createdBy;

    /**
     * null
     */
    private Short lastUpdatedBy;

    /**
     * null
     */
    private Date lastUpdateDate;

    /**
     * null
     */
    private Short lastUpdateLogin;

    /**
     * null
     */
    private Short programApplicationId;

    /**
     * null
     */
    private Short programId;

    /**
     * null
     */
    private Date programUpdateDate;

    /**
     * null
     */
    private Short requestId;

    /**
     * null
     */
    private String attributeCategory;

    /**
     * 密码
     */
    private String password;

    /**
     * 生日
     */
    private Date birthday;

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

    /**
     * 微信OpenId
     * @return
     */
    @Column(name = "wx_openid")
    private String wxOpenId;
    /**
     * QQ OpenId
     */
    @Column(name = "qq_openid")
    private String qqOpenId;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public Short getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Short objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Short getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Short createdBy) {
        this.createdBy = createdBy;
    }

    public Short getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Short lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Short getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Short lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Short getProgramApplicationId() {
        return programApplicationId;
    }

    public void setProgramApplicationId(Short programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    public Short getProgramId() {
        return programId;
    }

    public void setProgramId(Short programId) {
        this.programId = programId;
    }

    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    public Short getRequestId() {
        return requestId;
    }

    public void setRequestId(Short requestId) {
        this.requestId = requestId;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }
}