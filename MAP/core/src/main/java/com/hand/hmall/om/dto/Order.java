package com.hand.hmall.om.dto;


import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Order
 * @description 订单dto
 * @date 2017年5月26日10:52:23
 */

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ORDER")
public class Order extends BaseDTO {
    @Id
    @GeneratedValue
    private Long orderId;

    @ExcelVOAttribute(name = "订单编码", column = "B", isExport = true)
    private String code;

    @ExcelVOAttribute(name = "平台订单编号", column = "C", isExport = true)
    private String escOrderCode;


    private String orderStatus;


    private Long userId;

    private String currencyId;

    private String websiteId;

    private String salechannelId;

    private String storeId;

    @ExcelVOAttribute(name = "支付金额", column = "J", isExport = true)
    private Double paymentAmount;

    @ExcelVOAttribute(name = "总金额", column = "K", isExport = true)
    private BigDecimal orderAmount;

    private BigDecimal discountFee;

    @ExcelVOAttribute(name = "下单时间", column = "A", isExport = true)
    private Date orderCreationtime;

    private String buyerMemo;

    private String isInvoiced;

    private String invoiceType;

    private String invoiceName;

    private String invoiceUrl;

    private BigDecimal postFee;

    private BigDecimal fixFee;

    private BigDecimal epostFee;

    private BigDecimal preFixfee;

    private BigDecimal prePostfee;

    private BigDecimal preEpostfee;

    private String totalcon;

    @ExcelVOAttribute(name = "收货人", column = "G", isExport = true)
    private String receiverName;

    private String receiverCountry;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    @ExcelVOAttribute(name = "收货人手机", column = "H", isExport = true)
    private String receiverMobile;

    private String receiverPhone;

    @ExcelVOAttribute(name = "预计交期", column = "L", isExport = true)
    private Date estimateDeliveryTime;

    private Date estimateConTime;

    private String payRate;

    private String syncflag;

    private String payStatus;

    private String splitAllowed;

    private String locked;

    private BigDecimal couponFee;

    private BigDecimal totalDiscount;

    private String chosenCoupon;

    private String chosenPromotion;

    private String isIo;

    private String isOut;

    private Date tradeFinishTime;

    private String invoiceEntityId;

    private String invoiceEntityAddr;

    private String invoiceEntityPhone;

    private String invoiceBankName;

    private String invoiceBankAccount;

    private String orderType;

    private BigDecimal refundAmount;

    @Transient
    private Date startTime;

    @Transient
    private Date endTime;
    @Transient
    private String productId;

    @Transient
    private String vproduct;

    @Transient
    private String name;//用户组
    @Transient
    @ExcelVOAttribute(name = "用户ID", column = "F", isExport = true)
    private String customerid;//用户编号
    @Transient
    private Date payTime;//支付时间
    @Transient
    private String sex;//性別
    @Transient
    private String regionCode;//地区编码
    @Transient
    private String shippingType;//发运方式
    @Transient
    private String posCode;//服务点编码
    @Transient
    private String logisticsCompanies;//快递公司
    @Transient
    private String logisticsNumber;//快递号
    @Transient
    private String deliveryCode;//发货单号

    @Transient
    private String account;//支付账户
    @Transient
    private String payMode;//支付渠道
    @Transient
    private Long payAmount;//支付金额
    @Transient
    private String userName;//用户昵称
    @Transient
    private String usergroupCode;//用户组编码

    @Transient
    private String displayName; //店铺名称

    @Transient
    @ExcelVOAttribute(name = "订单归属", column = "D", isExport = true)
    private String responsibleName; // 订单负责人姓名

    /**
     * 商品数量
     */
    @ExcelVOAttribute(name = "商品数量", column = "I", isExport = true)
    @Transient
    private String quantity;
    @Transient
    private String orderSta;

    @Transient
    private String address;

    @Transient
    private String mobile;

    @Transient
    private String orderCode;

    private String syncZmall;

    @Transient
    private List<OrderEntry> orderEntries;

    @Transient
    private List<OrderEntry> orderEntryList;

    @Transient
    private BigDecimal historyTotalRefundAmount;
    //订单状态中文，用于订单列表页面
    @Transient
    @ExcelVOAttribute(name = "订单状态", column = "E", isExport = true)
    private String meaning;

    private Double currentAmount;
    /**
     * 订单差额 保价接口使用
     */
    @Transient
    private Double extraReduce;

    private String cancelRefundUncreate;//取消未生成退款

    /**
     * 发货单ID
     */
    @Transient
    private Long consignmentId;

    @Transient
    private List<OmPromotionrule> activities;

