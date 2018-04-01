package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleOperatorClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleOperatorClientImpl.class)
public interface ISaleExcelClient {
    @RequestMapping(value = "/h/sale/excel/queryByCondition" , method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/excel/user/query" , method = RequestMethod.POST)
    public ResponseData userQuery(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/excel/user/upload" , method = RequestMethod.POST)
    public ResponseData userUpload(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/excel/user/deleteError",method = RequestMethod.GET)
    public ResponseData userDeleteError(@RequestParam("excelId") String excelId);

    @RequestMapping(value = "/h/sale/excel/user/selectByExcelId",method = RequestMethod.GET)
    public ResponseData userSelectByExcelId(@RequestParam("excelId") String excelId);



    @RequestMapping(value = "/h/sale/excel/product/query" , method = RequestMethod.POST)
    public ResponseData productQuery(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/excel/product/upload" , method = RequestMethod.POST)
    public ResponseData productUpload(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/excel/product/deleteError",method = RequestMethod.GET)
    public ResponseData productDeleteError(@RequestParam("excelId") String excelId);

    @RequestMapping(value = "/h/sale/excel/product/selectByExcelId",method = RequestMethod.GET)
    public ResponseData productSelectByExcelId(@RequestParam("excelId") String excelId);
}
