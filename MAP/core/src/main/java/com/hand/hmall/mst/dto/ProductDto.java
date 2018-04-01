package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送至商城的商品对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_PRODUCT")
public class ProductDto {

    private Long productId;

    /**
     * 是否可定制
     * 1  可定制
     * 0  不可定制
     */
    private String hascustom;

    private String code;

    private String name;

    private Long catalogversionId;

    private String approvalStatus;

    private String unit;

    private Long imageUrlId;

    private Long thumbnilUrlId;

    private Long baseProductId;

    private Long variantProductId;

    private String productType;

    private String customType;

    private String isSuit;

    private String isSinSale;

    private String saleStatus;

    private String brandId;

    private String productStatus;

    private Date listingTime;

    private Date delistingTime;

    private String isSpeSale;

    private String originCountry;

    private String sourcePlace;

    private BigDecimal miniOrderQuantity;

    private BigDecimal rate;

    private String style;

    private String designType;

    private BigDecimal pLong;

    private BigDecimal pWide;

    private BigDecimal pHigh;

    private String sizeSpecification;

    private String sizeUnit;

    private BigDecimal packingLong;

    private BigDecimal packingWide;

    private BigDecimal packingHigh;

    private String packingUnit;

    private BigDecimal packingVolume;

    private String packingVolUnit;

    private BigDecimal grossWeight;

    private BigDecimal netWeight;

    private String weightUnit;

    private String shape;

    private String texture;

    private String textureSpecification;

    private String maintainSpecification;

    private String isResin;

    private BigDecimal netCapacity;

    private String netCapacityUnit;

    private String qgp;

    private String syncflag;
    private Date creationDate;

    private String nameEn;

    private String vProductCode;

    private String platformCode;

    private String mtart;

    private String matkl;

    private String rewardProduct;

    private String defaultDelivery;

    private String isPreSale;

    private String mainColor;

    private String isPackaging;

    private String isStorage;

    private String sofaCombiningForm;

    private String isWashable;

    private String suitableUsers;

    private String designElement1;

    private String designElement2;

    private String designElement3;

    private String isSoftCushion;

    private String isWheel;

    private String drawerRailNum;

    private String isDamping;

    private String paintCraft;

    private String drawerNumber;

    private String backrestLength;

    private String isRotate;

    private String material;

    private String seriesName;

    private String introduction;

    private String salepoint;

    private String property;

    private String isMain;

    private String addFunction1;

    private String addFunction2;

    private String addFunction3;

    private String filler1;

    private String filler2;

    private String filler3;

    private String woodenCrafts1;

    private String woodenCrafts2;

    private String woodenCrafts3;

    private String leatherRange1;

    private String leatherRange2;

    private String leatherRange3;

    private String leatherCraft1;

    private String leatherCraft2;

    private String leatherCraft3;

    private String customSupportType;

    private String customChannelSource;

    /**
     * 基础关联关系
     */
    private List<Product> variantPrdLst;

    private String warehouse;

    private String escName;

    public String getEscName() {
        return escName;
    }

    public void setEscName(String escName) {
        this.escName = escName;
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

    public String getCustomChannelSource() {
        return customChannelSource;
    }

    public void setCustomChannelSource(String customChannelSource) {
        this.customChannelSource = customChannelSource;
    }

    public String getAddFunction1() {
        return addFunction1;
    }

    public void setAddFunction1(String addFunction1) {
        this.addFunction1 = addFunction1;
    }

    public String getAddFunction2() {
        return addFunction2;
    }

    public void setAddFunction2(String addFunction2) {
        this.addFunction2 = addFunction2;
    }

    public String getAddFunction3() {
        return addFunction3;
    }

    public void setAddFunction3(String addFunction3) {
        this.addFunction3 = addFunction3;
    }

    public String getFiller1() {
        return filler1;
    }

    public void setFiller1(String filler1) {
        this.filler1 = filler1;
    }

    public String getFiller2() {
        return filler2;
    }

    public void setFiller2(String filler2) {
        this.filler2 = filler2;
    }

    public String getFiller3() {
        return filler3;
    }

    public void setFiller3(String filler3) {
        this.filler3 = filler3;
    }

    public String getWoodenCrafts1() {
        return woodenCrafts1;
    }

    public void setWoodenCrafts1(String woodenCrafts1) {
        this.woodenCrafts1 = woodenCrafts1;
    }

    public String getWoodenCrafts2() {
        return woodenCrafts2;
    }

    public void setWoodenCrafts2(String woodenCrafts2) {
        this.woodenCrafts2 = woodenCrafts2;
    }

    public String getWoodenCrafts3() {
        return woodenCrafts3;
    }

    public void setWoodenCrafts3(String woodenCrafts3) {
        this.woodenCrafts3 = woodenCrafts3;
    }

    public String getLeatherRange1() {
        return leatherRange1;
    }

    public void setLeatherRange1(String leatherRange1) {
        this.leatherRange1 = leatherRange1;
    }

    public String getLeatherRange2() {
        return leatherRange2;
    }

    public void setLeatherRange2(String leatherRange2) {
        this.leatherRange2 = leatherRange2;
    }

    public String getLeatherRange3() {
        return leatherRange3;
    }

    public void setLeatherRange3(String leatherRange3) {
        this.leatherRange3 = leatherRange3;
    }

    public String getLeatherCraft1() {
        return leatherCraft1;
    }

    public void setLeatherCraft1(String leatherCraft1) {
        this.leatherCraft1 = leatherCraft1;
    }

    public String getLeatherCraft2() {
        return leatherCraft2;
    }

    public void setLeatherCraft2(String leatherCraft2) {
        this.leatherCraft2 = leatherCraft2;
    }

    public String getLeatherCraft3() {
        return leatherCraft3;
    }

    public void setLeatherCraft3(String leatherCraft3) {
        this.leatherCraft3 = leatherCraft3;
    }

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
    }

