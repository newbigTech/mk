package com.hand.hmall.client.price;

import com.hand.hmall.client.price.impl.PriceClientImpl;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.pojo.PriceRequestData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by qinzhipeng on 2017/9/6.
 */
@FeignClient(value = "hmall-pd-service", fallback = PriceClientImpl.class)
public interface  IPriceClient {
    @RequestMapping(value = "/i/price/calculateSalePrice", method = RequestMethod.POST)
    public ResponseData calculateSalePrice(@RequestBody List<PriceRequestData> priceRequestDataList);

    @RequestMapping(value = "/i/price/calculateOrderPrice", method = RequestMethod.POST)
    public ResponseData calculateOrderPrice(@RequestBody List<PriceRequestData> priceRequestDataList);
}
