package com.hand.hmall.mapper;

import com.hand.hmall.model.Brand;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 马君
 * @version 0.1
 * @name BrandMapper
 * @description 品牌Mapper
 * @date 2017/6/2 16:28
 */

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据品牌名称查询品牌
     * @param name 品牌名称
     * @return Brand
     */
    Brand selectOneByName(@Param("name") String name);

    /**
     * 根据品牌编号唯一的品牌
     * @param code 品牌编号
     * @return Brand
     */
    Brand selectUniqueByCode(@Param("code") String code);
}
