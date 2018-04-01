package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleDrawClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 */
public class SaleDrawClientImpl implements ISaleDrawClient {
    @Override
    public ResponseData query(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData submit(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData active(@RequestBody List<Map<String, Object>> maps) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData detail(@RequestParam("id") String id) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData awardPro(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData submitAwardPro(@RequestBody List<Map<String, Object>> list) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData startUsingAwardPro(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData endUsingAwardPro(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData getAward(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }
}
