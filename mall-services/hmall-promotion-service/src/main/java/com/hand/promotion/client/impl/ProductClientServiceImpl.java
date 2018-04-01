package com.hand.promotion.client.impl;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.IProductClientService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/27.
 */
@Component(value = "productClientService")
public class ProductClientServiceImpl implements IProductClientService {
    @Override
    public Map<String, Object> queryForCart(String productId, String productCode) {
        return null;
    }

    @Override
    public Map<String, Object> queryForCoupon( String productId,  String productCode) {
        return null;
    }

    @Override
    public List<Map<String,?>> selectProductDetailInfo( String productCode) {
        return null;
    }

    @Override
    public ResponseData selectProductByCode(String productCode) {
        return null;
    }

    @Override
    public ResponseData selectProductByCodes(List<String> codes) {
        return null;
    }

    @Override
    public ResponseData selectSkuByNotIn( List<String> productIds) {
        return null;
    }

    @Override
    public ResponseData selectSpuByNotIn( List<String> productIds) {
        return null;
    }

    @Override
    public ResponseData selectByProductIds( List<String> productIds) {
        return null;
    }


    @Override
    public ResponseData getStoreCategoryIdNotEqual( List<String> notIn) {
        return null;
    }

    @Override
    public ResponseData queryByCondition( Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData queryCategroyByProductCode(@RequestParam("productCode") String productCode) {
        return null;
    }

    @Override
    public ResponseData queryParentId(Long categoryId) {
        return null;
    }


    @Override
    public ResponseData getPictures( Map map) {
        return null;
    }

    @Override
    public ResponseData selectByProductId(String productId) {
        return null;
    }
}
