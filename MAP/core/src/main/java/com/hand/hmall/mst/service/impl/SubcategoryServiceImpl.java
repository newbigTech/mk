package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.SubCategory;
import com.hand.hmall.mst.dto.SubCategoryDto;
import com.hand.hmall.mst.mapper.SubcategoryMapper;
import com.hand.hmall.mst.service.ISubcategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SubcategoryServiceImpl
 * @description 实现类
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class SubcategoryServiceImpl extends BaseServiceImpl<SubCategory> implements ISubcategoryService {

    @Autowired
    SubcategoryMapper mapper;

    @Override
    public void updateFlagByPk(List<SubCategoryDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            mapper.updateFlagBymappingId(list);
        }
    }

    @Override
    public List<SubCategory> queryListByCategoryIdAndSubCategoryId(String[] categoryIdArray, String subCategoryId) {
        return mapper.queryListByCategoryIdAndSubCategoryId(categoryIdArray, subCategoryId);
    }

    @Override
    public List<SubCategory> queryListBySubCategoryIdAndCategoryId(String[] subCategoryIdArray, String categoryId) {
        return mapper.queryListBySubCategoryIdAndCategoryId(subCategoryIdArray, categoryId);
    }

    @Override
    public List<SubCategoryDto> querySyncData() {
        return mapper.querySyncData();
    }
}