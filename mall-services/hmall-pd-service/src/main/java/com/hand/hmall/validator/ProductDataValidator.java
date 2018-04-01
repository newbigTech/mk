package com.hand.hmall.validator;

import com.hand.hmall.common.Constants;
import com.hand.hmall.model.Brand;
import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.LovData;
import com.hand.hmall.pojo.PatchLineData;
import com.hand.hmall.pojo.ProductData;
import com.hand.hmall.pojo.SuitLineData;
import com.hand.hmall.service.IBrandService;
import com.hand.hmall.service.IEnumerationService;
import com.hand.hmall.service.IProductService;
import com.hand.hmall.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 马君
 * @version 0.1
 * @name ProductReceiveDataValidator
 * @description 对ProductReceiveData进行校验
 * @date 2017/6/5 19:51
 */

@Component
public class ProductDataValidator implements Validator<ProductData> {


    @Autowired
    private IBrandService iBrandService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IEnumerationService iEnumerationService;

    @Override
    public ValidateResult validate(ProductData productData) {
        ValidateResult result = new ValidateResult();
        result.setValid(false);

        // 商品编码
        if (StringUtils.isBlank(productData.getCode())) {
            result.setMessage("商品编码不能为空");
            return result;
        }

        // 定制类型
        if (StringUtils.isBlank(productData.getCustomType())) {
            result.setMessage("定制类型不能为空");
            return result;
        }


        // 商品单位
        LovData unit = productData.getUnit();
        if (unit != null) {
            if (StringUtils.isBlank(unit.getCode())) {
                result.setMessage("单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(unit.getName())) {
                result.setMessage("单位名称不能为空");
                return result;
            }
        }

        // 品牌
        String brandCode = productData.getBrand();
        if (StringUtils.isNotBlank(brandCode)) {
            Brand brand = iBrandService.selectByCode(brandCode);
            if (brand == null) {
                result.setMessage("品牌" + brandCode + "不存在");
                return result;
            }
        }

        // 商品类型
        String productTypeCode = productData.getProductType();
        if (StringUtils.isNotBlank(productTypeCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_PRODUCT_TYPE, productTypeCode)) {
                result.setMessage("商品类型" + productTypeCode + "不存在");
                return result;
            }
        }

        // 定制类型
        String customTypeCode = productData.getCustomType();
        if (StringUtils.isNotBlank(customTypeCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_CUSTOM_TYPE, customTypeCode)) {
                result.setMessage("定制类型" + customTypeCode + "不存在");
                return result;
            }
        }

        if (StringUtils.isNotBlank(productData.getIsSuit())) {
            if (!Arrays.asList("00", "12").contains(productData.getIsSuit())) {
                result.setMessage("是否套件" + productData.getIsSuit() + "非法(00, 12)");
                return result;
            }
        }

