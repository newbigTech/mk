package com.hand.hmall.dto;

import javax.persistence.*;

@Entity
@Table(name = "HMALL_MST_POINTOFSERVICE")
public class MstPointOfService {
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
     * null
     */
    private String attributeCategory;


    /**
     * 给日日顺的发货人邮编
     */
    private String delZipCode;

    /**
     * 给日日顺的发货人省份
     */
    private String delProvince;

    /**
     * 给日日顺的发货人城市
     */
    private String delCity;

    /**
     * 给日日顺的发货人地区
     */
    private String delDistrict;

    /**
     * 给日日顺的发货人电话
     */
    private String delPhone;

    /**
     * 给日日顺的发货人地址
     */
    private String delAddress;

    /**
     * 给日日顺的发货人手机
     */
    private String delMobile;

    /**
     * 主键
     *
     * @return POINT_OF_SERVICE_ID 主键
     */
    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    /**
     * 主键
     *
     * @param pointOfServiceId 主键
     */
    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    /**
     * 地点
     *
     * @return CODE 地点
     */
    public String getCode() {
        return code;
    }

    /**
     * 地点
     *
     * @param code 地点
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 中文名称
     *
     * @return DISPLAYNAME 中文名称
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * 中文名称
     *
     * @param displayname 中文名称
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname == null ? null : displayname.trim();
    }

    /**
     * 服务点类型
     *
     * @return TYPE 服务点类型
     */
    public String getType() {
        return type;
    }

    /**
     * 服务点类型
     *
     * @param type 服务点类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 发送货物的目的地运输区域
     *
     * @return ZONE1 发送货物的目的地运输区域
     */
    public String getZone1() {
        return zone1;
    }

    /**
     * 发送货物的目的地运输区域
     *
     * @param zone1 发送货物的目的地运输区域
     */
    public void setZone1(String zone1) {
        this.zone1 = zone1 == null ? null : zone1.trim();
    }

    /**
     * 联系电话
     *
     * @return CONTACT_NUMBER 联系电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 联系电话
     *
     * @param contactNumber 联系电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber == null ? null : contactNumber.trim();
    }

    /**
     * 住宅号及街道
     *
     * @return ADDRESS 住宅号及街道
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住宅号及街道
     *
     * @param address 住宅号及街道
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 店面状态
     *
     * @return SHOPSTATUS 店面状态
     */
    public String getShopstatus() {
        return shopstatus;
    }

    /**
     * 店面状态
     *
     * @param shopstatus 店面状态
     */
    public void setShopstatus(String shopstatus) {
        this.shopstatus = shopstatus == null ? null : shopstatus.trim();
    }

    /**
     * 服务中心/分拨中心
     *
     * @return ZZWERKS 服务中心/分拨中心
     */
    public String getZzwerks() {
        return zzwerks;
    }

    /**
     * 服务中心/分拨中心
     *
     * @param zzwerks 服务中心/分拨中心
     */
    public void setZzwerks(String zzwerks) {
        this.zzwerks = zzwerks == null ? null : zzwerks.trim();
    }

    /**
     * 公司代码
     *
     * @return BUKRS 公司代码
     */
    public String getBukrs() {
        return bukrs;
    }

    /**
     * 公司代码
     *
     * @param bukrs 公司代码
     */
    public void setBukrs(String bukrs) {
        this.bukrs = bukrs == null ? null : bukrs.trim();
    }

    /**
     * 接口同步标记
     *
     * @return SYNCFLAG 接口同步标记
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口同步标记
     *
     * @param syncflag 接口同步标记
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }


    /**
     * null
     *
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     *
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }


    /**
     * 给日日顺的发货人邮编
     *
     * @return DEL_ZIP_CODE 给日日顺的发货人邮编
     */
    public String getDelZipCode() {
        return delZipCode;
    }

    /**
     * 给日日顺的发货人邮编
     *
     * @param delZipCode 给日日顺的发货人邮编
     */
    public void setDelZipCode(String delZipCode) {
        this.delZipCode = delZipCode == null ? null : delZipCode.trim();
    }

    /**
     * 给日日顺的发货人省份
     *
     * @return DEL_PROVINCE 给日日顺的发货人省份
     */
    public String getDelProvince() {
        return delProvince;
    }

    /**
     * 给日日顺的发货人省份
     *
     * @param delProvince 给日日顺的发货人省份
     */
    public void setDelProvince(String delProvince) {
        this.delProvince = delProvince == null ? null : delProvince.trim();
    }

    /**
     * 给日日顺的发货人城市
     *
     * @return DEL_CITY 给日日顺的发货人城市
     */
    public String getDelCity() {
        return delCity;
    }

    /**
     * 给日日顺的发货人城市
     *
     * @param delCity 给日日顺的发货人城市
     */
    public void setDelCity(String delCity) {
        this.delCity = delCity == null ? null : delCity.trim();
    }

    /**
     * 给日日顺的发货人地区
     *
     * @return DEL_DISTRICT 给日日顺的发货人地区
     */
    public String getDelDistrict() {
        return delDistrict;
    }

    /**
     * 给日日顺的发货人地区
     *
     * @param delDistrict 给日日顺的发货人地区
     */
    public void setDelDistrict(String delDistrict) {
        this.delDistrict = delDistrict == null ? null : delDistrict.trim();
    }

    /**
     * 给日日顺的发货人电话
     *
     * @return DEL_PHONE 给日日顺的发货人电话
     */
    public String getDelPhone() {
        return delPhone;
    }

    /**
     * 给日日顺的发货人电话
     *
     * @param delPhone 给日日顺的发货人电话
     */
    public void setDelPhone(String delPhone) {
        this.delPhone = delPhone == null ? null : delPhone.trim();
    }

    /**
     * 给日日顺的发货人地址
     *
     * @return DEL_ADDRESS 给日日顺的发货人地址
     */
    public String getDelAddress() {
        return delAddress;
    }

    /**
     * 给日日顺的发货人地址
     *
     * @param delAddress 给日日顺的发货人地址
     */
    public void setDelAddress(String delAddress) {
        this.delAddress = delAddress == null ? null : delAddress.trim();
    }

    /**
     * 给日日顺的发货人手机
     *
     * @return DEL_MOBILE 给日日顺的发货人手机
     */
    public String getDelMobile() {
        return delMobile;
    }

    /**
     * 给日日顺的发货人手机
     *
     * @param delMobile 给日日顺的发货人手机
     */
    public void setDelMobile(String delMobile) {
        this.delMobile = delMobile == null ? null : delMobile.trim();
    }
}