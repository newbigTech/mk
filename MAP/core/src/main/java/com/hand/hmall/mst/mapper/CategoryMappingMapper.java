package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.CategoryMapping;
import com.hand.hmall.mst.dto.CategoryMappingDto;
import com.hand.hmall.mst.dto.ProductDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CategoryMappingMapper
 * @description 类别映射mapper
 * @date 2017年5月26日10:52:23
 */
public interface CategoryMappingMapper extends Mapper<CategoryMapping> {

    /**
     * 接口标识
     *
     * @param list
     */
    void updateFlagBymappingId(List<CategoryMappingDto> list);

    /**
     * 根据商品ID 当前类别ID查询所有商品对应关系
     *
     * @param productIdArray 子类别ID
     * @param categoryId     当前类别ID
     * @return
     */
    List<CategoryMapping> queryListByProductIdAndCategoryId(@Param("productIdArray") String[] productIdArray, @Param("categoryId") String categoryId);

    /**
     * 商品详情页面中删除类别结构映射信息
     *
     * @param categoryMapping 类别映射dto
     * @return
     */
    int deleteCategoryMapping(CategoryMapping categoryMapping);


    /**
     * 接口同步数据查询
     *
     * @return
     */
    List<CategoryMappingDto> querySyncData(List<ProductDto> list);

    /**
     * @param map
     * @return
     * @description 导入商品模板更新商品和类别映射关系时删掉不要的映射关系
     */
    int deleteCategoryMappingRelationShip(Map map);

    /**
     * 根据商品ID查询对应的映射关系
     *
     * @param productId
     * @return
     */
    List<CategoryMapping> getMappingInfoByProductId(Long productId);

    /**
     * 根据商品和类别ID查询指定映射
     *
     * @return
     */
    CategoryMapping selectByCategoryAndProductId(CategoryMapping categoryMapping);
}