package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.github.pagehelper.PageHelper;
import com.hand.promotion.dto.Catalogversion;
import com.hand.promotion.dto.HmallMstProduct;
import com.hand.promotion.mapper.HmallMstProductMapper;
import com.hand.promotion.service.ICatalogversionService;
import com.hand.promotion.service.IProductService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author XinyangMei
 * @version 1.0
 * @Title IProductCodeServiceImpl
 * @Description 查询产品信息的service层
 * @date 2017/6/28 15:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProductCodeServiceImpl implements IProductService {
    @Autowired
    private HmallMstProductMapper hmallMstProductMapper;
    @Resource
    private ICatalogversionService catalogversionService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 根据产品版本与编码查询产品信息
     *
     * @param productCode 产品编码
     * @return
     */
    @Override
    public HmallMstProduct selectMKOnlineProductByCode(String productCode) {
        logger.info(">>>>>>>>productCode>>{}", productCode);
        Catalogversion mkOline = catalogversionService.selectMarkorOnline();
        HmallMstProduct product = hmallMstProductMapper.selectByProductCode(productCode, mkOline.getCatalogversionId());
        logger.info(">>>>>>>>" + product);
        return product;
    }

    @Override
    public List<HmallMstProduct> selectProductByCatalogId(int page, int pageSize, long catalogId) {
        Catalogversion mkOline = catalogversionService.selectMarkorOnline();
        PageHelper.startPage(page, pageSize);
        return hmallMstProductMapper.selectByCatalog(mkOline.getCatalogversionId());
    }



    @Override
    public HmallMstProduct selectByProductId(long productId) {
        return hmallMstProductMapper.selectByPrimaryKey(productId);
    }

    @Override
    public List<HashMap> selectFeightInfo(List productCodes) {
        if(CollectionUtils.isEmpty(productCodes)){
            return null;
        }
        return hmallMstProductMapper.queryFeightInfo(productCodes);
    }


}
