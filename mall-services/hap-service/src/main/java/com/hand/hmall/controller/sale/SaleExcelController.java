package com.hand.hmall.controller.sale;

import com.hand.hmall.client.sale.ISaleExcelClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 */
@RestController
@RequestMapping(value = "/h/sale/excel", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleExcelController {
    @Autowired
    private ISaleExcelClient saleExcelClient;
    @RequestMapping(value = "/queryByCondition" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByCondition(@RequestBody Map<String,Object> map)
    {
        return saleExcelClient.queryByCondition(map);
    }

    @RequestMapping(value = "/user/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData userQuery(@RequestBody Map<String,Object> map)
    {
        return saleExcelClient.userQuery(map);
    }

    @RequestMapping(value = "/user/upload" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData userUpload(@RequestBody Map<String,Object> map)
    {
        return saleExcelClient.userUpload(map);
    }

    @RequestMapping(value = "/user/deleteError" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData userDeleteError(@RequestParam("excelId") String excelId)
    {
        return saleExcelClient.userDeleteError(excelId);
    }
    @RequestMapping(value = "/user/selectByExcelId" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData userSelectByExcelId(@RequestParam("excelId") String excelId)
    {
        return saleExcelClient.userSelectByExcelId(excelId);
    }


    @RequestMapping(value = "/product/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData productQuery(@RequestBody Map<String,Object> map)
    {
        return saleExcelClient.productQuery(map);
    }

    @RequestMapping(value = "/product/upload" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData productUpload(@RequestBody Map<String,Object> map)
    {
        return saleExcelClient.productUpload(map);
    }

    @RequestMapping(value = "/product/deleteError" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData productDeleteError(@RequestParam("excelId") String excelId)
    {
        return saleExcelClient.productDeleteError(excelId);
    }
    @RequestMapping(value = "/product/selectByExcelId" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData productSelectByExcelId(@RequestParam("excelId") String excelId)
    {
        return saleExcelClient.productSelectByExcelId(excelId);
    }
}
