package com.hand.hmall.client.sale.impl;

import com.hand.hmall.client.sale.ISaleExcelClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 */
public class SaleExcelClientImpl implements ISaleExcelClient {
    @Override
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData userQuery(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData userUpload(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData userDeleteError(@RequestParam String excelId) {
        return null;
    }

    @Override
    public ResponseData userSelectByExcelId(@RequestParam("excelId") String excelId) {
        return null;
    }

    @Override
    public ResponseData productQuery(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData productUpload(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseData productDeleteError(@RequestParam("excelId") String excelId) {
        return null;
    }

    @Override
    public ResponseData productSelectByExcelId(@RequestParam("excelId") String excelId) {
        return null;
    }
}
