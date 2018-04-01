package com.hand.hmall.controller.sale;

import com.hand.hmall.client.sale.ISaleOperatorClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@RestController
@RequestMapping(value = "/h/sale/operator", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleOperatorController {

    @Autowired
    private ISaleOperatorClient saleOperatorClient;
    @RequestMapping(value = "/queryByBaseId" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData productQuery(@RequestBody Map<String,Object> map)
    {
        return saleOperatorClient.queryByBaseId(map);
    }

    @RequestMapping(value = "/getSynCoupon" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getSynCoupon()
    {
        return saleOperatorClient.getSynCoupon();
    }


    @RequestMapping(value = "/setSynCoupon" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData setSynCoupon(@RequestBody List<Map> couponList)
    {
        return saleOperatorClient.setSynCoupon(couponList);
    }
}
