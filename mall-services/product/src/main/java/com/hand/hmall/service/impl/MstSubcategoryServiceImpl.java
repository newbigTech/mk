package com.hand.hmall.service.impl;


import com.hand.hmall.dto.CategoryInfo;
import com.hand.hmall.dto.MstSubcategory;
import com.hand.hmall.mapper.MstSubcategoryMapper;
import com.hand.hmall.service.IMstSubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class MstSubcategoryServiceImpl implements IMstSubcategoryService {
    @Autowired
    private MstSubcategoryMapper mstSubcategoryMapper;

    /**
     * 促销商品类别条件使用，查询所有商品分类
     *
     * @return
     */
    @Override
    public List<CategoryInfo> queryForSaleCategory(Map map) {
        return mstSubcategoryMapper.queryForSaleCategory();
    }

    @Override
    public List<MstSubcategory> queryParentId(long categoryId) {
        MstSubcategory mstSubcategory = new MstSubcategory();
        mstSubcategory.setSubCategoryId(categoryId);
        return mstSubcategoryMapper.select(mstSubcategory);
    }
}