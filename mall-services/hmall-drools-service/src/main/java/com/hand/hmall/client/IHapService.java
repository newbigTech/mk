package com.hand.hmall.client;

import com.hand.hmall.client.impl.HapServiceImpl;
import com.hand.hmall.dto.PriceRequestData;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author baihua
 * @version 0.1
 * @name IHapService$
 * @description $END$
 * @date 2017/12/19$
 */
@FeignClient(value = "hap-service", fallback = HapServiceImpl.class)
public interface IHapService {
    /**
     * 调用HAP微服务  V码获取商品价格
     * @param priceRequestDataList
     * @return
     */
    @RequestMapping(value = "/h/price/calculateSalePrice", method = RequestMethod.POST)
    public ResponseData calculateSalePrice(@RequestBody List<PriceRequestData> priceRequestDataList);

}
