package com.hand.hmall.client.price.impl;

import com.hand.hmall.client.price.IPriceClient;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.pojo.PriceRequestData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by qinzhipeng on 2017/9/6.
 */
public class PriceClientImpl implements IPriceClient {
    @Override
    public ResponseData calculateSalePrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return null;
    }

    @Override
    public ResponseData calculateOrderPrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return null;
    }
}
