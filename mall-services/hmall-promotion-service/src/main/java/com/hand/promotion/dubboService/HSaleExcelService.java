package com.hand.promotion.dubboService;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleDroolsExcelService;
import com.hand.promotion.service.IDroolsExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by darkdog on 2018/2/5.
 * 商品Excel导入接口
 */

public class HSaleExcelService implements ISaleDroolsExcelService {

    @Autowired
    private IDroolsExcelService droolsExcelService;

    /**
     * 分页查询 根据type（用途）、名称查询导入的Excel文件描述
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByCondition(Map<String, Object> map) {
        return droolsExcelService.queryByCondition(map);
    }

    /**
     * 根据excelId查询成功导入的产品数据
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData productQuery(Map<String, Object> map) {
        return droolsExcelService.queryProductByExcelId(map);
    }

    /**
     * 导入用户信息到Excel:Product表中 同时记录excel文件信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData productUpload(Map<String, Object> map) {
        return droolsExcelService.insertProductExcel(map);
    }

    /**
     * 删除商品导入信息错误记录
     *
     * @param excelId
     */
    @Override
    public ResponseData productDeleteError(String excelId) {
        droolsExcelService.deleteErrorProductExcel(excelId);
        return new ResponseData();
    }

    /**
     * 根据成功导入的商品excel的ID查询导入的商品ID
     *
     * @param excelId
     * @return
     */
    @Override
    public ResponseData productSelectByExcelId(String excelId) {
        ResponseData responseData = droolsExcelService.selectProductByExcelId(excelId);
        List<String> productIds = new ArrayList<>();
        List<Map> mapList = (List<Map>) responseData.getResp();
        if (mapList != null) {
            for (Map map : mapList) {
                productIds.add(map.get("productId").toString());
            }
        }
        return new ResponseData(productIds);
    }


}
