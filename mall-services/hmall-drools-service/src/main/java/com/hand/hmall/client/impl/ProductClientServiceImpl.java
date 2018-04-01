package com.hand.hmall.client.impl;

import com.hand.hmall.client.IProductClientService;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/27.
 */
public class ProductClientServiceImpl implements IProductClientService {
    @Override
    public Map<String, Object> queryForCart(@PathVariable(value = "productId") String productId, @PathVariable(value = "productCode") String productCode) {
        return null;
    }

    @Override
    public Map<String, Object> queryForCoupon(@PathVariable(value = "productId") String productId, @PathVariable(value = "productCode") String productCode) {
        return null;
    }

    @Override
    public List<Map<String,?>> selectProductDetailInfo(@PathVariable("productCode") String productCode) {
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
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds) {
        return null;
    }

    @Override
    public ResponseData selectSpuByNotIn(@RequestBody List<String> productIds) {
        return null;
    }

    @Override
    public ResponseData selectByProductIds(@RequestBody List<String> productIds) {
        return null;
    }

//    @Override
//    public ResponseData getProductIdByStoreCategoryId(@RequestBody List<Map<String, Object>> maps) {
//        return null;
//    }

    @Override
    public ResponseData getStoreCategoryIdNotEqual(@RequestBody List<String> notIn) {
        return null;
    }

    @Override
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
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
    public ResponseData getPictures(@RequestBody Map map) {
        return null;
    }

    @Override
    public ResponseData selectByProductId(String productId) {
        return null;
    }
}