        // 销售状态
        String saleStatusCode = productData.getSaleStatus();
        if (StringUtils.isNotBlank(saleStatusCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_SALE_STATUS, saleStatusCode)) {
                result.setMessage("销售状态" + saleStatusCode + "不存在");
                return result;
            }
        }

        // 商品状态
        String productStatusCode = productData.getProductStatus();
        if (StringUtils.isNotBlank(productStatusCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_PRODUCT_STATUS, productStatusCode)) {
                result.setMessage("产品状态" + productStatusCode + "不存在");
                return result;
            }
        }

        // 时间类型
        String listingTime = productData.getListingTime();
        if (StringUtils.isNotBlank(listingTime)) {
            if (DateUtil.parse(listingTime) == null) {
                result.setMessage("上市时间类型非法（yyyy-MM-dd HH:mm:ss）");
                return result;
            }
        }

        String deListingTime = productData.getDeListingTime();
        if (StringUtils.isNotBlank(deListingTime)) {
            if (DateUtil.parse(deListingTime) == null) {
                result.setMessage("下市时间类型非法（yyyy-MM-dd HH:mm:ss）");
                return result;
            }
        }

        // 风格
        String stypeCode = productData.getStype();
        if (StringUtils.isNotBlank(stypeCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_STYPE, stypeCode)) {
                result.setMessage("风格" + stypeCode + "不存在");
                return result;
            }
        }

        // 设计类型
        String designTypeCode = productData.getDesignType();
        if (StringUtils.isNotBlank(designTypeCode)) {
            if (!iEnumerationService.checkEnumerationValue(Constants.PRODUCT_DESIGN_TYPE, designTypeCode)) {
                result.setMessage("设计类型" + designTypeCode + "不存在");
                return result;
            }
        }

        // 包含树脂
        if (StringUtils.isNotBlank(productData.getIsResin())) {
            if (!Arrays.asList("Y", "N").contains(productData.getIsResin())) {
                result.setMessage("是否包含树脂" + productData.getIsResin() + "非法(Y, N)");
                return result;
            }
        }

        // 单独销售
        if (StringUtils.isNotBlank(productData.getIsSinSale())) {
            if (!Arrays.asList("Y", "N").contains(productData.getIsSinSale())) {
                result.setMessage("是否单独销售" + productData.getIsSinSale() + "非法(Y, N)");
                return result;
            }
        }

        // 是否特卖
        if (StringUtils.isNotBlank(productData.getIsSpeSale())) {
            if (!Arrays.asList("Y", "N").contains(productData.getIsSpeSale())) {
                result.setMessage("是否特卖" + productData.getIsSpeSale() + "非法(Y, N)");
                return result;
            }
        }

        // 套件关系
        if (CollectionUtils.isNotEmpty(productData.getSuitLines())) {
            for (SuitLineData suitLineData : productData.getSuitLines()) {
                if (StringUtils.isBlank(suitLineData.getComponentCode())) {
                    result.setMessage("套件商品编号不能为空");
                    return result;
                } else {
                    Product suitProduct = iProductService.selectMasterStagedByCode(suitLineData.getComponentCode());
                    if (suitProduct == null) {
                        result.setMessage("套件商品" + suitLineData.getComponentCode() + "不存在");
                        return result;
                    }
                }
                if (suitLineData.getQuantity() == null) {
                    result.setMessage("套件数量不能为空");
                    return result;
                }
            }
        }

        // 补件关系
        if (CollectionUtils.isNotEmpty(productData.getPatchLines())) {
            for (PatchLineData patchLineData : productData.getPatchLines()) {
                if (StringUtils.isNotBlank(patchLineData.getPatchLineCode())) {
                    Product patchProduct = iProductService.selectMasterStagedByCode(patchLineData.getPatchLineCode());
                    if (patchProduct == null) {
                        result.setMessage("补件商品" + patchLineData.getPatchLineCode() + "不存在");
                        return result;
                    }
                } else {
                    result.setMessage("补件商品编号不能为空");
                    return result;
                }
            }
        }

        // 尺寸单位
        LovData sizeUnit = productData.getSizeUnit();
        if (sizeUnit != null) {
            if (StringUtils.isBlank(sizeUnit.getCode())) {
                result.setMessage("尺寸单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(sizeUnit.getName())) {
                result.setMessage("尺寸单位名称不能为空");
                return result;
            }
        }

        // 包装单位
        LovData packingUnit = productData.getPackingUnit();
        if (packingUnit != null) {
            if (StringUtils.isBlank(packingUnit.getCode())) {
                result.setMessage("包装单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(packingUnit.getName())) {
                result.setMessage("包装单位名称不能为空");
                return result;
            }
        }

        // 包装体积单位
        LovData packingVolUnit = productData.getPackingVolUnit();
        if (packingVolUnit != null) {
            if (StringUtils.isBlank(packingVolUnit.getCode())) {
                result.setMessage("包装体积单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(packingVolUnit.getName())) {
                result.setMessage("包装体积单位名称不能为空");
                return result;
            }
        }

        // 重量单位
        LovData weightUnit = productData.getWeightUnit();
        if (weightUnit != null) {
            if (StringUtils.isBlank(weightUnit.getCode())) {
                result.setMessage("重量单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(weightUnit.getName())) {
                result.setMessage("重量单位名称不能为空");
                return result;
            }
        }

        // 净重单位
        LovData netCapacityUnit = productData.getNetCapacityUnit();
        if (netCapacityUnit != null) {
            if (StringUtils.isBlank(netCapacityUnit.getCode())) {
                result.setMessage("净容量单位编码不能为空");
                return result;
            }

            if (StringUtils.isBlank(netCapacityUnit.getName())) {
                result.setMessage("净容量单位名称不能为空");
                return result;
            }
        }

        result.setMessage("验证通过");
        result.setValid(true);
        return result;
    }
}
