package com.hand.hmall.as.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Table;
import java.math.BigDecimal;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_TMREFUND")
/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsTmrefundImportDto
 * @description 天猫退款单页面导出Excel模板对应的dto
 * @date 2017/9/14
 */
public class AsTmrefundImportDto extends BaseDTO {
    public static final String DEFAULT_SHEET_NAME = "天猫退款单";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";
    @ExcelVOAttribute(name = "订单编号", column = "A", isExport = true)
    private String escOrderCode;

    @ExcelVOAttribute(name = "退款编号", column = "B", isExport = true)
    private String code;

    @ExcelVOAttribute(name = "支付宝交易号", column = "C", isExport = true)
    private String alipayNo;

    @ExcelVOAttribute(name = "订单付款时间", column = "D", isExport = true)
    private String orderPaymentTime;

    @ExcelVOAttribute(name = "商家编码", column = "E", isExport = true)
    private Long productId;

    @ExcelVOAttribute(name = "退款完结时间", column = "F", isExport = true)
    private String refundFinishTime;

    @ExcelVOAttribute(name = "买家会员名称", column = "G", isExport = true)
    private String buyerNick;

    @ExcelVOAttribute(name = "买家实际支付金额", column = "H", isExport = true)
    private BigDecimal actualPaidAmount;

    @ExcelVOAttribute(name = "宝贝标题", column = "I", isExport = true)
    private String title;

    @ExcelVOAttribute(name = "买家退款金额", column = "J", isExport = true)
    private BigDecimal refundFee;

    @ExcelVOAttribute(name = "手工退款/系统退款", column = "K", isExport = true)
    private String manualOrAuto;

    @ExcelVOAttribute(name = "是否需要退货", column = "L", isExport = true)
    private String hasGoodReturn;

    @ExcelVOAttribute(name = "退款的申请时间", column = "M", isExport = true)
    private String created;

    @ExcelVOAttribute(name = "超时时间", column = "N", isExport = true)
    private String timeout;

    @ExcelVOAttribute(name = "退款状态", column = "O", isExport = true)
    private String status;

    @ExcelVOAttribute(name = "货物状态", column = "P", isExport = true)
    private String goodStatus;

    @ExcelVOAttribute(name = "退货物流信息", column = "Q", isExport = true)
    private String returnLogistics;

    @ExcelVOAttribute(name = "发货物流信息", column = "R", isExport = true)
    private String consignmentLogistics;

    @ExcelVOAttribute(name = "客服介入状态", column = "S", isExport = true)
    private String csStatus;

    @ExcelVOAttribute(name = "卖家真实姓名", column = "T", isExport = true)
    private String sellerName;

    @ExcelVOAttribute(name = "卖家退货地址", column = "U", isExport = true)
    private String sellerAddress;

    @ExcelVOAttribute(name = "卖家邮编", column = "V", isExport = true)
    private String sellerZip;

    @ExcelVOAttribute(name = "卖家电话", column = "W", isExport = true)
    private String sellerPhone;

    @ExcelVOAttribute(name = "卖家手机", column = "X", isExport = true)
    private String sellerMobile;

    @ExcelVOAttribute(name = "退货物流单号", column = "Y", isExport = true)
    private String sid;

    @ExcelVOAttribute(name = "退货物流公司", column = "Z", isExport = true)
    private String companyName;

    @ExcelVOAttribute(name = "买家退款原因", column = "AA", isExport = true)
    private String reason;

    @ExcelVOAttribute(name = "买家退款说明", column = "AB", isExport = true)
    private String refundDesc;

    @ExcelVOAttribute(name = "买家退货时间", column = "AC", isExport = true)
    private String goodReturnTime;

    @ExcelVOAttribute(name = "责任方", column = "AD", isExport = true)
    private String responsibilitySide;

    @ExcelVOAttribute(name = "售中或售后", column = "AE", isExport = true)
    private String refundPhase;

    @ExcelVOAttribute(name = "商家备注", column = "AF", isExport = true)
    private String sellerNote;

    @ExcelVOAttribute(name = "完结时间", column = "AG", isExport = true)
    private String finishTime;

    @ExcelVOAttribute(name = "部分退款/全部退款", column = "AH", isExport = true)
    private String refundScope;

    @ExcelVOAttribute(name = "审核操作人", column = "AI", isExport = true)
    private String auditPerson;

    @ExcelVOAttribute(name = "举证超时", column = "AJ", isExport = true)
    private String burdenTimeout;

    @ExcelVOAttribute(name = "是否零秒响应", column = "AK", isExport = true)
    private String auditAuto;

    @ExcelVOAttribute(name = "退款操作人", column = "AL", isExport = true)
    private String refundPerson;

    @ExcelVOAttribute(name = "主数据编码", column = "AM", isExport = true)
    private String mainDataCode;

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(String orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getRefundFinishTime() {
        return refundFinishTime;
    }

    public void setRefundFinishTime(String refundFinishTime) {
        this.refundFinishTime = refundFinishTime;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getManualOrAuto() {
        return manualOrAuto;
    }

    public void setManualOrAuto(String manualOrAuto) {
        this.manualOrAuto = manualOrAuto;
    }

    public String getHasGoodReturn() {
        return hasGoodReturn;
    }

    public void setHasGoodReturn(String hasGoodReturn) {
        this.hasGoodReturn = hasGoodReturn;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(String goodStatus) {
        this.goodStatus = goodStatus;
    }

    public String getReturnLogistics() {
        return returnLogistics;
    }

    public void setReturnLogistics(String returnLogistics) {
        this.returnLogistics = returnLogistics;
    }

    public String getConsignmentLogistics() {
        return consignmentLogistics;
    }

    public void setConsignmentLogistics(String consignmentLogistics) {
        this.consignmentLogistics = consignmentLogistics;
    }

    public String getCsStatus() {
        return csStatus;
    }

    public void setCsStatus(String csStatus) {
        this.csStatus = csStatus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerZip() {
        return sellerZip;
    }

    public void setSellerZip(String sellerZip) {
        this.sellerZip = sellerZip;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getGoodReturnTime() {
        return goodReturnTime;
    }

    public void setGoodReturnTime(String goodReturnTime) {
        this.goodReturnTime = goodReturnTime;
    }

    public String getResponsibilitySide() {
        return responsibilitySide;
    }

    public void setResponsibilitySide(String responsibilitySide) {
        this.responsibilitySide = responsibilitySide;
    }

    public String getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(String refundPhase) {
        this.refundPhase = refundPhase;
    }

    public String getSellerNote() {
        return sellerNote;
    }

    public void setSellerNote(String sellerNote) {
        this.sellerNote = sellerNote;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRefundScope() {
        return refundScope;
    }

    public void setRefundScope(String refundScope) {
        this.refundScope = refundScope;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getBurdenTimeout() {
        return burdenTimeout;
    }

    public void setBurdenTimeout(String burdenTimeout) {
        this.burdenTimeout = burdenTimeout;
    }

    public String getAuditAuto() {
        return auditAuto;
    }

    public void setAuditAuto(String auditAuto) {
        this.auditAuto = auditAuto;
    }

    public String getRefundPerson() {
        return refundPerson;
    }

    public void setRefundPerson(String refundPerson) {
        this.refundPerson = refundPerson;
    }

    public String getMainDataCode() {
        return mainDataCode;
    }

    public void setMainDataCode(String mainDataCode) {
        this.mainDataCode = mainDataCode;
    }

    public BigDecimal getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(BigDecimal actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }
}
