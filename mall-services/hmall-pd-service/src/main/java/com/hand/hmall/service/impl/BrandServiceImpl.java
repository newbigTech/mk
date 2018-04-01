package com.hand.hmall.service.impl;

import com.hand.hmall.model.Brand;
import com.hand.hmall.service.IBrandService;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name BrandServiceImpl
 * @description 品牌服务实现类
 * @date 2017/6/30 14:03
 */

@Service
public class BrandServiceImpl extends
        BaseServiceImpl<Brand> implements IBrandService {

    /**
     * {@inheritDoc}
     *
     * @see IBrandService#selectByCode(String)
     */
    public Brand selectByCode(String code) {
        Brand brand = new Brand();
        brand.setCode(code);
        return this.selectOne(brand);
    }
}
