package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;
/**
 * @author 梅新养
 * @name:HmallMstPointOfService
 * @Description:HMALL_MST_POINTOFSERVICE 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "HMALL_MST_POINTOFSERVICE")
public class HmallMstPointOfService {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_POINTOFSERVICE_S.nextval from dual")
    private Long pointOfServiceId;

    /**
     * 地点
     */
    private String code;

    /**
     * 中文名称
     */
    private String displayname;

    /**
     * 服务点类型
     */
    private String type;

    /**
     * 发送货物的目的地运输区域 
     */
    private String zone1;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 住宅号及街道
     */
    private String address;

    /**
     * 店面状态 
     */
    private String shopstatus;

    /**
     * 服务中心/分拨中心
     */
    private String zzwerks;

    /**
     * 公司代码
     */
    private String bukrs;

    /**
     * 接口同步标记
     */
    private String syncflag;

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
     * 主键
     * @return POINT_OF_SERVICE_ID 主键
     */
    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    /**
     * 主键
     * @param pointOfServiceId 主键
     */
    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    /**
     * 地点
     * @return CODE 地点
     */
    public String getCode() {
        return code;
    }

    /**
     * 地点
     * @param code 地点
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 中文名称
     * @return DISPLAYNAME 中文名称
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * 中文名称
     * @param displayname 中文名称
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname == null ? null : displayname.trim();
    }

    /**
     * 服务点类型
     * @return TYPE 服务点类型
     */
    public String getType() {
        return type;
    }

    /**
     * 服务点类型
     * @param type 服务点类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 发送货物的目的地运输区域 
     * @return ZONE1 发送货物的目的地运输区域 
     */
    public String getZone1() {
        return zone1;
    }

    /**
     * 发送货物的目的地运输区域 
     * @param zone1 发送货物的目的地运输区域 
     */
    public void setZone1(String zone1) {
        this.zone1 = zone1 == null ? null : zone1.trim();
    }

    /**
     * 联系电话
     * @return CONTACT_NUMBER 联系电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 联系电话
     * @param contactNumber 联系电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber == null ? null : contactNumber.trim();
    }

    /**
     * 住宅号及街道
     * @return ADDRESS 住宅号及街道
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住宅号及街道
     * @param address 住宅号及街道
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 店面状态 
     * @return SHOPSTATUS 店面状态 
     */
    public String getShopstatus() {
        return shopstatus;
    }

    /**
     * 店面状态 
     * @param shopstatus 店面状态 
     */
    public void setShopstatus(String shopstatus) {
        this.shopstatus = shopstatus == null ? null : shopstatus.trim();
    }

    /**
     * 服务中心/分拨中心
     * @return ZZWERKS 服务中心/分拨中心
     */
    public String getZzwerks() {
        return zzwerks;
    }

    /**
     * 服务中心/分拨中心
     * @param zzwerks 服务中心/分拨中心
     */
    public void setZzwerks(String zzwerks) {
        this.zzwerks = zzwerks == null ? null : zzwerks.trim();
    }

    /**
     * 公司代码
     * @return BUKRS 公司代码
     */
    public String getBukrs() {
        return bukrs;
    }

    /**
     * 公司代码
     * @param bukrs 公司代码
     */
    public void setBukrs(String bukrs) {
        this.bukrs = bukrs == null ? null : bukrs.trim();
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
}