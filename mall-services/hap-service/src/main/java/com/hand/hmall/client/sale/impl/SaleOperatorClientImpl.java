package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleOperatorClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
public class SaleOperatorClientImpl implements ISaleOperatorClient{
    @Override
    public ResponseData queryByBaseId(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData getSynCoupon() {
        return null;
    }

    @Override
    public ResponseData setSynCoupon(@RequestBody List<Map> couponList) {
        return null;
    }


}
