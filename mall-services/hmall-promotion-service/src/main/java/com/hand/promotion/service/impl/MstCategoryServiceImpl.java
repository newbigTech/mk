package com.hand.promotion.service.impl;

import com.hand.promotion.dto.MstCategory;
import com.hand.promotion.mapper.MstCategoryMapper;
import com.hand.promotion.service.IMstCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MstCategoryServiceImpl extends BaseServiceImpl<MstCategory> implements IMstCategoryService {

    @Autowired
    private MstCategoryMapper mstCategoryMapper;

}