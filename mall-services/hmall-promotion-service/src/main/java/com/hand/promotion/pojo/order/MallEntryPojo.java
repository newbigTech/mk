package com.hand.promotion.pojo.order;

import java.io.Serializable;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/3
 * @description
 */
public class MallEntryPojo implements Serializable{
    /**
     *
     */
    private String lineNumber;


    /**
     * 产品编码
     */
    private String product;

    /**
     * 产品id
     */
    private String productId;


    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 门店编码
     */
    private String pointOfService;

    /**
     * 套件编码
     */
    private String suitCode;


    /**
     * 前台传入商品单价
     */
    private Double basePrice;



    /**
     * 订单行商品数量
     */
    private Integer quantity;

    /**
     * 订单行优惠金额
     */
    private Double discountFee;

    /**
     * 订单头优惠分摊到行的金额
     */
    private Double discountFeel;

    /**
     * 订单行总优惠金额
     */
    private Double totalDiscount;

    /**
     * 优惠后行商品单价
     */
    private Double unitFee;

    /**
     * 订单行应付金额
     */
    private Double totalFee;

    /**
     * 订单头优惠券促销分摊到行上的金额
     */
    private Double couponFee;

    /**
     * 行是否为赠品
     */
    private String isGift = "N";


    /**
     * 运费
     */
    private Double shippingFee;

    /**
     * 安装费
     */
    private Double installationFee;

    /**
     * 运费减免
     */
    private Double preShippingFee;
    /**
     * 安装费减免
     */
    private Double preInstallationFee;

    /**
     * 商品包装体积
     */
    private String productPackageSize;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public String getSuitCode() {
        return suitCode;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }



    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        return discountFeel;
    }

    public void setDiscountFeel(Double discountFeel) {
        this.discountFeel = discountFeel;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public Double getPreShippingFee() {
        return preShippingFee;
    }

    public void setPreShippingFee(Double preShippingFee) {
        this.preShippingFee = preShippingFee;
    }

    public Double getPreInstallationFee() {
        return preInstallationFee;
    }

    public void setPreInstallationFee(Double preInstallationFee) {
        this.preInstallationFee = preInstallationFee;
    }

    public String getProductPackageSize() {
        return productPackageSize;
    }

    public void setProductPackageSize(String productPackageSize) {
        this.productPackageSize = productPackageSize;
    }
}
