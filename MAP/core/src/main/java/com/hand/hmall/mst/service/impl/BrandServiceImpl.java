package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Brand;
import com.hand.hmall.mst.dto.BrandDto;
import com.hand.hmall.mst.mapper.BrandMapper;
import com.hand.hmall.mst.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 品牌对象的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class BrandServiceImpl extends BaseServiceImpl<Brand> implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 推送syncFlag为N的品牌信息到商城当中
     * @return
     */
    @Override
    public List<BrandDto> selectPushingBrand() {
        return brandMapper.selectPushingBrand();
    }

    /**
     * 更新品牌接口推送标志
     * @param dto
     */
    @Override
    public void updateBrandSyncflag(List<BrandDto> dto) {
        brandMapper.updateBrandSyncflag(dto);
    }

}