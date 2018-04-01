package com.hand.promotion.client.impl;

import com.hand.promotion.client.IStockClientService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2017/3/24.
 */
@Component
public class StockClientServiceImpl implements IStockClientService {
    @Override
    public List<Map<String, Object>> queryByList( List<Map<String, Object>> condition) {
        return null;
    }

    @Override
    public Map<String, ?> query( Map<String, Object> condition) {
        return null;
    }
}
