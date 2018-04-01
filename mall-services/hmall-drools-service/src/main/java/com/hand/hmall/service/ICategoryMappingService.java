package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.CategoryMapping;

import javax.annotation.Resource;

/**
 * @author XinyangMei
 * @Title ICategoryMappingService
 * @Description desp
 * @date 2017/6/28 21:22
 */
public interface ICategoryMappingService {
   CategoryMapping getCategoryByProductId(long productId);
}
