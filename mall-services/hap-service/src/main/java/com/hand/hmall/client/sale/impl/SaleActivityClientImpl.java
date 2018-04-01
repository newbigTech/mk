package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleActivityClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@Service
public class SaleActivityClientImpl implements ISaleActivityClient {
    @Override
    public ResponseData query(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData submit(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData activity(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }

    @Override
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps) {
        return null;
    }

    @Override
    public ResponseData detail(@RequestParam("id") String id) {
        return null;
    }

    @Override
    public ResponseData delete(@RequestBody List<Map<String, String>> maps) {
        return null;
    }
}
