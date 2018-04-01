package com.hand.hmall.service;

import com.hand.hmall.model.Brand;

/**
 * @author 马君
 * @version 0.1
 * @name IBrandService
 * @description IBrandService
 * @date 2017/6/30 14:01
 */
public interface IBrandService extends IBaseService<Brand> {

    /**
     * 通过品牌code查询
     * @param code 品牌编码
     * @return Brand
     */
    Brand selectByCode(String code);
}
