package com.hand.hmall.controller;

import com.hand.hmall.client.price.IPriceClient;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.pojo.PriceRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by qinzhipeng on 2017/9/6.
 */
@RequestMapping(value = "/h/price", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class HapPriceController {
    @Autowired
    private IPriceClient priceClient;

    /**
     * 计算销售价格
     * @param priceRequestDataList
     * @return
     */
    @RequestMapping(value = "/calculateSalePrice", method = RequestMethod.POST)
    public ResponseData calculateSalePrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return priceClient.calculateSalePrice(priceRequestDataList);
    }

    /**
     * 计算采购价格
     * @param priceRequestDataList
     * @return
     */
    @RequestMapping(value = "/calculateOrderPrice", method = RequestMethod.POST)
    public ResponseData calculateOrderPrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return priceClient.calculateOrderPrice(priceRequestDataList);
    }

}
