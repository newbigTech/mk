package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IDroolsExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * hap商品导入调用controller
 * Created by shanks on 2017/3/22.
 */
@RestController
@RequestMapping(value = "/h/sale/excel", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HSaleExcelController {
    @Autowired
    private IDroolsExcelService droolsExcelService;

    /**
     * 分页查询 根据type（用途）、名称查询导入的Excel文件描述
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByCondition", method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
        return droolsExcelService.queryByCondition(map);
    }

    /**
     * 该方法未使用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/query", method = RequestMethod.POST)
    public ResponseData userQuery(@RequestBody Map<String, Object> map) {
        return droolsExcelService.queryUserByExcelId(map);
    }

    /**
     * 用户信息导入 该方法未使用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/upload", method = RequestMethod.POST)
    public ResponseData userUpload(@RequestBody Map<String, Object> map) {
        return droolsExcelService.insertUserExcel(map);
    }

    /**
     * 删除用户导入的错误记录
     *
     * @param excelId
     */
    @RequestMapping(value = "/user/deleteError", method = RequestMethod.GET)
    public ResponseData userDeleteError(@RequestParam String excelId) {
        droolsExcelService.deleteErrorUserExcel(excelId);
        return new ResponseData();
    }

    /**
     * 根据主键查询 从该excel中导入的用户id 未使用
     *
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/user/selectByExcelId", method = RequestMethod.GET)
    public ResponseData userSelectByExcelId(@RequestParam String excelId) {
        ResponseData responseData = droolsExcelService.selectUserByExcelId(excelId);
        List<String> userIds = new ArrayList<>();
        List<Map> mapList = (List<Map>) responseData.getResp();
        if (mapList != null) {
            for (Map map : mapList) {
                userIds.add(map.get("userId").toString());
            }
        }
        return new ResponseData(userIds);
    }


    /**
     * 根据excelId查询成功导入的产品数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/query", method = RequestMethod.POST)
    public ResponseData productQuery(@RequestBody Map<String, Object> map) {
        return droolsExcelService.queryProductByExcelId(map);
    }

    /**
     * 导入用户信息到Excel:Product表中 同时记录excel文件信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/upload", method = RequestMethod.POST)
    public ResponseData productUpload(@RequestBody Map<String, Object> map) {
        return droolsExcelService.insertProductExcel(map);
    }

    /**
     * 删除商品导入信息错误记录
     *
     * @param excelId
     */
    @RequestMapping(value = "/product/deleteError", method = RequestMethod.GET)
    public ResponseData productDeleteError(@RequestParam String excelId) {
        droolsExcelService.deleteErrorProductExcel(excelId);
        return new ResponseData();
    }

    /**
     * 根据成功导入的商品excel的ID查询导入的商品ID
     *
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/product/selectByExcelId", method = RequestMethod.GET)
    public ResponseData productSelectByExcelId(@RequestParam String excelId) {
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
