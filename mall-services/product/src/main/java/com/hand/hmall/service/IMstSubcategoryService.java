package com.hand.hmall.service;


import com.hand.hmall.dto.CategoryInfo;
import com.hand.hmall.dto.MstSubcategory;

import java.util.List;
import java.util.Map;

public interface IMstSubcategoryService {
    /**
     * 促销商品类别条件使用，查询所有商品分类
     * @return
     */
    List<CategoryInfo> queryForSaleCategory(Map map);

    List<MstSubcategory> queryParentId(long categoryId);

}