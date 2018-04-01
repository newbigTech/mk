package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.MstCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hmall.dto.MstCategory;
import com.hand.hmall.service.IMstCategoryService;

import java.util.List;

@Service
public class MstCategoryServiceImpl extends BaseServiceImpl<MstCategory> implements IMstCategoryService {

    @Autowired
    private MstCategoryMapper mstCategoryMapper;

}