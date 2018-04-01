package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */


import com.hand.promotion.dto.CategoryMapping;

/**
 * @author XinyangMei
 * @Title ICategoryMappingService
 * @Description desp
 * @date 2017/6/28 21:22
 */
public interface ICategoryMappingService {
   CategoryMapping getCategoryByProductId(long productId);
}
