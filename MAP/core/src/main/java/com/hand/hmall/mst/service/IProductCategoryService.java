package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Category;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.ProductCategory;
import com.hand.hmall.mst.dto.SyncData;

import java.util.List;

/**
 * @author zhangyanan
 * @version 0.1
 * @name IProductCategoryService
 * @description 商品类别Service
 * @date 2017/5/26
 */
public interface IProductCategoryService extends IBaseService<ProductCategory>, ProxySelf<IProductCategoryService> {
    List<Category> getInfo();

    /**
     * 查询商品类别列表(商品类别、商品编码模糊查询)
     *
     * @param productCategory
     * @return
     */
    List<ProductCategory> getProductCategoryList(ProductCategory productCategory, int pageNum, int pageSize);

    /**
     * 查询超类别列表
     *
     * @return
     */
    List<ProductCategory> querySuperType(ProductCategory dto, int page, int pageSize);

    /**
     * 查询子类别列表
     *
     * @return
     */
    List<ProductCategory> querySubType(ProductCategory dto, int page, int pageSize);

    /**
     * 根据类别ID查询商品列表
     *
     * @return
     */
    List<Product> queryProductByCategoryId(ProductCategory dto, int page, int pageSize);

    /**
     * 根据类别Code和版本目录查询对应类别
     *
     * @param category
     * @return
     */
    Long selectByCodeAndVersion(ProductCategory category);

    /**
     * 根据类别ID查询查询超类别子类别都没有的其他类别
     *
     * @return
     */
    List<ProductCategory> queryTypeNotInSuperAndSub(ProductCategory dto, int page, int pageSize);

    /**
     * 根据类别ID查询未包含的商品
     *
     * @return
     */
    List<Product> queryProductNotItself(ProductCategory dto, int page, int pageSize);

    /**
     * 根据商品ID关联的映射表中的类别Id而查出类别列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<ProductCategory> selectProductCategoryList(ProductCategory dto, int page, int pageSize);

    /**
     * 根据类别id和目录版本查询类别信息
     *
     * @param dto
     * @return
     */
    ProductCategory queryCategoryByCategoryIdAndVersion(ProductCategory dto);


    /**
     * 类别同步
     *
     * @param iRequest
     * @param dto
     * @return
     */
    List<SyncData> sync(IRequest iRequest, List<SyncData> dto);

    /**
     * 根据categoryId查询类别
     *
     * @param categoryId
     * @return
     */
    ProductCategory selectByCategoryId(Long categoryId);
}