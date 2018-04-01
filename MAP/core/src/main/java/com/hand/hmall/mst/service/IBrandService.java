package com.hand.hmall.mst.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Brand;
import com.hand.hmall.mst.dto.BrandDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 品牌对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IBrandService extends IBaseService<Brand>, ProxySelf<IBrandService> {

    /**
     * 推送syncFlag为N的品牌信息到商城当中
     *
     * @return
     */
    public List<BrandDto> selectPushingBrand();

    /**
     * @param dto
     * @description 更新品牌接口推送标志
     */
    public void updateBrandSyncflag(List<BrandDto> dto);
}