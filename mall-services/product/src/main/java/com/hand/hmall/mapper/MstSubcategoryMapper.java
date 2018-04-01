package com.hand.hmall.mapper;


import com.hand.hmall.dto.CategoryInfo;
import com.hand.hmall.dto.MstSubcategory;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MstSubcategoryMapper extends Mapper<MstSubcategory> {

    List<CategoryInfo> queryForSaleCategory();

}