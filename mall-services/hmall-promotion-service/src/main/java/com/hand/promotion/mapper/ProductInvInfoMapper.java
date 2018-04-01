package com.hand.promotion.mapper;


import com.hand.promotion.model.ProductInvInfo;

import java.util.List;

/**
 * 商品库存信息Mapper
 *
 * @author xinyangMei
 */
public interface ProductInvInfoMapper {


    /**
     * 根据商品编码查询库存信息
     *
     * @param productCode
     * @return
     */
    List<ProductInvInfo> selectByProductCode(String productCode);


}