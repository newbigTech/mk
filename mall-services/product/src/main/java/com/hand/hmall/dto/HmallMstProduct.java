package com.hand.hmall.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author XinyangMei
 * @Title HmallMstProduct
 * @Description 表HMALL_MST_PRODUCT对应的实体类
 * @date 2017/6/28 15:25
 * @version 1.0
 */
@Entity
@Table(name = "HMALL_MST_PRODUCT")
public class HmallMstProduct implements Serializable{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_PRODUCT_S.nextval from dual")
    private Long productId;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 版本目录
     */
    private Long catalogversionId;

    /**
     * 上下架状态
     */
    private String approvalStatus;

    /**
     * 单位
     */
    private String unit;

    /**
     * 主图链接
     */
    private Long imageUrlId;

    /**
     * 缩略图链接
     */
    private Long thumbnilUrlId;

    /**
     * 基础商品
     */
    private Long baseProductId;

    /**
     * 变体商品
     */
    private Long variantProductId;

    /**
     * 商品类型
     */
    private String productType;

    /**
     * 商品定制类型
     */
    private String customType;

    /**
     * 是否套件
     */
    private String isSuit;

    /**
     * 是否单独销售
     */
    private String isSinSale;

    /**
     * 商品品牌
     */
    private Long brandId;

    /**
     * 是否可销售
     */
    private String saleStatus;

    /**
     * 产品状态
     */
    private String productStatus;

    /**
     * 上市时间
     */
    private Date listingTime;

    /**
     * 退市时间
     */
    private Date delistingTime;

    /**
     * 特卖标记
     */
    private String isSpeSale;

    /**
     * 产国
     */
    private String originCountry;

    /**
     * 源地
     */
    private String sourcePlace;

    /**
     * 最小订单数量
     */
    private int miniOrderQuantity;

    /**
     * 转换单位转换率
     */
    private double rate;
    @Column(name = "V_PRODUCT_CODE")
    private String vProductCode;

    /**
     * 风格
     */
    private String stype;

    /**
     * 新产品设计类型
     */
    private String designType;

    /**
     * 长
     */
    private double pLong;

    /**
     * 宽
     */
    private double pWide;

    /**
     * 高
     */
    private double pHigh;

    /**
     * 尺寸说明
     */
    private String sizeSpecification;

    /**
     * 尺寸单位
     */
    private String sizeUnit;

    /**
     * 包装长度
     */
    private double packingLong;

    /**
     * 包装宽度
     */
    private double packingWide;

    /**
     * 包装高度
     */
    private double packingHigh;

    /**
     * 包装单位
     */
    private String packingUnit;

    /**
     * 包装体积
     */
    private double packingVolume;

    /**
     * 包装体积单位
     */
    private String packingVolUnit;

    /**
     * 毛重
     */
    private double grossWeight;

    /**
     * 净重
     */
    private double netWeight;

    /**
     * 重量单位
     */
    private String weightUnit;

    /**
     * 外形
     */
    private String shape;

    /**
     * 材质（价签）
     */
    private String texture;

    /**
     * 材质（说明书）
     */
    private String textureSpecification;

    /**
     * 保养说明
     */
    private String maintainSpecification;

    /**
     * 包含树脂
     */
    private String isResin;

    /**
     * 净容量
     */
    private double netCapacity;

    /**
     * 净容量单位
     */
    private String netCapacityUnit;

    /**
     * 保质期
     */
    private String qgp;

    /**
     * 接口传输标示
     */
    private String syncflag;

    private String warehouse;

    /**
     * 版本号
     */
    private Long objectVersionNumber;

    private String defaultDelivery;

    private String customSupportType;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCatalogversionId() {
        return catalogversionId;
    }

