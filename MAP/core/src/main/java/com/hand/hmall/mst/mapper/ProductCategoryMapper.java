package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Category;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.ProductCategory;

import java.util.List;

/**
 * @author zhangyanan
 * @version 0.1
 * @name ProductCategoryMapper
 * @description 商品类别Mapper
 * @date 2017/5/22
 */
public interface ProductCategoryMapper extends Mapper<ProductCategory> {
    List<Category> getInfo();

    /**
     * 批量更新接口同步标识
     *
     * @param dto
     */
    void updateCategory(List<Category> dto);

    /**
     * 模糊查询商品类别列表
     *
     * @param productCategory
     * @return
     */
    List<ProductCategory> getProductCategoryList(ProductCategory productCategory);

    /**
     * 查询超类别列表
     *
     * @return
     */
    List<ProductCategory> querySuperType(ProductCategory dto);

    /**
     * 查询子类别列表
     *
     * @return
     */
    List<ProductCategory> querySubType(ProductCategory dto);

    /**
     * 根据类别ID查询商品列表
     *
     * @return
     */
    List<Product> queryProductByCategoryId(ProductCategory dto);

    /**
     * 根据类别Code和版本目录查询对应类别ID
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
    List<ProductCategory> queryTypeNotInSuperAndSub(ProductCategory dto);

    /**
     * 根据商品ID查询类别列表
     *
     * @param dto
     * @return
     */
    List<ProductCategory> selectProductCategoryList(ProductCategory dto);

    /**
     * 根据类别ID查询未包含的商品
     *
     * @return
     */
    List<Product> queryProductNotItself(ProductCategory dto);

    /**
     * 根据类别编码和目录版本获得类别列表
     *
     * @return
     */
    ProductCategory queryCategoryByCategoryIdAndVersion(ProductCategory dto);
}