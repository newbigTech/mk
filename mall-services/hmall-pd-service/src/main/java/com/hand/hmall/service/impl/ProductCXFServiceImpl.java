package com.hand.hmall.service.impl;

import com.hand.hmall.common.Constants;
import com.hand.hmall.common.ResponseType;
import com.hand.hmall.convertor.ProductConvertor;
import com.hand.hmall.model.Odtype;
import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.ErrorLine;
import com.hand.hmall.pojo.PdResponse;
import com.hand.hmall.pojo.ProductData;
import com.hand.hmall.service.*;
import com.hand.hmall.validator.ProductDataValidator;
import com.hand.hmall.validator.ValidateResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ProductCXFServiceImpl
 * @description 商品SOAP接口实现
 * @date 2017/6/23 9:14
 */
@Service
public class ProductCXFServiceImpl implements IProductCXFService {

    private static final String REGULARLY = "A4"; // 常规品

    @Autowired
    private ProductDataValidator productDataValidator;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private ProductConvertor productConvertor;
    @Autowired
    private ISuitLineMappingService iSuitLineMappingService;
    @Autowired
    private IPatchLineMappingService iPatchLineMappingService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IOdtypeService iOdtypeService;
    @Autowired
    private INotificationService iNotificationService;

    /**
     * {@inheritDoc}
     *
     * @see IProductCXFService#reveiveProductsFromRetail
     */
    @Override
    public PdResponse reveiveProductsFromRetail(List<ProductData> productDatas) {
        if (CollectionUtils.isEmpty(productDatas)) {
            return new PdResponse(false, ResponseType.INVALID_PARAMETER.CODE, "商品数据为空");
        }
        List<ErrorLine> errorLines = new ArrayList<>();

        for (ProductData productData : productDatas) {
            if (!validate(productData, errorLines)) {
                continue;
            }
            String productCode = productData.getCode();
            String platformCode = productData.getPlatformCode();

            try {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                        // 将ProductData转化成Product对象
                        Product product = productConvertor.convert(productData);

                        if (REGULARLY.equals(productData.getCustomType())) {
                            // 常规品存储，根据平台号查询基础商品，存在则关联
                            if (StringUtils.isNotBlank(platformCode)) {
                                Product basicProduct = iProductService.selectMasterStagedByCode(platformCode);
                                if (basicProduct != null) {
                                    product.setBaseProductId(basicProduct.getProductId());
                                }
                            }

                        } else {

                            // 订制品存储，根据商品号获取基础商品，如存在则关联，不存在将该商品的属性 + 商品号作为基础商品存储并关联

                            // 商品编码截掉最后一位尺寸获取商品号
                            String GNO = productCode.substring(0, productCode.length() - 1);

                            // 检查商品号是否存在，如果不存在则保存该商品号，并且作为基础商品关联
                            Product basicProduct = iProductService.selectMasterStagedByCode(GNO);
                            if (basicProduct == null) {
                                basicProduct = new Product();
                                BeanUtils.copyProperties(product, basicProduct);
                                basicProduct.setCode(GNO);
                                basicProduct.setPlatformCode(null);
                                basicProduct.setCustomSupportType(Constants.CUSTOM_SUPPORT_TYPE_GNO);
                                iProductService.insertSelective(basicProduct);

                                // 增加新增商品通知
                                iNotificationService.addProductNewNotice(basicProduct);
                            }
                            product.setBaseProductId(basicProduct.getProductId());

                        }

                        Product checkProduct = iProductService.selectMasterStagedByCode(product.getCode());

                        // 如果商品已经存在则更新否则新增
                        if (checkProduct == null) {
                            iProductService.insertSelective(product);
                            iNotificationService.addProductNewNotice(product);
                        } else {
                            product.setProductId(checkProduct.getProductId());
                            product.setLastUpdateDate(new Date());
                            iProductService.updateByPrimaryKeySelective(product);
                            // 添加商品名称变更通知（如果名称由变更）
                            iNotificationService.addProductChangeNotice(product, checkProduct);
                        }

                        // 如果商品为订制品保存定制频道信息，并对常规品进行关联
                        if (!REGULARLY.equals(product.getCustomType())) {
                            // 保存定制频道信息
                            saveOdtypes(product);
                            // 取出该定制品下面的所有常规品进行关联（结合常规品存储理解）
                            List<Product> regularProList = iProductService.selectMasterStagedByPlatformCode(product.getCode());
                            if (CollectionUtils.isNotEmpty(regularProList)) {
                                for (Product regular : regularProList) {
                                    regular.setBaseProductId(product.getProductId());
                                    iProductService.updateByPrimaryKeySelective(regular);
                                }
                            }
                        }

                        // 保存套件关系
                        iSuitLineMappingService.saveSuitLines(productData.getSuitLines(), product.getProductId());

                        // 保存补件关系
                        iPatchLineMappingService.savePatchLines(productData.getPatchLines(), product.getProductId());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLine errorLine = new ErrorLine(productCode, "数据存储异常");
                errorLines.add(errorLine);
            }
        }

        if (errorLines.size() == 0) {
            return new PdResponse(true, ResponseType.SUCCESS.CODE, ResponseType.SUCCESS.MESSAGE);
        } else if (errorLines.size() < productDatas.size()) {
            return new PdResponse(true, ResponseType.SUCCESS.CODE, ResponseType.SUCCESS.PARTIAL_SUCCESS_MESSAGE, errorLines);
        } else {
            return new PdResponse(false, ResponseType.FAILURE.CODE, ResponseType.FAILURE.MESSAGE, errorLines);
        }
    }

    /**
     * 检查productData是否合法，并记录非法信息
     *
     * @param productData ProductData
     * @param errorLines  错误信息
     * @return 是否合法
     */
    private boolean validate(ProductData productData, List<ErrorLine> errorLines) {
        ValidateResult validateResult = productDataValidator.validate(productData);
        if (validateResult.isValid()) {
            return true;
        } else {
            errorLines.add(new ErrorLine(productData.getCode(), validateResult.getMessage()));
            return false;
        }
    }

    /**
     * 保存商品的频道信息
     *
     * @param product 商品
     */
    private void saveOdtypes(Product product) {

        Odtype odtype = new Odtype();
        odtype.setProductId(product.getProductId());
        List<Odtype> odtypeList = iOdtypeService.select(odtype);

        // 如果商品已经保存了odtype则无需保存
        if (CollectionUtils.isEmpty(odtypeList)) {
            Odtype superOdtype = new Odtype();
            superOdtype.setProductId(product.getProductId());
            superOdtype.setIsUsed(Constants.NO);
            superOdtype.setApprovalStatus(Constants.APPROVAL_STATUS_CHECKED);
            superOdtype.setCustChanSrc(Constants.ODTYPE_SUPER);

            Odtype regularOdtype = new Odtype();
            regularOdtype.setProductId(product.getProductId());
            regularOdtype.setIsUsed(Constants.NO);
            regularOdtype.setApprovalStatus(Constants.APPROVAL_STATUS_CHECKED);
            regularOdtype.setCustChanSrc(Constants.ODTYPE_REGULAR);

            iOdtypeService.insertSelective(superOdtype);
            iOdtypeService.insertSelective(regularOdtype);
        }
    }

    private void addProductNewNotice() {

    }
}
