package com.hand.hmall.model;

import java.util.Date;

public class HmallMstProduct {
    /**
     * 主键
     */
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
    private Long miniOrderQuantity;

    /**
     * 转换单位转换率
     */
    private Double rate;

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
    private Double pLong;

    /**
     * 宽
     */
    private Double pWide;

    /**
     * 高
     */
    private Double pHigh;

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
    private Double packingLong;

    /**
     * 包装宽度
     */
    private Double packingWide;

    /**
     * 包装高度
     */
    private Double packingHigh;

    /**
     * 包装单位
     */
    private String packingUnit;

    /**
     * 包装体积
     */
    private Double packingVolume;

    /**
     * 包装体积单位
     */
    private String packingVolUnit;

    /**
     * 毛重
     */
    private Double grossWeight;

    /**
     * 净重
     */
    private Double netWeight;

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
    private Double netCapacity;

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
     * 英文名称
     */
    private String nameEn;

    /**
     * 主键
     *
     * @return PRODUCT_ID 主键
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 主键
     *
     * @param productId 主键
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 商品编码
     *
     * @return CODE 商品编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 商品编码
     *
     * @param code 商品编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 商品名称
     *
     * @return NAME 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 版本目录
     *
     * @return CATALOGVERSION_ID 版本目录
     */
    public Long getCatalogversionId() {
        return catalogversionId;
    }

    /**
     * 版本目录
     *
     * @param catalogversionId 版本目录
     */
    public void setCatalogversionId(Long catalogversionId) {
        this.catalogversionId = catalogversionId;
    }

    /**
     * 上下架状态
     *
     * @return APPROVAL_STATUS 上下架状态
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * 上下架状态
     *
     * @param approvalStatus 上下架状态
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus == null ? null : approvalStatus.trim();
    }

    /**
     * 单位
     *
     * @return UNIT 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 主图链接
     *
     * @return IMAGE_URL_ID 主图链接
     */
    public Long getImageUrlId() {
        return imageUrlId;
    }

    /**
     * 主图链接
     *
     * @param imageUrlId 主图链接
     */
    public void setImageUrlId(Long imageUrlId) {
        this.imageUrlId = imageUrlId;
    }

    /**
     * 缩略图链接
     *
     * @return THUMBNIL_URL_ID 缩略图链接
     */
    public Long getThumbnilUrlId() {
        return thumbnilUrlId;
    }

    /**
     * 缩略图链接
     *
     * @param thumbnilUrlId 缩略图链接
     */
    public void setThumbnilUrlId(Long thumbnilUrlId) {
        this.thumbnilUrlId = thumbnilUrlId;
    }

    /**
     * 基础商品
     *
     * @return BASE_PRODUCT_ID 基础商品
     */
    public Long getBaseProductId() {
        return baseProductId;
    }

    /**
     * 基础商品
     *
     * @param baseProductId 基础商品
     */
    public void setBaseProductId(Long baseProductId) {
        this.baseProductId = baseProductId;
    }

    /**
     * 变体商品
     *
     * @return VARIANT_PRODUCT_ID 变体商品
     */
    public Long getVariantProductId() {
        return variantProductId;
    }

    /**
     * 变体商品
     *
     * @param variantProductId 变体商品
     */
    public void setVariantProductId(Long variantProductId) {
        this.variantProductId = variantProductId;
    }

    /**
     * 商品类型
     *
     * @return PRODUCT_TYPE 商品类型
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 商品类型
     *
     * @param productType 商品类型
     */
    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    /**
     * 商品定制类型
     *
     * @return CUSTOM_TYPE 商品定制类型
     */
    public String getCustomType() {
        return customType;
    }

    /**
     * 商品定制类型
     *
     * @param customType 商品定制类型
     */
    public void setCustomType(String customType) {
        this.customType = customType == null ? null : customType.trim();
    }

    /**
     * 是否套件
     *
     * @return IS_SUIT 是否套件
     */
    public String getIsSuit() {
        return isSuit;
    }

