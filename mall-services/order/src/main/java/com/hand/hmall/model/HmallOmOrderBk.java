package com.hand.hmall.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name OrderBk
 * @description 订单备份Dto
 * @date 2017年8月4日10:52:23
 */
@Entity
@Table(name = "HMALL_OM_ORDER_BK")
public class HmallOmOrderBk {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_ORDER_BK_S.nextval from dual")
    private Long orderId;

    /**
     * 订单编号

     */
    private String code;

    /**
     * 平台订单编号
     */
    private String escOrderCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 币种
     */
    private String currencyId;

    /**
     * 网站
     */
    private String websiteId;

    /**
     * 渠道
     */
    private String salechannelId;

    /**
     * 店铺
     */
    private String storeId;

    /**
     * 付款金额
     */
    private Double paymentAmount;

    /**
     * 订单金额
     */
    private Double orderAmount;

    /**
     * 促销优惠总金额
     */
    private Double discountFee;

    /**
     * 平台下单时间
     */
    private Date orderCreationtime;

    /**
     * 买家备注
     */
    private String buyerMemo;

    /**
     * 是否开票
     */
    private String isInvoiced;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 发票抬头
     */
    private String invoiceName;

    /**
     * 发票信息
     */
    private String invoiceUrl;

    /**
     * 物流运费
     */
    private Double postFee;

    /**
     * 快递运费
     */
    private Double epostFee;

    /**
     * 安装费
     */
    private Double fixFee;

    private Double prePostfee;

    private Double preEpostfee;

    private Double preFixfee;

    /**
     * 物流运费减免
     */
    @Column(name = "PRE_POSTFEE")
    private Double prePostFee;

    /**
     * 快递运费减免
     */
    @Column(name = "PRE_EPOSTFEE")
    private Double preEpostFee;

    /**
     * 安装费减免
     */
    @Column(name = "PRE_FIXFEE")
    private Double preFixFee;

    /**
     * 是否齐套
     */
    private String totalcon;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人国家
     */
    private String receiverCountry;

    /**
     * 收货人省
     */
    private String receiverState;

    /**
     * 收货人市
     */
    private String receiverCity;

    /**
     * 收货人区
     */
    private String receiverDistrict;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 收货人邮编
     */
    private String receiverZip;

    /**
     * 收货人手机号
     */
    private String receiverMobile;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 预计交货时间
     */
    private Date estimateDeliveryTime;

    /**
     * 增加预计发货时间
     */
    private Date estimateConTime;

    /**
     * 付款比例
     */
    private String payRate;

    /**
     * 接口传输标示
     */
    private String syncflag;

    private String payStatus;

    private String splitAllowed;

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
     * 配送方式
     */
    private String shippingType;

    /**
     * 订单已锁定
     */
    private String locked;

    /**
     * 优惠券优惠总金额
     */
    private Double couponFee;

    /**
     * 总优惠金额
     */
    private Double totalDiscount;

    /**
     * 所选优惠券
     */
    private String chosenCoupon;

    /**
     * 所选促销
     */
    private String chosenPromotion;

    @Transient
    private String customerId;

    @Transient
    private String websiteCode;

    @Transient
    private String channelCode;

    @Transient
    private String storeCode;

    @Transient
    private String countryCode;

    @Transient
    private String stateCode;

    @Transient
    private String cityCode;

    @Transient
    private String districtCode;

    @Transient
    private String currencyCode;

    /*
    * 备份版本
    * */
    private Long version;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * 主键
     *
     * @return ORDER_ID 主键
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 主键
     *
     * @param orderId 主键
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 订单编号
     *
     * @return CODE 订单编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 订单编号
     *
     * @param code 订单编号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 平台订单编号
     *
     * @return ESC_ORDER_CODE 平台订单编号
     */
    public String getEscOrderCode() {
        return escOrderCode;
    }