    public String getRewardProduct() {
        return rewardProduct;
    }

    public void setRewardProduct(String rewardProduct) {
        this.rewardProduct = rewardProduct;
    }

    public String getDefaultDelivery() {
        return defaultDelivery;
    }

    public void setDefaultDelivery(String defaultDelivery) {
        this.defaultDelivery = defaultDelivery;
    }

    public String getIsPreSale() {
        return isPreSale;
    }

    public void setIsPreSale(String isPreSale) {
        this.isPreSale = isPreSale;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getIsPackaging() {
        return isPackaging;
    }

    public void setIsPackaging(String isPackaging) {
        this.isPackaging = isPackaging;
    }

    public String getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(String isStorage) {
        this.isStorage = isStorage;
    }

    public String getSofaCombiningForm() {
        return sofaCombiningForm;
    }

    public void setSofaCombiningForm(String sofaCombiningForm) {
        this.sofaCombiningForm = sofaCombiningForm;
    }

    public String getIsWashable() {
        return isWashable;
    }

    public void setIsWashable(String isWashable) {
        this.isWashable = isWashable;
    }

    public String getSuitableUsers() {
        return suitableUsers;
    }

    public void setSuitableUsers(String suitableUsers) {
        this.suitableUsers = suitableUsers;
    }

    public String getDesignElement1() {
        return designElement1;
    }

    public void setDesignElement1(String designElement1) {
        this.designElement1 = designElement1;
    }

    public String getDesignElement2() {
        return designElement2;
    }

    public void setDesignElement2(String designElement2) {
        this.designElement2 = designElement2;
    }

    public String getDesignElement3() {
        return designElement3;
    }

    public void setDesignElement3(String designElement3) {
        this.designElement3 = designElement3;
    }

    public String getIsSoftCushion() {
        return isSoftCushion;
    }

    public void setIsSoftCushion(String isSoftCushion) {
        this.isSoftCushion = isSoftCushion;
    }

    public String getIsWheel() {
        return isWheel;
    }

    public void setIsWheel(String isWheel) {
        this.isWheel = isWheel;
    }

    public String getDrawerRailNum() {
        return drawerRailNum;
    }

    public void setDrawerRailNum(String drawerRailNum) {
        this.drawerRailNum = drawerRailNum;
    }

    public String getIsDamping() {
        return isDamping;
    }

    public void setIsDamping(String isDamping) {
        this.isDamping = isDamping;
    }

    public String getPaintCraft() {
        return paintCraft;
    }

    public void setPaintCraft(String paintCraft) {
        this.paintCraft = paintCraft;
    }

    public String getDrawerNumber() {
        return drawerNumber;
    }

    public void setDrawerNumber(String drawerNumber) {
        this.drawerNumber = drawerNumber;
    }

    public String getBackrestLength() {
        return backrestLength;
    }

    public void setBackrestLength(String backrestLength) {
        this.backrestLength = backrestLength;
    }

    public String getIsRotate() {
        return isRotate;
    }

    public void setIsRotate(String isRotate) {
        this.isRotate = isRotate;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSalepoint() {
        return salepoint;
    }

    public void setSalepoint(String salepoint) {
        this.salepoint = salepoint;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getvProductCode() {
        return vProductCode;
    }

    public void setvProductCode(String vProductCode) {
        this.vProductCode = vProductCode;
    }

    public String getIsSinSale() {
        return isSinSale;
    }

    public void setIsSinSale(String isSinSale) {
        this.isSinSale = isSinSale;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public BigDecimal getMiniOrderQuantity() {
        return miniOrderQuantity;
    }

    public void setMiniOrderQuantity(BigDecimal miniOrderQuantity) {
        this.miniOrderQuantity = miniOrderQuantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }

    public BigDecimal getpLong() {
        return pLong;
    }

    public void setpLong(BigDecimal pLong) {
        this.pLong = pLong;
    }

    public BigDecimal getpWide() {
        return pWide;
    }

    public void setpWide(BigDecimal pWide) {
        this.pWide = pWide;
    }

    public BigDecimal getpHigh() {
        return pHigh;
    }

    public void setpHigh(BigDecimal pHigh) {
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

    public BigDecimal getPackingLong() {
        return packingLong;
    }

    public void setPackingLong(BigDecimal packingLong) {
        this.packingLong = packingLong;
    }

    public BigDecimal getPackingWide() {
        return packingWide;
    }

    public void setPackingWide(BigDecimal packingWide) {
        this.packingWide = packingWide;
    }

    public BigDecimal getPackingHigh() {
        return packingHigh;
    }

    public void setPackingHigh(BigDecimal packingHigh) {
        this.packingHigh = packingHigh;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
    }

    public BigDecimal getPackingVolume() {
        return packingVolume;
    }

    public void setPackingVolume(BigDecimal packingVolume) {
        this.packingVolume = packingVolume;
    }

    public String getPackingVolUnit() {
        return packingVolUnit;
    }

    public void setPackingVolUnit(String packingVolUnit) {
        this.packingVolUnit = packingVolUnit;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
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

    public BigDecimal getNetCapacity() {
        return netCapacity;
    }

    public void setNetCapacity(BigDecimal netCapacity) {
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

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public List<Product> getVariantPrdLst() {
        return variantPrdLst;
    }

    public void setVariantPrdLst(List<Product> variantPrdLst) {
        this.variantPrdLst = variantPrdLst;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getHascustom() {
        return hascustom;
    }

    public void setHascustom(String hascustom) {
        this.hascustom = hascustom;
    }
}
