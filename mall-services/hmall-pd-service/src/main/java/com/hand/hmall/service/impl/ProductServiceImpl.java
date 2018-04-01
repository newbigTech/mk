package com.hand.hmall.service.impl;

import com.hand.hmall.model.Catalogversion;
import com.hand.hmall.model.Product;
import com.hand.hmall.service.ICatalogversionService;
import com.hand.hmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ProductServiceImpl
 * @description 商品服务实现类
 * @date 2017/6/30 9:50
 */
@Service
public class ProductServiceImpl extends
        BaseServiceImpl<Product> implements IProductService {

    @Autowired
    private ICatalogversionService iCatalogversionService;

    /**
     * {@inheritDoc}
     *
     * @see IProductService#selectMasterStagedByCode
     */
    @Override
    public Product selectMasterStagedByCode(String productCode) {
        Catalogversion masterStaged = iCatalogversionService.selectMasterStaged();
        Product product = new Product();
        product.setCode(productCode);
        product.setCatalogversionId(masterStaged.getCatalogversionId());
        return this.selectOne(product);
    }

    /**
     * {@inheritDoc}
     *
     * @see IProductService#selectMarkorOnlineByCode
     */
    @Override
    public Product selectMarkorOnlineByCode(String productCode) {
        Catalogversion markorOnline = iCatalogversionService.selectMarkorOnline();
        Product product = new Product();
        product.setCode(productCode);
        product.setCatalogversionId(markorOnline.getCatalogversionId());
        return this.selectOne(product);
    }

    /**
     * {@inheritDoc}
     *
     * @see IProductService#selectMasterStagedByPlatformCode(String)
     */
    @Override
    public List<Product> selectMasterStagedByPlatformCode(String platformCode) {
        Catalogversion markorOnline = iCatalogversionService.selectMasterStaged();
        Product product = new Product();
        product.setPlatformCode(platformCode);
        return this.select(product);
    }

    @Override
    public void updateByPrimaryKeySelective(Product product) {
        product.setLastUpdateDate(new Date());
        super.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product selectMarkorOnlineByVCode(String vCode) {
        try {
            Catalogversion markorOnline = iCatalogversionService.selectMarkorOnline();
            Product product = new Product();
            product.setvProductCode(vCode);
            product.setCatalogversionId(markorOnline.getCatalogversionId());
            return this.selectOne(product);
        } catch (Exception e) {
            return null;
        }
    }
}
