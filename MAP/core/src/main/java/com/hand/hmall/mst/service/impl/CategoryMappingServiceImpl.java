package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.CategoryMapping;
import com.hand.hmall.mst.dto.CategoryMappingDto;
import com.hand.hmall.mst.dto.ProductDto;
import com.hand.hmall.mst.mapper.CategoryMappingMapper;
import com.hand.hmall.mst.service.ICategoryMappingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CategoryMappingServiceImpl
 * @description 实现类
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class CategoryMappingServiceImpl extends BaseServiceImpl<CategoryMapping> implements ICategoryMappingService {

    @Autowired
    CategoryMappingMapper mapper;

    /**
     * 接口标识
     * @param list
     */
    @Override
    public void updateFlagByPk(List<CategoryMappingDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            mapper.updateFlagBymappingId(list);
        }

    }

    /**
     * 根据商品ID 当前类别ID查询所有商品对应关系
     * @param productIdArray 子类别ID
     * @param categoryId     当前类别ID
     * @return
     */
    @Override
    public List<CategoryMapping> queryListByProductIdAndCategoryId(String[] productIdArray, String categoryId) {
        return mapper.queryListByProductIdAndCategoryId(productIdArray, categoryId);
    }

    /**
     * 商品详情页面中删除类别结构映射信息
     * @param categoryMapping 类别映射dto
     * @return
     */
    @Override
    public int deleteCategoryMapping(CategoryMapping categoryMapping) {
        return mapper.deleteCategoryMapping(categoryMapping);
    }

    /**
     * 接口同步数据查询
     * @param list
     * @return
     */
    @Override
    public List<CategoryMappingDto> querySyncData(List<ProductDto> list) {
        return mapper.querySyncData(list);
    }

    /**
     * 导入商品模板更新商品和类别映射关系时删掉不要的映射关系
     * @param map
     * @return
     */
    @Override
    public int deleteCategoryMappingRelationShip(Map map) {
        return mapper.deleteCategoryMappingRelationShip(map);
    }

    /**
     * 根据商品ID查询对应的映射关系
     * @param productId
     * @return
     */
    @Override
    public List<CategoryMapping> getMappingInfoByProductId(Long productId) {
        return mapper.getMappingInfoByProductId(productId);
    }

    /**
     * 根据商品和类别ID查询指定映射
     * @param categoryMapping
     * @return
     */
    @Override
    public CategoryMapping selectByCategoryAndProductId(CategoryMapping categoryMapping) {
        return mapper.selectByCategoryAndProductId(categoryMapping);
    }
}