    @Transient
    private List<OrderCouponrule> couponList;
    @Transient
    private PromotionResult promotionResult;


    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Double getExtraReduce() {
        return extraReduce;
    }

    public void setExtraReduce(Double extraReduce) {
        this.extraReduce = extraReduce;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(Date tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(BigDecimal couponFee) {
        this.couponFee = couponFee;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsergroupCode() {
        return usergroupCode;
    }

    public void setUsergroupCode(String usergroupCode) {
        this.usergroupCode = usergroupCode;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public String getSalechannelId() {
        return salechannelId;
    }

    public void setSalechannelId(String salechannelId) {
        this.salechannelId = salechannelId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public String getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public BigDecimal getFixFee() {
        return fixFee;
    }

    public void setFixFee(BigDecimal fixFee) {
        this.fixFee = fixFee;
    }

    public String getTotalcon() {
        return totalcon;
    }

    public void setTotalcon(String totalcon) {
        this.totalcon = totalcon;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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

    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public Date getEstimateConTime() {
        return estimateConTime;
    }

    public void setEstimateConTime(Date estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getLogisticsCompanies() {
        return logisticsCompanies;
    }

    public void setLogisticsCompanies(String logisticsCompanies) {
        this.logisticsCompanies = logisticsCompanies;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(BigDecimal epostFee) {
        this.epostFee = epostFee;
    }

    public BigDecimal getPreFixfee() {
        return preFixfee;
    }

    public void setPreFixfee(BigDecimal preFixfee) {
        this.preFixfee = preFixfee;
    }

    public BigDecimal getPrePostfee() {
        return prePostfee;
    }

    public void setPrePostfee(BigDecimal prePostfee) {
        this.prePostfee = prePostfee;
    }

    public BigDecimal getPreEpostfee() {
        return preEpostfee;
    }

    public void setPreEpostfee(BigDecimal preEpostfee) {
        this.preEpostfee = preEpostfee;
    }

    public String getIsIo() {
        return isIo;
    }

    public void setIsIo(String isIo) {
        this.isIo = isIo;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getSyncZmall() {
        return syncZmall;
    }

    public void setSyncZmall(String syncZmall) {
        this.syncZmall = syncZmall;
    }

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    public void setOrderEntries(List<OrderEntry> orderEntries) {
        this.orderEntries = orderEntries;
    }

    public String getInvoiceEntityId() {
        return invoiceEntityId;
    }

    public void setInvoiceEntityId(String invoiceEntityId) {
        this.invoiceEntityId = invoiceEntityId;
    }

    public String getInvoiceEntityAddr() {
        return invoiceEntityAddr;
    }

    public void setInvoiceEntityAddr(String invoiceEntityAddr) {
        this.invoiceEntityAddr = invoiceEntityAddr;
    }

    public String getInvoiceEntityPhone() {
        return invoiceEntityPhone;
    }

    public void setInvoiceEntityPhone(String invoiceEntityPhone) {
        this.invoiceEntityPhone = invoiceEntityPhone;
    }

    public String getInvoiceBankName() {
        return invoiceBankName;
    }

    public void setInvoiceBankName(String invoiceBankName) {
        this.invoiceBankName = invoiceBankName;
    }

    public String getInvoiceBankAccount() {
        return invoiceBankAccount;
    }

    public void setInvoiceBankAccount(String invoiceBankAccount) {
        this.invoiceBankAccount = invoiceBankAccount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCancelRefundUncreate() {
        return cancelRefundUncreate;
    }

    public void setCancelRefundUncreate(String cancelRefundUncreate) {
        this.cancelRefundUncreate = cancelRefundUncreate;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public BigDecimal getHistoryTotalRefundAmount() {
        return historyTotalRefundAmount;
    }

    public void setHistoryTotalRefundAmount(BigDecimal historyTotalRefundAmount) {
        this.historyTotalRefundAmount = historyTotalRefundAmount;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public List<OmPromotionrule> getActivities() {
        return activities;
    }

    public void setActivities(List<OmPromotionrule> activities) {
        this.activities = activities;
    }

    public List<OrderCouponrule> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<OrderCouponrule> couponList) {
        this.couponList = couponList;
    }

    public List<OrderEntry> getOrderEntryList() {
        return orderEntryList;
    }

    public void setOrderEntryList(List<OrderEntry> orderEntryList) {
        this.orderEntryList = orderEntryList;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public PromotionResult getPromotionResult() {
        return promotionResult;
    }

    public void setPromotionResult(PromotionResult promotionResult) {
        this.promotionResult = promotionResult;
    }
}
