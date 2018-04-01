package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;
/**
 * @author 梅新养
 * @name:HmallMstUser
 * @Description:HMALL_MST_USER 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
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
     * 版本号
     */
    private Double objectVersionNumber;

    /**
     * null
     */
    private Date creationDate;

    /**
     * null
     */
    private Long createdBy;

    /**
     * null
     */
    private Long lastUpdatedBy;

    /**
     * null
     */
    private Date lastUpdateDate;

    /**
     * null
     */
    private Long lastUpdateLogin;

    /**
     * null
     */
    private Long programApplicationId;

    /**
     * null
     */
    private Long programId;

    /**
     * null
     */
    private Date programUpdateDate;

    /**
     * null
     */
    private Long requestId;

    /**
     * null
     */
    private String attributeCategory;


    /**
     * 生日
     */
    private Date birthday;

    /**
     * 密码
     */
    private String password;

    /**
     * 会员等级
     */
    private String userLevel;

    /**
     * 主键
     * @return USER_ID 主键
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 主键
     * @param userId 主键
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 会员编码手机号
     * @return CUSTOMERID 会员编码手机号
     */
    public String getCustomerid() {
        return customerid;
    }

    /**
     * 会员编码手机号
     * @param customerid 会员编码手机号
     */
    public void setCustomerid(String customerid) {
        this.customerid = customerid == null ? null : customerid.trim();
    }

    /**
     * 性别
     * @return SEX 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 性别
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 手机号
     * @return MOBILE_NUMBER 手机号
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * 手机号
     * @param mobileNumber 手机号
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber == null ? null : mobileNumber.trim();
    }

    /**
     * 昵称
     * @return NAME 昵称
     */
    public String getName() {
        return name;
    }

    /**
     * 昵称
     * @param name 昵称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 是否在黑名单
     * @return IS_BLACK_LIST 是否在黑名单
     */
    public String getIsBlackList() {
        return isBlackList;
    }

    /**
     * 是否在黑名单
     * @param isBlackList 是否在黑名单
     */
    public void setIsBlackList(String isBlackList) {
        this.isBlackList = isBlackList == null ? null : isBlackList.trim();
    }

    /**
     * 标签
     * @return LABEL 标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 标签
     * @param label 标签
     */
    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    /**
     * 备注
     * @return REMARK 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 邮件
     * @return EMAIL 邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮件
     * @param email 邮件
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 接口同步标记
     * @return SYNCFLAG 接口同步标记
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口同步标记
     * @param syncflag 接口同步标记
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }

    /**
     * 国家
     * @return COUNTRY 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 国家
     * @param country 国家
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * 省
     * @return REGION 省
     */
    public String getRegion() {
        return region;
    }

    /**
     * 省
     * @param region 省
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * 市
     * @return CITY 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 市
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 区
     * @return DISTRICT 区
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 区
     * @param district 区
     */
    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    /**
     * 版本号
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }

    /**
     * 生日
     * @return BIRTHDAY 生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 生日
     * @param birthday 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 密码
     * @return PASSWORD 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 会员等级
     * @return USER_LEVEL 会员等级
     */
    public String getUserLevel() {
        return userLevel;
    }

    /**
     * 会员等级
     * @param userLevel 会员等级
     */
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel == null ? null : userLevel.trim();
    }
}