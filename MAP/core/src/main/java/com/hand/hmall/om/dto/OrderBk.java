package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name OrderBk
 * @description 订单备份Dto
 * @date 2017年8月4日10:52:23
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ORDER_BK")
public class OrderBk extends BaseDTO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
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
     * 配送方式
     */
    private String shippingType;

    /**
     * 优惠总金额
     */
    private BigDecimal discountFee;

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
     * 当前物流运费
     */
    private BigDecimal postFee;

    /**
     * 当前快递运费
     */
    private BigDecimal epostFee;

    /**
     * 安装费
     */
    private BigDecimal fixFee;

    /**
     * 原物流运费减免
     */
    private BigDecimal prePostfee;

    /**
     * 原快递运费减免
     */
    private BigDecimal preEpostfee;

    /**
     * 原安装费减免
     */
    private BigDecimal preFixfee;

    /**
     * 当前订单金额
     */
    private BigDecimal orderAmount;

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
     * 接口同步标记
     */
    private String syncflag;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 是否允许分批发运
     */
    private String splitAllowed;

    /**
     * 版本号
     */
    private Long objectVersionNumber;

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
    private String quantity;

    @Transient
    private String orderSta;

    @Transient
    private Date startTime;

    @Transient
    private Date endTime;
    @Transient
    private String productId;

    @Transient
    private String vproduct;

    @Transient
    private String customerid;//用户编号

    private Long version;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderSta() {
        return orderSta;
    }

    public void setOrderSta(String orderSta) {
        this.orderSta = orderSta;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    /**
     * 主键
     * @return ORDER_ID 主键
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 主键
     * @param orderId 主键
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 订单编号
     * @return CODE 订单编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 订单编号
     * @param code 订单编号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 平台订单编号
     * @return ESC_ORDER_CODE 平台订单编号
     */
    public String getEscOrderCode() {
        return escOrderCode;
    }

    /**
     * 平台订单编号
     * @param escOrderCode 平台订单编号
     */
    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode == null ? null : escOrderCode.trim();
    }

    /**
     * 订单状态
     * @return ORDER_STATUS 订单状态
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 订单状态
     * @param orderStatus 订单状态
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    /**
     * 用户
     * @return USER_ID 用户
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户
     * @param userId 用户
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 币种
     * @return CURRENCY_ID 币种
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * 币种
     * @param currencyId 币种
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId == null ? null : currencyId.trim();
    }

    /**
     * 网站
     * @return WEBSITE_ID 网站
     */
    public String getWebsiteId() {
        return websiteId;
    }

    /**
     * 网站
     * @param websiteId 网站
     */
    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId == null ? null : websiteId.trim();
    }

    /**
     * 渠道
     * @return SALECHANNEL_ID 渠道
     */
    public String getSalechannelId() {
        return salechannelId;
    }

    /**
     * 渠道
     * @param salechannelId 渠道
     */
    public void setSalechannelId(String salechannelId) {
        this.salechannelId = salechannelId == null ? null : salechannelId.trim();
    }

    /**
     * 店铺
     * @return STORE_ID 店铺
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * 店铺
     * @param storeId 店铺
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    /**
     * 付款金额
     * @return PAYMENT_AMOUNT 付款金额
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * 付款金额
     * @param paymentAmount 付款金额
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * 配送方式
     * @return SHIPPING_TYPE 配送方式
     */
    public String getShippingType() {
        return shippingType;
    }

    /**
     * 配送方式
     * @param shippingType 配送方式
     */
    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    /**
     * 优惠总金额
     * @return DISCOUNT_FEE 优惠总金额
     */
    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    /**
     * 优惠总金额
     * @param discountFee 优惠总金额
     */
    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    /**
     * 平台下单时间
     * @return ORDER_CREATIONTIME 平台下单时间
     */
    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    /**
     * 平台下单时间
     * @param orderCreationtime 平台下单时间
     */
    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    /**
     * 买家备注
     * @return BUYER_MEMO 买家备注
     */
    public String getBuyerMemo() {
        return buyerMemo;
    }

    /**
     * 买家备注
     * @param buyerMemo 买家备注
     */
    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo == null ? null : buyerMemo.trim();
    }

    /**
     * 是否开票
     * @return IS_INVOICED 是否开票
     */
    public String getIsInvoiced() {
        return isInvoiced;
    }

    /**
     * 是否开票
     * @param isInvoiced 是否开票
     */
    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced == null ? null : isInvoiced.trim();
    }

    /**
     * 发票类型
     * @return INVOICE_TYPE 发票类型
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * 发票类型
     * @param invoiceType 发票类型
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    /**
     * 发票抬头
     * @return INVOICE_NAME 发票抬头
     */
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * 发票抬头
     * @param invoiceName 发票抬头
     */
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName == null ? null : invoiceName.trim();
    }

    /**
     * 发票信息
     * @return INVOICE_URL 发票信息
     */
    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    /**
     * 发票信息
     * @param invoiceUrl 发票信息
     */
    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl == null ? null : invoiceUrl.trim();
    }

    /**
     * 当前物流运费
     * @return POST_FEE 当前物流运费
     */
    public BigDecimal getPostFee() {
        return postFee;
    }

    /**
     * 当前物流运费
     * @param postFee 当前物流运费
     */
    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    /**
     * 当前快递运费
     * @return EPOST_FEE 当前快递运费
     */
    public BigDecimal getEpostFee() {
        return epostFee;
    }

    /**
     * 当前快递运费
     * @param epostFee 当前快递运费
     */
    public void setEpostFee(BigDecimal epostFee) {
        this.epostFee = epostFee;
    }

    /**
     * 安装费
     * @return FIX_FEE 安装费
     */
    public BigDecimal getFixFee() {
        return fixFee;
    }

    /**
     * 安装费
     * @param fixFee 安装费
     */
    public void setFixFee(BigDecimal fixFee) {
        this.fixFee = fixFee;
    }

    /**
     * 原物流运费减免
     * @return PRE_POSTFEE 原物流运费减免
     */
    public BigDecimal getPrePostfee() {
        return prePostfee;
    }

    /**
     * 原物流运费减免
     * @param prePostfee 原物流运费减免
     */
    public void setPrePostfee(BigDecimal prePostfee) {
        this.prePostfee = prePostfee;
    }

    /**
     * 原快递运费减免
     * @return PRE_EPOSTFEE 原快递运费减免
     */
    public BigDecimal getPreEpostfee() {
        return preEpostfee;
    }

    /**
     * 原快递运费减免
     * @param preEpostfee 原快递运费减免
     */
    public void setPreEpostfee(BigDecimal preEpostfee) {
        this.preEpostfee = preEpostfee;
    }

    /**
     * 原安装费减免
     * @return PRE_FIXFEE 原安装费减免
     */
    public BigDecimal getPreFixfee() {
        return preFixfee;
    }

    /**
     * 原安装费减免
     * @param preFixfee 原安装费减免
     */
    public void setPreFixfee(BigDecimal preFixfee) {
        this.preFixfee = preFixfee;
    }

    /**
     * 当前订单金额
     * @return ORDER_AMOUNT 当前订单金额
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * 当前订单金额
     * @param orderAmount 当前订单金额
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * 是否齐套
     * @return TOTALCON 是否齐套
     */
    public String getTotalcon() {
        return totalcon;
    }

    /**
     * 是否齐套
     * @param totalcon 是否齐套
     */
    public void setTotalcon(String totalcon) {
        this.totalcon = totalcon == null ? null : totalcon.trim();
    }

    /**
     * 收货人姓名
     * @return RECEIVER_NAME 收货人姓名
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 收货人姓名
     * @param receiverName 收货人姓名
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    @Override
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 收货人地址
     * @return RECEIVER_ADDRESS 收货人地址
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * 收货人地址
     * @param receiverAddress 收货人地址
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    /**
     * 收货人邮编
     * @return RECEIVER_ZIP 收货人邮编
     */
    public String getReceiverZip() {
        return receiverZip;
    }

    /**
     * 收货人邮编
     * @param receiverZip 收货人邮编
     */
    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
    }

    /**
     * 收货人手机号
     * @return RECEIVER_MOBILE 收货人手机号
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * 收货人手机号
     * @param receiverMobile 收货人手机号
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile == null ? null : receiverMobile.trim();
    }

    /**
     * 收货人电话
     * @return RECEIVER_PHONE 收货人电话
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 收货人电话
     * @param receiverPhone 收货人电话
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    /**
     * 预计交货时间
     * @return ESTIMATE_DELIVERY_TIME 预计交货时间
     */
    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    /**
     * 预计交货时间
     * @param estimateDeliveryTime 预计交货时间
     */
    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    /**
     * 增加预计发货时间
     * @return ESTIMATE_CON_TIME 增加预计发货时间
     */
    public Date getEstimateConTime() {
        return estimateConTime;
    }

    /**
     * 增加预计发货时间
     * @param estimateConTime 增加预计发货时间
     */
    public void setEstimateConTime(Date estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    /**
     * 付款比例
     * @return PAY_RATE 付款比例
     */
    public String getPayRate() {
        return payRate;
    }

    /**
     * 付款比例
     * @param payRate 付款比例
     */
    public void setPayRate(String payRate) {
        this.payRate = payRate == null ? null : payRate.trim();
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
     * 支付状态
     * @return PAY_STATUS 支付状态
     */
    public String getPayStatus() {
        return payStatus;
    }

    /**
     * 支付状态
     * @param payStatus 支付状态
     */
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    /**
     * 是否允许分批发运
     * @return SPLIT_ALLOWED 是否允许分批发运
     */
    public String getSplitAllowed() {
        return splitAllowed;
    }

    /**
     * 是否允许分批发运
     * @param splitAllowed 是否允许分批发运
     */
    public void setSplitAllowed(String splitAllowed) {
        this.splitAllowed = splitAllowed == null ? null : splitAllowed.trim();
    }

    /**
     * 版本号
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * 订单已锁定
     * @return LOCKED 订单已锁定
     */
    public String getLocked() {
        return locked;
    }

    /**
     * 订单已锁定
     * @param locked 订单已锁定
     */
    public void setLocked(String locked) {
        this.locked = locked == null ? null : locked.trim();
    }

    /**
     * 优惠券优惠总金额
     * @return COUPON_FEE 优惠券优惠总金额
     */
    public Double getCouponFee() {
        return couponFee;
    }

    /**
     * 优惠券优惠总金额
     * @param couponFee 优惠券优惠总金额
     */
    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    /**
     * 总优惠金额
     * @return TOTAL_DISCOUNT 总优惠金额
     */
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * 总优惠金额
     * @param totalDiscount 总优惠金额
     */
    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    /**
     * 所选优惠券
     * @return CHOSEN_COUPON 所选优惠券
     */
    public String getChosenCoupon() {
        return chosenCoupon;
    }

    /**
     * 所选优惠券
     * @param chosenCoupon 所选优惠券
     */
    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon == null ? null : chosenCoupon.trim();
    }

    /**
     * 所选促销
     * @return CHOSEN_PROMOTION 所选促销
     */
    public String getChosenPromotion() {
        return chosenPromotion;
    }

    /**
     * 所选促销
     * @param chosenPromotion 所选促销
     */
    public void setChosenPromotion(String chosenPromotion) {
        this.chosenPromotion = chosenPromotion == null ? null : chosenPromotion.trim();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}