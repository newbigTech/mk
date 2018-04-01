package com.hand.hmall.om.tpl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenzhigang
 * @version 0.1
 * @name TmallOrderTemplate
 * @description 天猫订单导入模板
 * @date 2017年8月7日
 */
@ExtensionAttribute(disable = true)
public class TmallOrderTemplate {


    // 订单号(ORDER_NUMBER, isExport = true)
    @ExcelVOAttribute(name = "订单号", column = "A", isExport = true)
    private String orderNumber; // 1

    // 拍下时间(CREATE_TIME, isExport = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelVOAttribute(name = "拍下时间", column = "B", isExport = true)
    private Date createTime; // 2

    // 总价(TOTAL_SALES, isExport = true)
    @ExcelVOAttribute(name = "总价", column = "C", isExport = true)
    private BigDecimal totalSales; // 3

    // 运费(TRANSPORTATION_COSTS, isExport = true)
    @ExcelVOAttribute(name = "运费", column = "D", isExport = true)
    private BigDecimal transportationCosts; // 4

    // 买家留言(BUYER_MESSAGE, isExport = true)
    @ExcelVOAttribute(name = "买家留言", column = "E", isExport = true)
    private String buyerMessage; // 5

    // 买家昵称(BUYER_NICKNAME, isExport = true)
    @ExcelVOAttribute(name = "买家昵称", column = "F", isExport = true)
    private String buyerNickname; // 6

    // 收件人(THE_RECIPIENT, isExport = true)
    @ExcelVOAttribute(name = "收件人", column = "G", isExport = true)
    private String theRecipient; // 7

    // 省(PROVINCE, isExport = true)
    @ExcelVOAttribute(name = "省", column = "H", isExport = true)
    private String province; // 8

    // 市(CITY, isExport = true)
    @ExcelVOAttribute(name = "市", column = "I", isExport = true)
    private String city; // 9

    // 区(AREA, isExport = true)
    @ExcelVOAttribute(name = "区", column = "J", isExport = true)
    private String area; // 10

    // 地址(ADDRESS, isExport = true)
    @ExcelVOAttribute(name = "地址", column = "K", isExport = true)
    private String address; // 11

    // 宝贝商家编码(PRODUCT_BUSINESS_CODE, isExport = true)
    @ExcelVOAttribute(name = "属性商家编码", column = "L", isExport = true)
    private String productBusinessCode; // 12

    // 购买数量(PURCHASE_QUANTITY, isExport = true)
    @ExcelVOAttribute(name = "购买数量", column = "M", isExport = true)
    private Integer purchaseQuantity; // 13

    // 实际单价(ACTUAL_UNIT_PRICE, isExport = true)
    @ExcelVOAttribute(name = "实际单价", column = "N", isExport = true)
    private BigDecimal actualUnitPrice; // 14

    // 应付金额(AMOUNTS_PAYABLE, isExport = true)
    @ExcelVOAttribute(name = "应付金额", column = "O", isExport = true)
    private BigDecimal amountsPayable; // 15

    // 发票抬头(INVOICE, isExport = true)
    @ExcelVOAttribute(name = "发票抬头", column = "P", isExport = true)
    private String invoice; // 16

    // 电话(TELEPHONE, isExport = true)
    @ExcelVOAttribute(name = "电话", column = "Q", isExport = true)
    private String telephone; // 17

    // 座机(LANDLINE, isExport = true)
    @ExcelVOAttribute(name = "座机", column = "R", isExport = true)
    private String landline; // 18

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTransportationCosts() {
        return transportationCosts;
    }

    public void setTransportationCosts(BigDecimal transportationCosts) {
        this.transportationCosts = transportationCosts;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNickname() {
        return buyerNickname;
    }

    public void setBuyerNickname(String buyerNickname) {
        this.buyerNickname = buyerNickname;
    }

    public String getTheRecipient() {
        return theRecipient;
    }

    public void setTheRecipient(String theRecipient) {
        this.theRecipient = theRecipient;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductBusinessCode() {
        return productBusinessCode;
    }

    public void setProductBusinessCode(String productBusinessCode) {
        this.productBusinessCode = productBusinessCode;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public BigDecimal getActualUnitPrice() {
        return actualUnitPrice;
    }

    public void setActualUnitPrice(BigDecimal actualUnitPrice) {
        this.actualUnitPrice = actualUnitPrice;
    }

    public BigDecimal getAmountsPayable() {
        return amountsPayable;
    }

    public void setAmountsPayable(BigDecimal amountsPayable) {
        this.amountsPayable = amountsPayable;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }
}