    /**
     * 平台订单编号
     *
     * @param escOrderCode 平台订单编号
     */
    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode == null ? null : escOrderCode.trim();
    }

    /**
     * 订单状态
     *
     * @return ORDER_STATUS 订单状态
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 订单状态
     *
     * @param orderStatus 订单状态
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    /**
     * 用户
     *
     * @return USER_ID 用户
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户
     *
     * @param userId 用户
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 币种
     *
     * @return CURRENCY_ID 币种
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * 币种
     *
     * @param currencyId 币种
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId == null ? null : currencyId.trim();
    }

    /**
     * 网站
     *
     * @return WEBSITE_ID 网站
     */
    public String getWebsiteId() {
        return websiteId;
    }

    /**
     * 网站
     *
     * @param websiteId 网站
     */
    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId == null ? null : websiteId.trim();
    }

    /**
     * 渠道
     *
     * @return SALECHANNEL_ID 渠道
     */
    public String getSalechannelId() {
        return salechannelId;
    }

    /**
     * 渠道
     *
     * @param salechannelId 渠道
     */
    public void setSalechannelId(String salechannelId) {
        this.salechannelId = salechannelId == null ? null : salechannelId.trim();
    }

    /**
     * 店铺
     *
     * @return STORE_ID 店铺
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * 店铺
     *
     * @param storeId 店铺
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    /**
     * 付款金额
     *
     * @return PAYMENT_AMOUNT 付款金额
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * 付款金额
     *
     * @param paymentAmount 付款金额
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * 订单金额
     *
     * @return ORDER_AMOUNT 订单金额
     */
    public Double getOrderAmount() {
        return orderAmount;
    }

    /**
     * 订单金额
     *
     * @param orderAmount 订单金额
     */
    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * 促销优惠总金额
     *
     * @return DISCOUNT_FEE 促销优惠总金额
     */
    public Double getDiscountFee() {
        return discountFee;
    }

    /**
     * 促销优惠总金额
     *
     * @param discountFee 促销优惠总金额
     */
    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    /**
     * 平台下单时间
     *
     * @return ORDER_CREATIONTIME 平台下单时间
     */
    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    /**
     * 平台下单时间
     *
     * @param orderCreationtime 平台下单时间
     */
    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    /**
     * 买家备注
     *
     * @return BUYER_MEMO 买家备注
     */
    public String getBuyerMemo() {
        return buyerMemo;
    }

    /**
     * 买家备注
     *
     * @param buyerMemo 买家备注
     */
    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo == null ? null : buyerMemo.trim();
    }

    /**
     * 是否开票
     *
     * @return IS_INVOICED 是否开票
     */
    public String getIsInvoiced() {
        return isInvoiced;
    }

    /**
     * 是否开票
     *
     * @param isInvoiced 是否开票
     */
    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced == null ? null : isInvoiced.trim();
    }

    /**
     * 发票类型
     *
     * @return INVOICE_TYPE 发票类型
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * 发票类型
     *
     * @param invoiceType 发票类型
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    /**
     * 发票抬头
     *
     * @return INVOICE_NAME 发票抬头
     */
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * 发票抬头
     *
     * @param invoiceName 发票抬头
     */
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName == null ? null : invoiceName.trim();
    }

    /**
     * 发票信息
     *
     * @return INVOICE_URL 发票信息
     */
    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    /**
     * 发票信息
     *
     * @param invoiceUrl 发票信息
     */
    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl == null ? null : invoiceUrl.trim();
    }


    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public Double getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(Double epostFee) {
        this.epostFee = epostFee;
    }

    public Double getFixFee() {
        return fixFee;
    }

    public void setFixFee(Double fixFee) {
        this.fixFee = fixFee;
    }

    public Double getPrePostFee() {
        return prePostFee;
    }

    public void setPrePostFee(Double prePostFee) {
        this.prePostFee = prePostFee;
    }

    public Double getPreEpostFee() {
        return preEpostFee;
    }

    public void setPreEpostFee(Double preEpostFee) {
        this.preEpostFee = preEpostFee;
    }

    public Double getPreFixFee() {
        return preFixFee;
    }

    public void setPreFixFee(Double preFixFee) {
        this.preFixFee = preFixFee;
    }

    /**
     * 是否齐套
     *
     * @return TOTALCON 是否齐套
     */
    public String getTotalcon() {
        return totalcon;
    }

    /**
     * 是否齐套
     *
     * @param totalcon 是否齐套
     */
    public void setTotalcon(String totalcon) {
        this.totalcon = totalcon == null ? null : totalcon.trim();
    }

    /**
     * 收货人姓名
     *
     * @return RECEIVER_NAME 收货人姓名
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 收货人姓名
     *
     * @param receiverName 收货人姓名
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    /**
     * 收货人国家
     *
     * @return RECEIVER_COUNTRY 收货人国家
     */
    public String getReceiverCountry() {
        return receiverCountry;
    }

    /**
     * 收货人国家
     *
     * @param receiverCountry 收货人国家
     */
    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    /**
     * 收货人省
     *
     * @return RECEIVER_STATE 收货人省
     */
    public String getReceiverState() {
        return receiverState;
    }

    /**
     * 收货人省
     *
     * @param receiverState 收货人省
     */
    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    /**
     * 收货人市
     *
     * @return RECEIVER_CITY 收货人市
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * 收货人市
     *
     * @param receiverCity 收货人市
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     * 收货人区
     *
     * @return RECEIVER_DISTRICT 收货人区
     */
    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    /**
     * 收货人区
     *
     * @param receiverDistrict 收货人区
     */
    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    /**
     * 收货人地址
     *
     * @return RECEIVER_ADDRESS 收货人地址
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * 收货人地址
     *
     * @param receiverAddress 收货人地址
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    /**
     * 收货人邮编
     *
     * @return RECEIVER_ZIP 收货人邮编
     */
    public String getReceiverZip() {
        return receiverZip;
    }

    /**
     * 收货人邮编
     *
     * @param receiverZip 收货人邮编
     */
    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
    }

    /**
     * 收货人手机号
     *
     * @return RECEIVER_MOBILE 收货人手机号
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * 收货人手机号
     *
     * @param receiverMobile 收货人手机号
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile == null ? null : receiverMobile.trim();
    }

    /**
     * 收货人电话
     *
     * @return RECEIVER_PHONE 收货人电话
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 收货人电话
     *
     * @param receiverPhone 收货人电话
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    /**
     * 预计交货时间
     *
     * @return ESTIMATE_DELIVERY_TIME 预计交货时间
     */
    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    /**
     * 预计交货时间
     *
     * @param estimateDeliveryTime 预计交货时间
     */
    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    /**
     * 增加预计发货时间
     *
     * @return ESTIMATE_CON_TIME 增加预计发货时间
     */
    public Date getEstimateConTime() {
        return estimateConTime;
    }

    /**
     * 增加预计发货时间
     *
     * @param estimateConTime 增加预计发货时间
     */
    public void setEstimateConTime(Date estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    /**
     * 付款比例
     *
     * @return PAY_RATE 付款比例
     */
    public String getPayRate() {
        return payRate;
    }

    /**
     * 付款比例
     *
     * @param payRate 付款比例
     */
    public void setPayRate(String payRate) {
        this.payRate = payRate == null ? null : payRate.trim();
    }

    /**
     * 接口传输标示
     *
     * @return SYNCFLAG 接口传输标示
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口传输标示
     *
     * @param syncflag 接口传输标示
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }

    /**
     * 版本号
     *
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     *
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     *
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     *
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     *
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     *
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     *
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     *
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     *
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     *
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     *
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     *
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     *
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     *
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     *
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     *
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     *
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public String getChosenPromotion() {
        return chosenPromotion;
    }

    public void setChosenPromotion(String chosenPromotion) {
        this.chosenPromotion = chosenPromotion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Double getPrePostfee() {
        return prePostfee;
    }

    public void setPrePostfee(Double prePostfee) {
        this.prePostfee = prePostfee;
    }

    public Double getPreEpostfee() {
        return preEpostfee;
    }

    public void setPreEpostfee(Double preEpostfee) {
        this.preEpostfee = preEpostfee;
    }

    public Double getPreFixfee() {
        return preFixfee;
    }

    public void setPreFixfee(Double preFixfee) {
        this.preFixfee = preFixfee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getSplitAllowed() {
        return splitAllowed;
    }

    public void setSplitAllowed(String splitAllowed) {
        this.splitAllowed = splitAllowed;
    }
}