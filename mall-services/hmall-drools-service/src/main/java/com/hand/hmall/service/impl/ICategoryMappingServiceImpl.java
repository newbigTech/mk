package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.CategoryMapping;
import com.hand.hmall.mapper.CategoryMappingMapper;
import com.hand.hmall.service.ICategoryMappingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author XinyangMei
 * @Title ICategoryMappingServiceImpl
 * @Description 查询订单行商品分类
 * @date 2017/6/28 21:23
 */
@Service
public class ICategoryMappingServiceImpl implements ICategoryMappingService{
    @Resource
    private CategoryMappingMapper categoryMappingMapper;

    @Override
    public CategoryMapping getCategoryByProductId(long productId) {
        return categoryMappingMapper.selectCategortByProductId(productId);
    }
}
