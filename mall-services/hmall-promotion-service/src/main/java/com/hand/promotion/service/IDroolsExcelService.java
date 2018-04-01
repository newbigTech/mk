package com.hand.promotion.service;


import com.hand.dto.ResponseData;

import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 */
public interface IDroolsExcelService {

    /**
     * @param map
     * @return
     */
    ResponseData queryByCondition(Map<String, Object> map);

    /**
     * 插入一条导入商品Excel信息
     * @param map
     * @return
     */
    ResponseData insertProductExcel(Map<String, Object> map);

    /**
     * @param excelId
     */
    void deleteErrorProductExcel(String excelId);

    /**
     * @param excelId
     * @return
     */
    ResponseData selectProductByExcelId(String excelId);

    /**
     * @param map
     * @return
     */
    ResponseData queryProductByExcelId(Map<String, Object> map);
}
