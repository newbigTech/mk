package com.hand.hmall.om.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Create at 2017年7月28日9:07:44
 * @Create by shoupeng.wei@hand-china.com
 * @Description: 订单推送商城相关Dto
 */
@Table(name="HMALL_OM_ORDER")
public class OrderSyncDto {

    @JsonIgnoreProperties
    private Long orderId;

    @Transient
    private String escOrderCode;    //平台订单编号,默认

    private String orderStatus;     //订单状态

    @Transient
    private String customerId;      //会员编码

    private String currencyCode;    //货币编码

    @Transient
    private String websiteCode;    //网站编码

    @Transient
    private String channelCode;    //渠道编码

    @Transient
    private String storeCode;    //店铺编码

    private String invoiceType;    //发票类型

    private String receiverName;    //收货人姓名

    private String countryCode;    //国家编码

    private String stateCode;

    private String cityCode;

    private String districtCode;

    private String receiverAddress;

    private String receiverZip;

    private String receiverMobile;

    private String receiverPhone;

    private String payRate;    //付款比例

    private BigDecimal paymentAmount;    //付款金额

    private BigDecimal orderAmount;    //订单金额

    private BigDecimal realOrderAmount;    //当前应付金额，为空时传入 orderAmount 的值


    private BigDecimal refundAmount;    //已退款金额

    private BigDecimal discountFee;    //优惠总金额

    private String buyerMemo;    //买家备注

    private String orderCreationtime;    //平台下单时间

    private String isInvoiced;    //是否开票

    private String invoiceName;    //发票抬头

    private String invoiceUrl;    //发票信息

    private BigDecimal postFee;    //物流运费

    private BigDecimal fixFee;    //安装费

    private BigDecimal epostFee;    //快递运费

    private BigDecimal prePostFee;    //物流运费减免

    private BigDecimal preEpostFee;    //快递运费减免

    private BigDecimal preFixFee;    //安装费减免

    private String totalcon;    //是否齐套

    private String code;    //订单编码

    private String shippingType;    //配送方式

    private String estimateDeliveryTime;    //预计交货时间

    private String estimateConTime;       //预计发货时间


    private BigDecimal couponFee;

    private BigDecimal totalDiscount;

    private String chosenCoupon;

    private String chosenPromotion;

    private String syncZmall;

    private String invoiceEntityId;

    private String invoiceEntityAddr;

    private String invoiceEntityPhone;

    private String invoiceBankName;

    private String invoiceBankAccount;
    @Transient
    private BigDecimal netAmount;

    @Transient
    private List<OrderEntryDto> entryList;

    @Transient
    private List<OmPromotionruleDto> orderPromotionList;

    @Transient
    private List<OrderCouponruleDto> orderCouponList;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
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

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public String getOrderCreationtime() {
        return orderCreationtime;
    }

    public void setOrderCreationtime(String orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    public String getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced;
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

    public BigDecimal getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(BigDecimal epostFee) {
        this.epostFee = epostFee;
    }

    public BigDecimal getPrePostFee() {
        return prePostFee;
    }

    public void setPrePostFee(BigDecimal prePostFee) {
        this.prePostFee = prePostFee;
    }

    public BigDecimal getPreEpostFee() {
        return preEpostFee;
    }

    public void setPreEpostFee(BigDecimal preEpostFee) {
        this.preEpostFee = preEpostFee;
    }

    public BigDecimal getPreFixFee() {
        return preFixFee;
    }

    public void setPreFixFee(BigDecimal preFixFee) {
        this.preFixFee = preFixFee;
    }

    public String getTotalcon() {
        return totalcon;
    }

    public void setTotalcon(String totalcon) {
        this.totalcon = totalcon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(String estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getEstimateConTime() {
        return estimateConTime;
    }

    public void setEstimateConTime(String estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    public List<OrderEntryDto> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<OrderEntryDto> entryList) {
        this.entryList = entryList;
    }

    public List<OmPromotionruleDto> getOrderPromotionList() {
        return orderPromotionList;
    }

    public void setOrderPromotionList(List<OmPromotionruleDto> orderPromotionList) {
        this.orderPromotionList = orderPromotionList;
    }

    public List<OrderCouponruleDto> getOrderCouponList() {
        return orderCouponList;
    }

    public void setOrderCouponList(List<OrderCouponruleDto> orderCouponList) {
        this.orderCouponList = orderCouponList;
    }

    public String getSyncZmall() {
        return syncZmall;
    }

    public void setSyncZmall(String syncZmall) {
        this.syncZmall = syncZmall;
    }

    public BigDecimal getRealOrderAmount() {
        return realOrderAmount;
    }

    public void setRealOrderAmount(BigDecimal realOrderAmount) {
        this.realOrderAmount = realOrderAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    @Override
    public String toString() {
        return "OrderSyncDto{" +
                "orderId=" + orderId +
                ", escOrderCode='" + escOrderCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", customerId='" + customerId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", websiteCode='" + websiteCode + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverZip='" + receiverZip + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", payRate='" + payRate + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", orderAmount=" + orderAmount +
                ", realOrderAmount=" + realOrderAmount +
                ", discountFee=" + discountFee +
                ", buyerMemo='" + buyerMemo + '\'' +
                ", refundAmount='" + refundAmount + '\'' +
                ", orderCreationtime='" + orderCreationtime + '\'' +
                ", isInvoiced='" + isInvoiced + '\'' +
                ", invoiceName='" + invoiceName + '\'' +
                ", invoiceUrl='" + invoiceUrl + '\'' +
                ", postFee=" + postFee +
                ", fixFee=" + fixFee +
                ", epostFee=" + epostFee +
                ", prePostFee=" + prePostFee +
                ", preEpostFee=" + preEpostFee +
                ", preFixFee=" + preFixFee +
                ", totalcon='" + totalcon + '\'' +
                ", code='" + code + '\'' +
                ", shippingType='" + shippingType + '\'' +
                ", estimateDeliveryTime='" + estimateDeliveryTime + '\'' +
                ", estimateConTime='" + estimateConTime + '\'' +
                ", couponFee=" + couponFee +
                ", totalDiscount=" + totalDiscount +
                ", chosenCoupon='" + chosenCoupon + '\'' +
                ", chosenPromotion='" + chosenPromotion + '\'' +
                ", syncZmall='" + syncZmall + '\'' +
                ", entryList=" + entryList +
                ", orderPromotionList=" + orderPromotionList +
                ", orderCouponList=" + orderCouponList +
                '}';
    }
}
