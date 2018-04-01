package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Brand;
import com.hand.hmall.mst.dto.BrandDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 品牌对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 推送syncFlag为N的品牌信息到商城当中
     *
     * @return
     */
    List<BrandDto> selectPushingBrand();

    /**
     * @param dto
     * @description 更新品牌接口推送标志
     */
    void updateBrandSyncflag(List<BrandDto> dto);
}