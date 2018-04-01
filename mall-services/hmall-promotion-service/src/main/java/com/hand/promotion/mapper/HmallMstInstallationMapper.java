package com.hand.promotion.mapper;


import com.hand.promotion.dto.HmallMstInstallation;

/**
 * 商品安装费基础信息查询Mapper
 */
public interface HmallMstInstallationMapper {


    /**
     * 根据商品分类和状态查询安装费基础信息
     *
     * @param categoryId
     * @param status
     * @return
     */
    HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId, String status);

}