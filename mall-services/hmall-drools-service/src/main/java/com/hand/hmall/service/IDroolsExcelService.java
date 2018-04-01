package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 */
public interface IDroolsExcelService {

    ResponseData queryByCondition(Map<String, Object> map);

    ResponseData insertUserExcel(Map<String, Object> map);
    void deleteErrorUserExcel(String excelId);
    ResponseData selectUserByExcelId(String excelId);
    ResponseData queryUserByExcelId(Map<String, Object> map);

    ResponseData insertProductExcel(Map<String, Object> map);
    void deleteErrorProductExcel(String excelId);
    ResponseData selectProductByExcelId(String excelId);
    ResponseData queryProductByExcelId(Map<String, Object> map);
}
