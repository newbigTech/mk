package com.hand.hmall.pojo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ProductData
 * @description 商品接收对象
 * @date 2017/6/2 13:09
 */
@XmlRootElement
@XmlType(propOrder = {"code", "customType", "name", "nameEn",
"unit", "productType", "serialCode", "isSuit",
"isSinSale", "brand", "saleStatus","productStatus", "listingTime", "deListingTime",
"isSpeSale", "originCountry", "sourcePlace", "minOrderQuantity", "stype", "designType",
"pLong", "pWide", "pHigh", "sizeSpecification", "sizeUnit", "packingLong", "packingWide",
"packingHigh", "packingUnit", "packingVolume", "packingVolUnit", "grossWeight", "netWeight",
        "weightUnit", "shape", "texture", "textureSpecification", "maintainSpecification", "isResin",
"netCapacity", "netCapacityUnit", "QGP", "vProductCode", "platformCode", "supplier", "suitLines", "patchLines"})
public class ProductData {


    /*
    *  商品编号
    * */
    private String code;

    /*
    * 系列编号
    * */
    private String serialCode;

    /*
    * 商品中文名称
    * */
    private String name;

    /*
    * 商品英文名称
    * */
    private String nameEn;

    /*
    * 单位
    * */
    private LovData unit;

    /*
    * 基础商品
    * */
    private String baseProduct;

    /*
    * 变体商品
    * */
    private String variantProduct;

    /*
    * 商品类型，传值
    * */
    private String productType;

    /*
    * 定制类型，传值
    * */
    private String customType;

    /*
    * 是否套件
    * */
    private String isSuit;

    /*
    * 是否单独销售
    * */
    private String isSinSale;

    /*
    * 品牌
    * */
    private String brand;

    /*
    * 销售状态
    * */
    private String saleStatus;

    /*
    * 产品状态
    * */
    private String productStatus;

    /*
    * 上市时间
    * */
    private String listingTime;

    /*
    * 退市时间
    * */
    private String deListingTime;

    /*
    * 是否特价销售
    * */
    private String isSpeSale;

    /*
    * 原产国
    * */
    private String originCountry;

    /*
    * 发货地
    * */
    private String sourcePlace;

    /*
    * 最小订单数量
    * */
    private Long minOrderQuantity;

    /*
    * 风格
    * */
    private String stype;

    /*
    * 设计类型
    * */
    private String designType;

    /*
    * 长
    * */
    private Double pLong;

    /*
    * 宽
    * */
    private Double pWide;

    /*
    * 高
    * */
    private Double pHigh;

    /*
    * 尺寸说明
    * */
    private String sizeSpecification;

    /*
    * 尺寸单位
    * */
    private LovData sizeUnit;

    /*
    * 包装长度
    * */
    private Double packingLong;

    /*
    * 包装宽度
    * */
    private Double packingWide;

    /*
    * 包装高度
    * */
    private Double packingHigh;

    /*
    * 包装单位
    * */
    private LovData packingUnit;

    /*
    * 包装体积
    * */
    private Double packingVolume;

    /*
    * 包装体积单位
    * */
    private LovData packingVolUnit;

    /*
    * 毛重
    * */
    private Double grossWeight;

    /*
    * 净重
    * */
    private Double netWeight;

    /*
    * 重量单位
    * */
    private LovData weightUnit;

    /*
    * 外形
    * */
    private String shape;

    /*
    * 材质
    * */
    private String texture;

    /*
    * 材质说明书
    * */
    private String textureSpecification;

    /*
    * 保养说明
    * */
    private String maintainSpecification;

    /*
    * 包含树脂
    * */
    private String isResin;

    /*
    * 净容量
    * */
    private Double netCapacity;

    /*
    * 净容量单位
    * */
    private LovData netCapacityUnit;

    /*
    * 保质期
    * */
    private String QGP;

    /*
    * 商品V码
    * */
    private String vProductCode;

    /*
    * 平台号
    * */
    private String platformCode;

