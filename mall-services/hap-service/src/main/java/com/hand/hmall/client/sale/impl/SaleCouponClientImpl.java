package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleCouponClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
public class SaleCouponClientImpl implements ISaleCouponClient{
    @Override
    public ResponseData query(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData queryActivity(@RequestBody Map<String, Object> datas) {
        return null;
    }

    @Override
    public ResponseData queryByNotIn(@RequestBody Map<String, Object> datas) {
        return null;
    }

    @Override
    public ResponseData submit(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData startUsing(@RequestBody List<Map<String, Object>> datas) {
        return null;
    }

    @Override
    public ResponseData endUsing(@RequestBody List<Map<String, Object>> datas) {
        return null;
    }

    @Override
    public ResponseData detail(@RequestParam("id") String id) {
        return null;
    }

    @Override
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }


}
