package com.hand.hmall.convertor;

import com.hand.hmall.common.Constants;
import com.hand.hmall.model.Brand;
import com.hand.hmall.model.Catalogversion;
import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.LovData;
import com.hand.hmall.pojo.ProductData;
import com.hand.hmall.service.IBrandService;
import com.hand.hmall.service.ICatalogversionService;
import com.hand.hmall.service.IEnumerationService;
import com.hand.hmall.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 马君
 * @version 0.1
 * @name ProductConvertor
 * @description ProductReceiveData到Product的转换器 因为该Convertor中存在数据库操作，所以放到统一事务中处理
 * @date 2017/6/3 11:24
 */

@Component
public class ProductConvertor implements Convertor<ProductData, Product> {

    private static final Logger logger = LoggerFactory.getLogger(ProductConvertor.class);
    private static final String IS_SUIT = "12";
    private static final String IS_NOT_SUIT = "00";

    @Autowired
    private IBrandService iBrandService;

    @Autowired
    private ICatalogversionService iCatalogversionService;

    @Autowired
    private IEnumerationService iEnumerationService;

    @Override
    public Product convert(ProductData productData) {
        return convert(productData, new Product());
    }

    @Override
    public Product convert(ProductData productData, Product product) {

        // 商品编码
        product.setCode(productData.getCode());
        // 商品中文名称
        product.setName(productData.getName());
        // 商品英文名称
        product.setNameEn(productData.getNameEn());

        // 审核状态
        product.setApprovalStatus(Constants.APPROVAL_STATUS_CHECKED);

        // 设置默认的目录版本master
        Catalogversion masterStaged = iCatalogversionService.selectMasterStaged();
        product.setCatalogversionId(masterStaged.getCatalogversionId());
        product.getCatalogversionId();

        // 单位
        LovData unit = productData.getUnit();
        if (unit != null) {
            product.setUnit(unit.getCode());
            iEnumerationService.addLovIfNotExists(unit, Constants.UNIT);
        }

        // 商品类型
        product.setProductType(productData.getProductType());
        // 定制类型
        product.setCustomType(productData.getCustomType());
        // 是否单品
        product.setIsSuit(IS_NOT_SUIT.equals(productData.getIsSuit())
                ? Constants.NO : IS_SUIT.equals(productData.getIsSuit()) ? Constants.YES : Constants.NO);
        // 是否单独销售
        product.setIsSinSale(productData.getIsSinSale());

        // 品牌
        String brandCode = productData.getBrand();
        if (StringUtils.isNotBlank(brandCode)) {
            Brand brand = iBrandService.selectByCode(brandCode);
            if (brand != null) {
                product.setBrandId(brand.getBrandId());
            }
        }

        // 销售状态
        product.setSaleStatus(productData.getSaleStatus());
        // 商品状态
        product.setProductStatus(productData.getProductStatus());
        // 上市时间
        product.setListingTime(DateUtil.parse(productData.getListingTime()));
        // 下市时间
        product.setDelistingTime(DateUtil.parse(productData.getDeListingTime()));
        // 是否特价销售
        product.setIsSpeSale(productData.getIsSpeSale());
        // 原产国
        product.setOriginCountry(productData.getOriginCountry());
        // 货源地
        product.setSourcePlace(productData.getSourcePlace());
        // 最小订单数量
        product.setMiniOrderQuantity(productData.getMinOrderQuantity());
        // 风格
        product.setStype(productData.getStype());
        // 新品设计类型
        product.setDesignType(productData.getDesignType());
        // 长宽高
        product.setpLong(productData.getpLong());
        product.setpWide(productData.getpWide());
        product.setpHigh(productData.getpHigh());
        // 尺寸说明书
        product.setSizeSpecification(productData.getSizeSpecification());
        // 尺寸单位
        LovData sizeUnit = productData.getSizeUnit();
        if (sizeUnit != null) {
            product.setSizeUnit(sizeUnit.getCode());
            iEnumerationService.addLovIfNotExists(sizeUnit, Constants.UNIT);
        }

        // 包装长宽高
        product.setPackingLong(productData.getPackingLong());
        product.setPackingWide(productData.getpWide());
        product.setPackingHigh(productData.getPackingHigh());


        // 包装单位
        LovData packingUnit = productData.getPackingUnit();
        if (packingUnit != null) {
            product.setPackingUnit(packingUnit.getCode());
            iEnumerationService.addLovIfNotExists(packingUnit, Constants.UNIT);
        }

        // 包装体积
        product.setPackingVolume(productData.getPackingVolume());
        // 包装体积单位
        LovData packingVolUnit = productData.getPackingVolUnit();
        if (packingVolUnit != null) {
            product.setPackingVolUnit(packingVolUnit.getCode());
            iEnumerationService.addLovIfNotExists(packingVolUnit, Constants.UNIT);
        }

        // 毛重
        product.setGrossWeight(productData.getGrossWeight());
        // 净重
        product.setNetWeight(productData.getNetWeight());

        // 重量单位
        LovData weightUnit = productData.getWeightUnit();
        if (weightUnit != null) {
            product.setWeightUnit(weightUnit.getCode());
            iEnumerationService.addLovIfNotExists(weightUnit, Constants.UNIT);
        }

        // 外形
        product.setShape(productData.getShape());
        // 材质
        product.setTexture(productData.getTexture());
        // 材质说明书
        product.setTextureSpecification(productData.getTextureSpecification());
        // 保养说明
        product.setMaintainSpecification(productData.getMaintainSpecification());

        // 是否包含树脂
        product.setIsResin(productData.getIsResin());
        // 净容量
        product.setNetCapacity(productData.getNetCapacity());


        // 净容量单位
        LovData netCapacityUnit = productData.getNetCapacityUnit();
        if (netCapacityUnit != null) {
            product.setNetCapacityUnit(netCapacityUnit.getCode());
            iEnumerationService.addLovIfNotExists(netCapacityUnit, Constants.UNIT);
        }

        // 保质期
        product.setQgp(productData.getQGP());

        // 商品V码
        product.setvProductCode(productData.getvProductCode());

        // 平台号
        product.setPlatformCode(productData.getPlatformCode());

        // 供应商
        product.setSupplier(productData.getSupplier());

        return product;
    }
}
