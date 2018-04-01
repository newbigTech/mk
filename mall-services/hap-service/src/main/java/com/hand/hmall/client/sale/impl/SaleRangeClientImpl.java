package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleRangeClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
public class SaleRangeClientImpl implements ISaleRangeClient {

    @Override
    public ResponseData conditionQuery(@RequestBody Map<String, Object> datas) {
        return null;
    }

    @Override
    public ResponseData groupQuery(String type) {
        return null;
    }

    @Override
    public ResponseData groupSubmit(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }

    @Override
    public ResponseData queryByConditions(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData groupDelete(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }


}
