package com.hand.hmall.mapper;

import com.hand.hmall.model.ProductInvInfo;

import java.util.List;

public interface ProductInvInfoMapper {


    /**
     *
     * @mbggenerated 2017-07-24
     */
    List<ProductInvInfo> selectByProductCode(String productCode);


}