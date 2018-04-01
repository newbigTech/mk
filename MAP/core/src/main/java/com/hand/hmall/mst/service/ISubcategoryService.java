package com.hand.hmall.mst.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.SubCategory;
import com.hand.hmall.mst.dto.SubCategoryDto;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ISubcategoryService
 * @description 子类别
 * @date 2017年5月26日10:52:23
 */

public interface ISubcategoryService extends IBaseService<SubCategory>, ProxySelf<ISubcategoryService> {

    /**
     * 更新标识
     *
     * @param list
     */
    void updateFlagByPk(List<SubCategoryDto> list);

    /**
     * 根据超类别ID  当前类别ID查询所有超类别对应关系
     *
     * @param categoryIdArray 超类别ID
     * @param subCategoryId   当前类别ID
     * @return
     */
    List<SubCategory> queryListByCategoryIdAndSubCategoryId(String[] categoryIdArray, String subCategoryId);

    /**
     * 根据子类别ID 当前类别ID查询所有子类别对应关系
     *
     * @param subCategoryIdArray 子类别ID
     * @param categoryId         当前类别ID
     * @return
     */
    List<SubCategory> queryListBySubCategoryIdAndCategoryId(String[] subCategoryIdArray, String categoryId);

    /**
     * 推送官网数据查询
     *
     * @return
     */
    List<SubCategoryDto> querySyncData();
}