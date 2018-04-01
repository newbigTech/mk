package com.hand.promotion.mapper;


import com.hand.promotion.dto.CategoryMapping;

/**
 * 商品分类Mapper
 *
 * @author xinyangMei
 */
public interface CategoryMappingMapper {

    /**
     * 根据商品主键查询商品分类数据
     *
     * @param productId 商品主键
     * @return
     */
    CategoryMapping selectCategortByProductId(Long productId);

}