    /*
    * 供应商
    * */
    private String supplier;

    /*
    * 套件关系
    * */
    private List<SuitLineData> suitLines;

    /*
    * 补件关系
    * */
    private List<PatchLineData> patchLines;

    @XmlElement(name = "code", required = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name = "serialCode", required = true)
    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public LovData getUnit() {
        return unit;
    }

    public void setUnit(LovData unit) {
        this.unit = unit;
    }

    @XmlTransient
    public String getBaseProduct() {
        return baseProduct;
    }

    public void setBaseProduct(String baseProduct) {
        this.baseProduct = baseProduct;
    }

    @XmlTransient
    public String getVariantProduct() {
        return variantProduct;
    }

    public void setVariantProduct(String variantProduct) {
        this.variantProduct = variantProduct;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @XmlElement(name = "customType", required = true)
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getListingTime() {
        return listingTime;
    }

    public void setListingTime(String listingTime) {
        this.listingTime = listingTime;
    }

    public String getDeListingTime() {
        return deListingTime;
    }

    public void setDeListingTime(String deListingTime) {
        this.deListingTime = deListingTime;
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

    public Long getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Long minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
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

    public Double getpLong() {
        return pLong;
    }

    public void setpLong(Double pLong) {
        this.pLong = pLong;
    }

    public Double getpWide() {
        return pWide;
    }

    public void setpWide(Double pWide) {
        this.pWide = pWide;
    }

    public Double getpHigh() {
        return pHigh;
    }

    public void setpHigh(Double pHigh) {
        this.pHigh = pHigh;
    }

    public String getSizeSpecification() {
        return sizeSpecification;
    }

    public void setSizeSpecification(String sizeSpecification) {
        this.sizeSpecification = sizeSpecification;
    }

    public LovData getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(LovData sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public Double getPackingLong() {
        return packingLong;
    }

    public void setPackingLong(Double packingLong) {
        this.packingLong = packingLong;
    }

    public Double getPackingWide() {
        return packingWide;
    }

    public void setPackingWide(Double packingWide) {
        this.packingWide = packingWide;
    }

    public Double getPackingHigh() {
        return packingHigh;
    }

    public void setPackingHigh(Double packingHigh) {
        this.packingHigh = packingHigh;
    }

    public LovData getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(LovData packingUnit) {
        this.packingUnit = packingUnit;
    }

    public Double getPackingVolume() {
        return packingVolume;
    }

    public void setPackingVolume(Double packingVolume) {
        this.packingVolume = packingVolume;
    }

    public LovData getPackingVolUnit() {
        return packingVolUnit;
    }

    public void setPackingVolUnit(LovData packingVolUnit) {
        this.packingVolUnit = packingVolUnit;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public LovData getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(LovData weightUnit) {
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

    public Double getNetCapacity() {
        return netCapacity;
    }

    public void setNetCapacity(Double netCapacity) {
        this.netCapacity = netCapacity;
    }

    public LovData getNetCapacityUnit() {
        return netCapacityUnit;
    }

    public void setNetCapacityUnit(LovData netCapacityUnit) {
        this.netCapacityUnit = netCapacityUnit;
    }

    public String getQGP() {
        return QGP;
    }

    public void setQGP(String QGP) {
        this.QGP = QGP;
    }

    public String getvProductCode() {
        return vProductCode;
    }

    public void setvProductCode(String vProductCode) {
        this.vProductCode = vProductCode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @XmlElementWrapper(name = "suitLines")
    @XmlElement(name = "suitLine")
    public List<SuitLineData> getSuitLines() {
        return suitLines;
    }

    public void setSuitLines(List<SuitLineData> suitLines) {
        this.suitLines = suitLines;
    }

    @XmlElementWrapper(name = "patchLines")
    @XmlElement(name = "patchLine")
    public List<PatchLineData> getPatchLines() {
        return patchLines;
    }


    public void setPatchLines(List<PatchLineData> patchLines) {
        this.patchLines = patchLines;
    }
}
