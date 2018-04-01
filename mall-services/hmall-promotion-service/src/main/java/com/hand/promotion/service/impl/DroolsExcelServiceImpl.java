package com.hand.promotion.service.impl;

import com.hand.dto.ResponseData;
import com.hand.promotion.dao.DroolsExcelDao;
import com.hand.promotion.dao.DroolsProductDao;
import com.hand.promotion.pojo.excel.DroolsExcel;
import com.hand.promotion.pojo.excel.DroolsProduct;
import com.hand.promotion.service.IDroolsExcelService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shanks on 2017/3/22.
 */
@Service
public class DroolsExcelServiceImpl implements IDroolsExcelService {

    @Autowired
    private DroolsExcelDao droolsExcelDao;
    @Autowired
    private DroolsProductDao droolsProductDao;


    /**
     * 分页查询 根据type（用途）、名称查询导入的Excel文件描述
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByCondition(Map<String, Object> map) {
        ResponseData responseData = new ResponseData();
        return droolsExcelDao.queryByCondition(map);
    }


    /**
     * 导入用户信息到Excel:Product表中 同时记录excel文件信息
     * @ not test
     * @param map
     * @return
     */
    @Override
    public ResponseData insertProductExcel(Map<String, Object> map) {
        String excelName = map.get("excelName").toString();
        Long importDate = System.currentTimeMillis();
        List<Map<String, Object>> productList = (List<Map<String, Object>>) map.get("productList");
        //查询文件是否已导入
        List<DroolsExcel> isExist = droolsExcelDao.queryByExcelName(map);
        //isExist不为空，文件已导入到数据库 更新
        if (!CollectionUtils.isEmpty(isExist)) {
            DroolsExcel droolsExcel = isExist.get(0);
            String excelId = droolsExcel.getId();
            droolsExcel.setExcelName(excelName);
            droolsExcel.setType("PRODUCT");
            //更新excel记录
            droolsExcelDao.update(droolsExcel);
            //删除原数据关联的商品数据
            List<DroolsProduct> deleteList = droolsProductDao.queryByExcelIdForDelete(droolsExcel.getId());
            deleteList.forEach(delete -> {
                droolsProductDao.delete(delete.getId());
            });
            //插入新的商品数据
            for (Map<String, Object> product : productList) {
                DroolsProduct droolsProduct = BeanMapExchange.mapToObject(product, DroolsProduct.class);
                if (product.get("productId") != null || product.get("productCode") != null || product.get("name") != null) {
                    droolsProduct.setId(UUID.randomUUID().toString());
                    droolsProduct.setExcelId(excelId);
                    droolsProduct.setImportDate(DateFormatUtil.timeStampToString(String.valueOf(importDate)));
                    droolsProductDao.insertPojo(droolsProduct);
                }
            }
            ResponseData responseData = new ResponseData();
            responseData.setMsgCode(excelId);
            return responseData;
        } else {
            //文件不存在，直接导入
            String excelId = UUID.randomUUID().toString();
//            Map<String, Object> droolsExcel = new HashMap<>();
//            droolsExcel.put("id", excelId);
//            droolsExcel.put("excelName", excelName);
//            droolsExcel.put("type", "PRODUCT");
            DroolsExcel droolsExcel = new DroolsExcel();
            droolsExcel.setId(excelId);
            droolsExcel.setExcelName(excelName);
            droolsExcel.setType("PRODUCT");
            //插入excel信息
            droolsExcelDao.insertPojo(droolsExcel);
            //插入商品关联信息
            for (Map<String, Object> product : productList) {
                DroolsProduct droolsProduct = BeanMapExchange.mapToObject(product, DroolsProduct.class);
                if (product.get("productId") != null || product.get("productCode") != null || product.get("name") != null) {
                    droolsProduct.setId(UUID.randomUUID().toString());
                    droolsProduct.setExcelId(excelId);
                    droolsProduct.setImportDate(DateFormatUtil.timeStampToString(String.valueOf(importDate)));
                    droolsProductDao.insertPojo(droolsProduct);
                }
            }
            ResponseData responseData = new ResponseData();
            responseData.setMsgCode(excelId);
            return responseData;
        }


    }

    /**
     * 删除商品导入信息错误记录
     * not test
     * @param excelId
     */
    @Override
    public void deleteErrorProductExcel(String excelId) {
        List<DroolsProduct> userErrorList = droolsProductDao.queryByExcelIdAndIsSuccess(excelId, "N");
        if (userErrorList != null) {
            for (DroolsProduct droolsProduct : userErrorList) {
                droolsProductDao.delete(droolsProduct.getId());
            }
        }
    }

    /**
     * 根据成功导入的商品excel的ID查询导入的商品ID
     * not test
     * @param excelId
     * @return
     */
    @Override
    public ResponseData selectProductByExcelId(String excelId) {
        return new ResponseData(droolsProductDao.queryByExcelIdAndIsSuccess(excelId, "Y"));
    }

    /**
     * 根据excelId查询成功导入的产品数据
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryProductByExcelId(Map<String, Object> map) {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        String excelId = map.get("excelId").toString();
        return droolsProductDao.queryByExcelId(excelId, page, pageSize);
    }
}
