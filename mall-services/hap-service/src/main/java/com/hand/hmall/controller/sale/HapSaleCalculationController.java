package com.hand.hmall.controller.sale;

import com.hand.hmall.client.sale.ISaleCalculationClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author tiantao
 *
 */
@RestController
@RequestMapping(value = "/h/sale/calculation", produces = { MediaType.APPLICATION_JSON_VALUE })
public class HapSaleCalculationController {
    @Autowired
    private ISaleCalculationClient saleCalculationClient;
    @RequestMapping(value = "/optionCartByMap" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData optionCartByMap(@RequestBody List<Map<String,Object>> carts) {

        return saleCalculationClient.optionCartByMap(carts);

    }

    @RequestMapping(value = "/promote" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData promote(@RequestBody Map<String,Object> objectMap) {

        return saleCalculationClient.promote(objectMap);

    }

}
