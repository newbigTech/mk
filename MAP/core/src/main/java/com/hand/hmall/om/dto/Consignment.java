package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Consignment
 * @description 发货单dto
 * @date 2017年6月5日13:54:47
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_CONSIGNMENT")
public class Consignment extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long consignmentId;

    @Column
    private String code;

    @Column
    private String brand;

    @Column
    private Long orderId;

    @Column
    private Long logisticsCompanies;

    @Column
    private String logisticsNumber;

    @Column
    private Long pointOfServiceId;

    @Column
    private String shippingType;

    @Column
    private Date shippingDate;

    @Column
    private String receiverName;

    @Column
    private String receiverCountry;

    @Column
    private String receiverState;

    @Column
    private String receiverCity;

    @Column
    private String receiverDistrict;

    @Column
    private String receiverTown;

    @Column
    private String receiverAddress;

    @Column
    private String receiverZip;

    @Column
    private String receiverMobile;

    @Column
    private String receiverPhone;

    @Column
    private String note;

    @Column
    private Date estimateDeliveryTime;

    @Column
    private String splitAllowed;

    @Column
    private String syncflag;

    @Column
    private Long approvedBy;

    @Column
    private Date approvedDate;

    @Column
    private Integer approvedTimes;

    @Column
    private String splitReason;

    @Column
    private String abnormalReason;

    @Column
    private String csApproved;
    @Column
    private String mergeConsignment;

    private String canDelivery;
    
    @Transient
    private String customerid;//会员编码

    @Transient
    private String name;//会员名

    @Transient
    private String sex;//性别

    @Transient
    private String payStatus;//支付状态

    @Transient
    private String payRate;//支付比例

    @Transient
    private Date orderCreationtime;//订单创建时间

    @Transient
    private String orderNumber;//订单号

    @Transient
    private String userLevel;//用户级别

    @Transient
    private String pointCode;//服务点

    @Transient
    private String regionCode;//服务点

    @Column
    private String sapCode;

    @Column
    private String status;

    @Transient
    private Date lastUpdateDateTime; //最新更新时间

    @Transient
    private String orderType;

    @Transient
    private String status1;//发货单状态

    @Transient
    private String status2;//发货单状态

    @Transient
    private String status3;//发货单状态

    @Transient
    private List<String> orderStatusList;

    @Transient
    private List<String> shippingTypeList;

    @Transient
    private String status4;//发货单状态

    @Transient
    private String shippingType1;//发运方式

    @Transient
    private String shippingType2;//发运方式

    @Transient
    private String shippingType3;//发运方式

    @Transient
    private String provice; //收货人省名

    @Transient
    private String city;//收货人市名

    @Transient
    private String startTime; //下单时间
    @Transient
    private String endTime;//下单时间

    @Transient
    private String corporateName;//快递公司名称

    @Transient
    private String corporateCode;//快递公司编码

    @Transient
    private String groupName;//会员组名

    @Transient
    private String shippingName;//发运方式名称

    @Transient
    private String consignmentStatus;//发货单状态名称

    @Transient
    private List<OrderEntry> consignmentEntries;

    @Transient
    private String orderCode;

    @Transient
    private String logisticsCompany;

    @Transient
    private String pointOfService;

    @Transient
    private String lang; //系统语言环境

    @Transient
    private String soldParty; //推送订单到retail时，传递webSide表中的售达方

    @Transient
    private String salesOffice;  //推送订单到retail时，传递webSide表中的销售办公室

    @Transient
    private Date tradeFinishTime; //推送订单到retail时，传递电商完结时间

    @Transient
    private String storeCode;//店铺code
    @Transient
    private List<OrderEntry> orderEntries;

    @Transient
    private List<DeliveryOrderDto> deliveryOrders;

    private String syncZmall;

    private Date tradeFinishedTime;

    public Date getTradeFinishedTime() {
        return tradeFinishedTime;
    }

    public void setTradeFinishedTime(Date tradeFinishedTime) {
        this.tradeFinishedTime = tradeFinishedTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    private String pause;

    private String pauseReason;

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSoldParty() {
        return soldParty;
    }

    public void setSoldParty(String soldParty) {
        this.soldParty = soldParty;
    }

    public String getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public Date getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(Date tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStatus4() {
        return status4;
    }

    public void setStatus4(String status4) {
        this.status4 = status4;
    }

    public String getSyncZmall() {
        return syncZmall;
    }

    public void setSyncZmall(String syncZmall) {
        this.syncZmall = syncZmall;
    }

    public String getStatus3() {
        return status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3;
    }

    public String getShippingType3() {
        return shippingType3;
    }

    public void setShippingType3(String shippingType3) {
        this.shippingType3 = shippingType3;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(String consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateCode() {
        return corporateCode;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShippingType1() {
        return shippingType1;
    }

    public void setShippingType1(String shippingType1) {
        this.shippingType1 = shippingType1;
    }

    public String getShippingType2() {
        return shippingType2;
    }

    public void setShippingType2(String shippingType2) {
        this.shippingType2 = shippingType2;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getLogisticsCompanies() {
        return logisticsCompanies;
    }

    public void setLogisticsCompanies(Long logisticsCompanies) {
        this.logisticsCompanies = logisticsCompanies;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverTown(String receiverTown) {
        this.receiverTown = receiverTown;
    }

    public String getReceiverTown() {
        return receiverTown;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setSplitAllowed(String splitAllowed) {
        this.splitAllowed = splitAllowed;
    }

    public String getSplitAllowed() {
        return splitAllowed;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getApprovedTimes() {
        return approvedTimes;
    }

    public void setApprovedTimes(Integer approvedTimes) {
        this.approvedTimes = approvedTimes;
    }

    public String getSplitReason() {
        return splitReason;
    }

    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }

    public String getAbnormalReason() {
        return abnormalReason;
    }

    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason;
    }

    public String getCsApproved() {
        return csApproved;
    }

    public void setCsApproved(String csApproved) {
        this.csApproved = csApproved;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public List<OrderEntry> getConsignmentEntries() {
        return consignmentEntries;
    }

    public void setConsignmentEntries(List<OrderEntry> consignmentEntries) {
        this.consignmentEntries = consignmentEntries;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    public void setOrderEntries(List<OrderEntry> orderEntries) {
        this.orderEntries = orderEntries;
    }

    public String getCanDelivery() {
        return canDelivery;
    }

    public void setCanDelivery(String canDelivery) {
        this.canDelivery = canDelivery;
    }

    public List<String> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<String> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public List<String> getShippingTypeList() {
        return shippingTypeList;
    }

    public void setShippingTypeList(List<String> shippingTypeList) {
        this.shippingTypeList = shippingTypeList;
    }

    public String getMergeConsignment() {
        return mergeConsignment;
    }

    public void setMergeConsignment(String mergeConsignment) {
        this.mergeConsignment = mergeConsignment;
    }

    public List<DeliveryOrderDto> getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(List<DeliveryOrderDto> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
}
