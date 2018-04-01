package com.hand.hmall.client.impl;

import com.hand.hmall.client.IStockClientService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2017/3/24.
 */
public class StockClientServiceImpl implements IStockClientService {
    @Override
    public List<Map<String, Object>> queryByList(@RequestBody List<Map<String, Object>> condition) {
        return null;
    }

    @Override
    public Map<String, ?> query(@RequestBody Map<String, Object> condition) {
        return null;
    }
}