    /**
     * 是否套件
     *
     * @param isSuit 是否套件
     */
    public void setIsSuit(String isSuit) {
        this.isSuit = isSuit == null ? null : isSuit.trim();
    }

    /**
     * 是否单独销售
     *
     * @return IS_SIN_SALE 是否单独销售
     */
    public String getIsSinSale() {
        return isSinSale;
    }

    /**
     * 是否单独销售
     *
     * @param isSinSale 是否单独销售
     */
    public void setIsSinSale(String isSinSale) {
        this.isSinSale = isSinSale == null ? null : isSinSale.trim();
    }

    /**
     * 商品品牌
     *
     * @return BRAND_ID 商品品牌
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * 商品品牌
     *
     * @param brandId 商品品牌
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * 是否可销售
     *
     * @return SALE_STATUS 是否可销售
     */
    public String getSaleStatus() {
        return saleStatus;
    }

    /**
     * 是否可销售
     *
     * @param saleStatus 是否可销售
     */
    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus == null ? null : saleStatus.trim();
    }

    /**
     * 产品状态
     *
     * @return PRODUCT_STATUS 产品状态
     */
    public String getProductStatus() {
        return productStatus;
    }

    /**
     * 产品状态
     *
     * @param productStatus 产品状态
     */
    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus == null ? null : productStatus.trim();
    }

    /**
     * 上市时间
     *
     * @return LISTING_TIME 上市时间
     */
    public Date getListingTime() {
        return listingTime;
    }

    /**
     * 上市时间
     *
     * @param listingTime 上市时间
     */
    public void setListingTime(Date listingTime) {
        this.listingTime = listingTime;
    }

    /**
     * 退市时间
     *
     * @return DELISTING_TIME 退市时间
     */
    public Date getDelistingTime() {
        return delistingTime;
    }

    /**
     * 退市时间
     *
     * @param delistingTime 退市时间
     */
    public void setDelistingTime(Date delistingTime) {
        this.delistingTime = delistingTime;
    }

    /**
     * 特卖标记
     *
     * @return IS_SPE_SALE 特卖标记
     */
    public String getIsSpeSale() {
        return isSpeSale;
    }

    /**
     * 特卖标记
     *
     * @param isSpeSale 特卖标记
     */
    public void setIsSpeSale(String isSpeSale) {
        this.isSpeSale = isSpeSale == null ? null : isSpeSale.trim();
    }

    /**
     * 产国
     *
     * @return ORIGIN_COUNTRY 产国
     */
    public String getOriginCountry() {
        return originCountry;
    }

    /**
     * 产国
     *
     * @param originCountry 产国
     */
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry == null ? null : originCountry.trim();
    }

    /**
     * 源地
     *
     * @return SOURCE_PLACE 源地
     */
    public String getSourcePlace() {
        return sourcePlace;
    }

    /**
     * 源地
     *
     * @param sourcePlace 源地
     */
    public void setSourcePlace(String sourcePlace) {
        this.sourcePlace = sourcePlace == null ? null : sourcePlace.trim();
    }

    /**
     * 最小订单数量
     *
     * @return MINI_ORDER_QUANTITY 最小订单数量
     */
    public Long getMiniOrderQuantity() {
        return miniOrderQuantity;
    }

    /**
     * 最小订单数量
     *
     * @param miniOrderQuantity 最小订单数量
     */
    public void setMiniOrderQuantity(Long miniOrderQuantity) {
        this.miniOrderQuantity = miniOrderQuantity;
    }

    /**
     * 转换单位转换率
     *
     * @return RATE 转换单位转换率
     */
    public Double getRate() {
        return rate;
    }

    /**
     * 转换单位转换率
     *
     * @param rate 转换单位转换率
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * 风格
     *
     * @return STYPE 风格
     */
    public String getStype() {
        return stype;
    }

    /**
     * 风格
     *
     * @param stype 风格
     */
    public void setStype(String stype) {
        this.stype = stype == null ? null : stype.trim();
    }

    /**
     * 新产品设计类型
     *
     * @return DESIGN_TYPE 新产品设计类型
     */
    public String getDesignType() {
        return designType;
    }

    /**
     * 新产品设计类型
     *
     * @param designType 新产品设计类型
     */
    public void setDesignType(String designType) {
        this.designType = designType == null ? null : designType.trim();
    }

    /**
     * 长
     *
     * @return P_LONG 长
     */
    public Double getpLong() {
        return pLong;
    }

    /**
     * 长
     *
     * @param pLong 长
     */
    public void setpLong(Double pLong) {
        this.pLong = pLong;
    }

    /**
     * 宽
     *
     * @return P_WIDE 宽
     */
    public Double getpWide() {
        return pWide;
    }

    /**
     * 宽
     *
     * @param pWide 宽
     */
    public void setpWide(Double pWide) {
        this.pWide = pWide;
    }

    /**
     * 高
     *
     * @return P_HIGH 高
     */
    public Double getpHigh() {
        return pHigh;
    }

    /**
     * 高
     *
     * @param pHigh 高
     */
    public void setpHigh(Double pHigh) {
        this.pHigh = pHigh;
    }

    /**
     * 尺寸说明
     *
     * @return SIZE_SPECIFICATION 尺寸说明
     */
    public String getSizeSpecification() {
        return sizeSpecification;
    }

    /**
     * 尺寸说明
     *
     * @param sizeSpecification 尺寸说明
     */
    public void setSizeSpecification(String sizeSpecification) {
        this.sizeSpecification = sizeSpecification == null ? null : sizeSpecification.trim();
    }

    /**
     * 尺寸单位
     *
     * @return SIZE_UNIT 尺寸单位
     */
    public String getSizeUnit() {
        return sizeUnit;
    }

    /**
     * 尺寸单位
     *
     * @param sizeUnit 尺寸单位
     */
    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit == null ? null : sizeUnit.trim();
    }

    /**
     * 包装长度
     *
     * @return PACKING_LONG 包装长度
     */
    public Double getPackingLong() {
        return packingLong;
    }

    /**
     * 包装长度
     *
     * @param packingLong 包装长度
     */
    public void setPackingLong(Double packingLong) {
        this.packingLong = packingLong;
    }

    /**
     * 包装宽度
     *
     * @return PACKING_WIDE 包装宽度
     */
    public Double getPackingWide() {
        return packingWide;
    }

    /**
     * 包装宽度
     *
     * @param packingWide 包装宽度
     */
    public void setPackingWide(Double packingWide) {
        this.packingWide = packingWide;
    }

    /**
     * 包装高度
     *
     * @return PACKING_HIGH 包装高度
     */
    public Double getPackingHigh() {
        return packingHigh;
    }

    /**
     * 包装高度
     *
     * @param packingHigh 包装高度
     */
    public void setPackingHigh(Double packingHigh) {
        this.packingHigh = packingHigh;
    }

    /**
     * 包装单位
     *
     * @return PACKING_UNIT 包装单位
     */
    public String getPackingUnit() {
        return packingUnit;
    }

    /**
     * 包装单位
     *
     * @param packingUnit 包装单位
     */
    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit == null ? null : packingUnit.trim();
    }

    /**
     * 包装体积
     *
     * @return PACKING_VOLUME 包装体积
     */
    public Double getPackingVolume() {
        return packingVolume;
    }

    /**
     * 包装体积
     *
     * @param packingVolume 包装体积
     */
    public void setPackingVolume(Double packingVolume) {
        this.packingVolume = packingVolume;
    }

    /**
     * 包装体积单位
     *
     * @return PACKING_VOL_UNIT 包装体积单位
     */
    public String getPackingVolUnit() {
        return packingVolUnit;
    }

    /**
     * 包装体积单位
     *
     * @param packingVolUnit 包装体积单位
     */
    public void setPackingVolUnit(String packingVolUnit) {
        this.packingVolUnit = packingVolUnit == null ? null : packingVolUnit.trim();
    }

    /**
     * 毛重
     *
     * @return GROSS_WEIGHT 毛重
     */
    public Double getGrossWeight() {
        return grossWeight;
    }

    /**
     * 毛重
     *
     * @param grossWeight 毛重
     */
    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * 净重
     *
     * @return NET_WEIGHT 净重
     */
    public Double getNetWeight() {
        return netWeight;
    }

    /**
     * 净重
     *
     * @param netWeight 净重
     */
    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    /**
     * 重量单位
     *
     * @return WEIGHT_UNIT 重量单位
     */
    public String getWeightUnit() {
        return weightUnit;
    }

    /**
     * 重量单位
     *
     * @param weightUnit 重量单位
     */
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit == null ? null : weightUnit.trim();
    }

    /**
     * 外形
     *
     * @return SHAPE 外形
     */
    public String getShape() {
        return shape;
    }

    /**
     * 外形
     *
     * @param shape 外形
     */
    public void setShape(String shape) {
        this.shape = shape == null ? null : shape.trim();
    }

    /**
     * 材质（价签）
     *
     * @return TEXTURE 材质（价签）
     */
    public String getTexture() {
        return texture;
    }

    /**
     * 材质（价签）
     *
     * @param texture 材质（价签）
     */
    public void setTexture(String texture) {
        this.texture = texture == null ? null : texture.trim();
    }

    /**
     * 材质（说明书）
     *
     * @return TEXTURE_SPECIFICATION 材质（说明书）
     */
    public String getTextureSpecification() {
        return textureSpecification;
    }

    /**
     * 材质（说明书）
     *
     * @param textureSpecification 材质（说明书）
     */
    public void setTextureSpecification(String textureSpecification) {
        this.textureSpecification = textureSpecification == null ? null : textureSpecification.trim();
    }

    /**
     * 保养说明
     *
     * @return MAINTAIN_SPECIFICATION 保养说明
     */
    public String getMaintainSpecification() {
        return maintainSpecification;
    }

    /**
     * 保养说明
     *
     * @param maintainSpecification 保养说明
     */
    public void setMaintainSpecification(String maintainSpecification) {
        this.maintainSpecification = maintainSpecification == null ? null : maintainSpecification.trim();
    }

    /**
     * 包含树脂
     *
     * @return IS_RESIN 包含树脂
     */
    public String getIsResin() {
        return isResin;
    }

    /**
     * 包含树脂
     *
     * @param isResin 包含树脂
     */
    public void setIsResin(String isResin) {
        this.isResin = isResin == null ? null : isResin.trim();
    }

    /**
     * 净容量
     *
     * @return NET_CAPACITY 净容量
     */
    public Double getNetCapacity() {
        return netCapacity;
    }

    /**
     * 净容量
     *
     * @param netCapacity 净容量
     */
    public void setNetCapacity(Double netCapacity) {
        this.netCapacity = netCapacity;
    }

    /**
     * 净容量单位
     *
     * @return NET_CAPACITY_UNIT 净容量单位
     */
    public String getNetCapacityUnit() {
        return netCapacityUnit;
    }

    /**
     * 净容量单位
     *
     * @param netCapacityUnit 净容量单位
     */
    public void setNetCapacityUnit(String netCapacityUnit) {
        this.netCapacityUnit = netCapacityUnit == null ? null : netCapacityUnit.trim();
    }

    /**
     * 保质期
     *
     * @return QGP 保质期
     */
    public String getQgp() {
        return qgp;
    }

    /**
     * 保质期
     *
     * @param qgp 保质期
     */
    public void setQgp(String qgp) {
        this.qgp = qgp == null ? null : qgp.trim();
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

    /**
     * 英文名称
     *
     * @return NAME_EN 英文名称
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * 英文名称
     *
     * @param nameEn 英文名称
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }
}