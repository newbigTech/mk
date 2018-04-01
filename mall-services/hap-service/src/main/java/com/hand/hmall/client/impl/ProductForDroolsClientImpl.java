package com.hand.hmall.client.impl;

import com.hand.hmall.client.IProductForDroolsClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/21.
 */
public class ProductForDroolsClientImpl implements IProductForDroolsClient {


    @Override
    public ResponseData selectAllProduct(Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData selectGift(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData queryByNotInAndCount(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds) {
        return null;
    }

    @Override
    public ResponseData queryByProductIds(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData checkedProductCount(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }

    @Override
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
        return null;
    }
}
