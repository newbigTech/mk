package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HmallMstProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title IProductCodeServiceImpl
 * @Description 查询产品信息的service层
 * @date 2017/6/28 15:24
 * @version 1.0
 */
public interface IProductService {
    /**
     * 根据产品版本与编码查询产品信息
     * @param productCode 产品编码
     * @return
     */
    HmallMstProduct selectMKOnlineProductByCode(String productCode);

    /**
     * 根据版本类别查询产品
     * @param catalogId 版本类别
     * @return
     */
    List<HmallMstProduct> selectProductByCatalogId(int page, int pageSize, long catalogId);

    HmallMstProduct selectByProductId(long productId);

    List<HashMap> selectFeightInfo(List productCodes);
}
