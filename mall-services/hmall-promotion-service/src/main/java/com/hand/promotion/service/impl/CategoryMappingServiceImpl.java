package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.CategoryMapping;
import com.hand.promotion.mapper.CategoryMappingMapper;
import com.hand.promotion.service.ICategoryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author XinyangMei
 * @Title ICategoryMappingServiceImpl
 * @Description 查询订单行商品分类
 * @date 2017/6/28 21:23
 */
@Service
public class CategoryMappingServiceImpl implements ICategoryMappingService {
    @Autowired
    private CategoryMappingMapper categoryMappingMapper;

    /**
     * 根据商品id查询商品分类
     *
     * @param productId
     * @return
     */
    @Override
    public CategoryMapping getCategoryByProductId(long productId) {
        return categoryMappingMapper.selectCategortByProductId(productId);
    }
}
