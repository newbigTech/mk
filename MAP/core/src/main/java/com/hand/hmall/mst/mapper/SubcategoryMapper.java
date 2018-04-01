package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.SubCategory;
import com.hand.hmall.mst.dto.SubCategoryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SubcategoryMapper
 * @description 子类别映射
 * @date 2017年5月26日10:52:23
 */

public interface SubcategoryMapper extends Mapper<SubCategory> {

    /**
     * 更新标识
     *
     * @param list
     */
    void updateFlagBymappingId(List<SubCategoryDto> list);

    /**
     * 根据超类别ID  当前类别ID查询所有超类别对应关系
     *
     * @param categoryIdArray 超类别ID
     * @param subCategoryId   当前类别ID
     * @return
     */
    List<SubCategory> queryListByCategoryIdAndSubCategoryId(@Param("categoryIdArray") String[] categoryIdArray, @Param("subCategoryId") String subCategoryId);

    /**
     * 根据子类别ID 当前类别ID查询所有子类别对应关系
     *
     * @param subCategoryIdArray 子类别ID
     * @param categoryId         当前类别ID
     * @return
     */
    List<SubCategory> queryListBySubCategoryIdAndCategoryId(@Param("subCategoryIdArray") String[] subCategoryIdArray, @Param("categoryId") String categoryId);

    /**
     * 推送官网数据查询
     *
     * @return
     */
    List<SubCategoryDto> querySyncData();
}