    public void setCatalogversionId(Long catalogversionId) {
        this.catalogversionId = catalogversionId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getImageUrlId() {
        return imageUrlId;
    }

    public void setImageUrlId(Long imageUrlId) {
        this.imageUrlId = imageUrlId;
    }

    public Long getThumbnilUrlId() {
        return thumbnilUrlId;
    }

    public void setThumbnilUrlId(Long thumbnilUrlId) {
        this.thumbnilUrlId = thumbnilUrlId;
    }

    public Long getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(Long baseProductId) {
        this.baseProductId = baseProductId;
    }

    public Long getVariantProductId() {
        return variantProductId;
    }

    public void setVariantProductId(Long variantProductId) {
        this.variantProductId = variantProductId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(String isSuit) {
        this.isSuit = isSuit;
    }

    public String getIsSinSale() {
        return isSinSale;
    }

    public void setIsSinSale(String isSinSale) {
        this.isSinSale = isSinSale;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Date getListingTime() {
        return listingTime;
    }

    public void setListingTime(Date listingTime) {
        this.listingTime = listingTime;
    }

    public Date getDelistingTime() {
        return delistingTime;
    }

    public void setDelistingTime(Date delistingTime) {
        this.delistingTime = delistingTime;
    }

    public String getIsSpeSale() {
        return isSpeSale;
    }

    public void setIsSpeSale(String isSpeSale) {
        this.isSpeSale = isSpeSale;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getSourcePlace() {
        return sourcePlace;
    }

    public void setSourcePlace(String sourcePlace) {
        this.sourcePlace = sourcePlace;
    }

    public int getMiniOrderQuantity() {
        return miniOrderQuantity;
    }

    public void setMiniOrderQuantity(int miniOrderQuantity) {
        this.miniOrderQuantity = miniOrderQuantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }

    public double getpLong() {
        return pLong;
    }

    public void setpLong(double pLong) {
        this.pLong = pLong;
    }

    public double getpWide() {
        return pWide;
    }

    public void setpWide(double pWide) {
        this.pWide = pWide;
    }

    public double getpHigh() {
        return pHigh;
    }

    public void setpHigh(double pHigh) {
        this.pHigh = pHigh;
    }

    public String getSizeSpecification() {
        return sizeSpecification;
    }

    public void setSizeSpecification(String sizeSpecification) {
        this.sizeSpecification = sizeSpecification;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public double getPackingLong() {
        return packingLong;
    }

    public void setPackingLong(double packingLong) {
        this.packingLong = packingLong;
    }

    public double getPackingWide() {
        return packingWide;
    }

    public void setPackingWide(double packingWide) {
        this.packingWide = packingWide;
    }

    public double getPackingHigh() {
        return packingHigh;
    }

    public void setPackingHigh(double packingHigh) {
        this.packingHigh = packingHigh;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
    }

    public double getPackingVolume() {
        return packingVolume;
    }

    public void setPackingVolume(double packingVolume) {
        this.packingVolume = packingVolume;
    }

    public String getPackingVolUnit() {
        return packingVolUnit;
    }

    public void setPackingVolUnit(String packingVolUnit) {
        this.packingVolUnit = packingVolUnit;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getTextureSpecification() {
        return textureSpecification;
    }

    public void setTextureSpecification(String textureSpecification) {
        this.textureSpecification = textureSpecification;
    }

    public String getvProductCode() {
        return vProductCode;
    }

    public void setvProductCode(String vProductCode) {
        this.vProductCode = vProductCode;
    }

    public String getMaintainSpecification() {
        return maintainSpecification;
    }

    public void setMaintainSpecification(String maintainSpecification) {
        this.maintainSpecification = maintainSpecification;
    }

    public String getIsResin() {
        return isResin;
    }

    public void setIsResin(String isResin) {
        this.isResin = isResin;
    }

    public double getNetCapacity() {
        return netCapacity;
    }

    public void setNetCapacity(double netCapacity) {
        this.netCapacity = netCapacity;
    }

    public String getNetCapacityUnit() {
        return netCapacityUnit;
    }

    public void setNetCapacityUnit(String netCapacityUnit) {
        this.netCapacityUnit = netCapacityUnit;
    }

    public String getQgp() {
        return qgp;
    }

    public void setQgp(String qgp) {
        this.qgp = qgp;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getDefaultDelivery() {
        return defaultDelivery;
    }

    public void setDefaultDelivery(String defaultDelivery) {
        this.defaultDelivery = defaultDelivery;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getCustomSupportType() {
        return customSupportType;
    }

    public void setCustomSupportType(String customSupportType) {
        this.customSupportType = customSupportType;
    }
}