package com.hand.hmall.service;

import com.hand.hmall.model.Product;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IProductService
 * @description 商品Service
 * @date 2017/6/30 9:48
 */
public interface IProductService extends IBaseService<Product> {

    /**
     * 查询master staged版本的商品
     * @param productCode 商品编号
     * @return 商品
     */
    Product selectMasterStagedByCode(String productCode);

    /**
     * 查询markor online版本的商品
     * @param productCode 商品编号
     * @return 商品
     */
    Product selectMarkorOnlineByCode(String productCode);

    /**
     * 根据平台号查询Master Staged版本的商品
     * @param platformCode
     * @return
     */
    List<Product> selectMasterStagedByPlatformCode(String platformCode);

    /**
     * 通过v码查询online版本的商品
     * @param vCode v码
     * @return Product
     */
    Product selectMarkorOnlineByVCode(String vCode);
}
