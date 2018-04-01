package com.hand.hmall.service.impl;

import com.hand.hmall.dao.DroolsExcelDao;
import com.hand.hmall.dao.DroolsProductDao;
import com.hand.hmall.dao.DroolsUserDao;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IDroolsExcelService;
import com.hand.hmall.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
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
    @Autowired
    private DroolsUserDao droolsUserDao;

    /**
     * 用户信息导入 未使用
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData insertUserExcel(Map<String, Object> map) {
        String excelId = UUID.randomUUID().toString();
        String excelName = map.get("excelName").toString();
        List<Map<String, Object>> userList = (List<Map<String, Object>>) map.get("userList");

        Map<String, Object> droolsExcel = new HashMap<>();
        droolsExcel.put("id", excelId);
        droolsExcel.put("excelName", excelName);
        droolsExcel.put("type", "USER");
        droolsExcelDao.add(droolsExcel);

        Long importDate = System.currentTimeMillis();
        for (Map<String, Object> user : userList) {
            user.put("id", UUID.randomUUID().toString());
            user.put("excelId", excelId);
            user.put("importDate", importDate);
            droolsUserDao.add(user);
        }
        ResponseData responseData = new ResponseData();
        responseData.setMsgCode(excelId);
        return responseData;
    }

    /**
     * 删除用户导入的错误记录
     * @param excelId
     */
    @Override
    public void deleteErrorUserExcel(String excelId) {
        List<Map<String, ?>> userErrorList = droolsUserDao.queryByExcelIdAndIsSuccess(excelId, "N");
        if (userErrorList != null) {
            for (Map<String, ?> userError : userErrorList) {
                droolsUserDao.delete(userError.get("id").toString());
            }
        }
    }

    /**
     * 根据excel主键查询 从该excel中成功导入的用户id 未使用
     * @param excelId
     * @return
     */
    @Override
    public ResponseData selectUserByExcelId(String excelId) {
        return new ResponseData(droolsUserDao.queryByExcelIdAndIsSuccess(excelId, "Y"));
    }

    /**
     * 分页查询 根据type（用途）、名称查询导入的Excel文件描述
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByCondition(Map<String, Object> map) {
        ResponseData responseData = new ResponseData();
        PagedValues pagedValues = droolsExcelDao.queryByCondition(map);
        responseData.setTotal((int) pagedValues.getTotal());
        responseData.setResp(pagedValues.getValues());
        return responseData;
    }

    /**
     * 该方法未使用
     * @param map
     * @return
     */
    @Override
    public ResponseData queryUserByExcelId(Map<String, Object> map) {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        String excelId = map.get("excelId").toString();
        PagedValues pagedValues = droolsUserDao.queryByExcelId(excelId, page, pageSize);
        ResponseData responseData = new ResponseData();
        responseData.setTotal((int) pagedValues.getTotal());
        responseData.setResp(pagedValues.getValues());
        return responseData;
    }

    /**
     * 导入用户信息到Excel:Product表中 同时记录excel文件信息
     * @param map
     * @return
     */
    @Override
    public ResponseData insertProductExcel(Map<String, Object> map) {
        String excelName = map.get("excelName").toString();
        Long importDate = System.currentTimeMillis();
        List<Map<String, Object>> productList = (List<Map<String, Object>>) map.get("productList");
        //查询文件是否已导入
        List<Map<String, ?>> isExist = droolsExcelDao.queryByExcelName(map);
        //isExist不为空，文件已导入到数据库 更新
        if (!CollectionUtils.isEmpty(isExist)) {
            Map<String, Object> droolsExcel = (Map<String, Object>) isExist.get(0);
            String excelId = (String) droolsExcel.get("id");
            droolsExcel.put("excelName", excelName);
            droolsExcel.put("type", "PRODUCT");
            //更新excel记录
            droolsExcelDao.update(droolsExcel);
            //删除原数据关联的商品数据
            List<Map<String, ?>> deleteList = droolsProductDao.queryByExcelIdForDelete((String) droolsExcel.get("id"));
            deleteList.forEach(delete -> {
                droolsProductDao.delete(delete.get("id").toString());
            });
            //插入新的商品数据

            for (Map<String, Object> product : productList) {
                if (product.get("productId") != null || product.get("productCode") != null || product.get("name") != null) {
                    product.put("id", UUID.randomUUID().toString());
                    product.put("excelId", excelId);
                    product.put("importDate", DateUtil.formMillstToDate(importDate, "yyyy-MM-dd HH:mm:ss"));
                    droolsProductDao.add(product);
                }
            }
            ResponseData responseData = new ResponseData();
            responseData.setMsgCode(excelId);
            return responseData;
        } else {
            //文件不存在，直接导入
            String excelId = UUID.randomUUID().toString();
            Map<String, Object> droolsExcel = new HashMap<>();
            droolsExcel.put("id", excelId);
            droolsExcel.put("excelName", excelName);
            droolsExcel.put("type", "PRODUCT");
            //插入excel信息
            droolsExcelDao.add(droolsExcel);
            //插入商品关联信息
            for (Map<String, Object> product : productList) {
                if (product.get("productId") != null || product.get("productCode") != null || product.get("name") != null) {
                    product.put("id", UUID.randomUUID().toString());
                    product.put("excelId", excelId);
                    product.put("importDate", DateUtil.formMillstToDate(importDate, "yyyy-MM-dd HH:mm:ss"));
                    droolsProductDao.add(product);
                }
            }
            ResponseData responseData = new ResponseData();
            responseData.setMsgCode(excelId);
            return responseData;
        }


    }

    /**
     * 删除商品导入信息错误记录
     * @param excelId
     */
    @Override
    public void deleteErrorProductExcel(String excelId) {
        List<Map<String, ?>> userErrorList = droolsProductDao.queryByExcelIdAndIsSuccess(excelId, "N");
        if (userErrorList != null) {
            for (Map<String, ?> userError : userErrorList) {
                droolsProductDao.delete(userError.get("id").toString());
            }
        }
    }

    /**
     * 根据成功导入的商品excel的ID查询导入的商品ID
     * @param excelId
     * @return
     */
    @Override
    public ResponseData selectProductByExcelId(String excelId) {
        return new ResponseData(droolsProductDao.queryByExcelIdAndIsSuccess(excelId, "Y"));
    }

    /**
     * 根据excelId查询成功导入的产品数据
     * @param map
     * @return
     */
    @Override
    public ResponseData queryProductByExcelId(Map<String, Object> map) {
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        String excelId = map.get("excelId").toString();
        PagedValues pagedValues = droolsProductDao.queryByExcelId(excelId, page, pageSize);
        ResponseData responseData = new ResponseData();
        responseData.setTotal((int) pagedValues.getTotal());
        responseData.setResp(pagedValues.getValues());
        return responseData;
    }
}
