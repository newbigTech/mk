package com.hand.hap.mdm.bom.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.mdm.bom.dto.ItemDimensionsPageInfo;
import com.hand.hap.mdm.bom.exception.ItemSizeException;
import com.hand.hap.mdm.bom.service.IItemDimensionsMaintainService;
import com.hand.hap.mdm.item.dto.MdmItemSizeInfo;
import com.hand.hap.system.dto.ResponseData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author chenzhigang@markor.com.cn
 * @version 0.1
 * @name ItemDimensionsMaintainController
 * @description 产品尺寸属性维护控制器
 * @date 2017/6/21
 */
@Controller
public class ItemDimensionsMaintainController {

    @Autowired
    private IItemDimensionsMaintainService service;

    /**
     * 查询并返回全部尺寸属性值信息
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/allSizeAttrs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData allSizeAttrs() {
        return new ResponseData(service.queryAllSizeAttrs());
    }


    /**
     * 保存平台物料包尺寸配置信息
     * @param mdmItemSizeInfos 需要保存的尺寸配置信息列表
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData save(@RequestBody List<MdmItemSizeInfo> mdmItemSizeInfos) {
        try {
            service.save(mdmItemSizeInfos);
            return new ResponseData(mdmItemSizeInfos);
        } catch (RuntimeException e) {
            ResponseData result = new ResponseData(false);
            result.setMessage("可配置包编码错误");
            return result;
        }
    }

    /**
     * 校验可配置包编码合法情况
     * @param mdmItemSizeInfoList 需要校验的尺寸配置信息列表
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/checkItemCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData checkItemCode(@RequestBody List<MdmItemSizeInfo> mdmItemSizeInfoList) {

        List<MdmItemSizeInfo> illegal = service.checkItemCode(mdmItemSizeInfoList);
        if (illegal.isEmpty()) {
            return new ResponseData(true);
        }
        StringBuilder waringMsg = new StringBuilder("不正确的配置包编码:");
        for (MdmItemSizeInfo info : illegal) {
            waringMsg.append(info.getItemCode() + " ");
        }
        ResponseData result = new ResponseData(false);
        result.setMessage(waringMsg.toString());
        return result;

    }


    /**
     * 根据平台ID查询其产品尺寸属性信息
     * @param platformId
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/query/{platformId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData queryPlatformSizeConf(@PathVariable String platformId) {
        List<MdmItemSizeInfo> sizeInfos = service.queryPlatformSizeConf(Long.parseLong(platformId));
        return new ResponseData(sizeInfos);
    }

    /**
     * 更新一条尺寸属性设置
     * @param infoId
     * @param attrCode
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/update/{infoId}/{attrCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData updatePlatformSizeConf(@PathVariable String infoId, @PathVariable String attrCode) {
        service.updatePlatformSizeConf(Long.parseLong(infoId), attrCode);
        return new ResponseData(true);
    }

    /**
     * （逻辑）删除尺寸属性设置信息
     * @param mdmItemSizeInfos
     * @return
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/platformSize/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData deletePlatformSizeConf(@RequestBody List<MdmItemSizeInfo> mdmItemSizeInfos) {
        service.delete(mdmItemSizeInfos);
        return new ResponseData(true);
    }

    /**
     * 导出Excel模板（此模板用于导入尺寸属性设置信息）
     * @param request
     * @param response
     */
    @RequestMapping(value = "/markor/hap/mdm/bom/downloadExcelTemplate", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<ItemDimensionsPageInfo> itemDimensionsPageInfos = new ArrayList<>();
        itemDimensionsPageInfos.add(new ItemDimensionsPageInfo());
        new ExcelUtil(ItemDimensionsPageInfo.class)
                .exportExcel(itemDimensionsPageInfos, ItemDimensionsPageInfo.DEFAULT_SHEET_NAME, 0,
                        request, response, ItemDimensionsPageInfo.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 导入尺寸属性设置信息
     * @param request
     * @param files
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/markor/hap/mdm/bom/itemDimensionsInfo/upload"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {

        // 当前导入的配置信息对应的平台ID
        long currentPlatformId = 0;

        boolean importResult = false;
        String message = "success"; // 方法执行结果消息

        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)
                && (request instanceof MultipartHttpServletRequest)) {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            if (fileNames.hasNext()) {
                // 获得上传文件
                MultipartFile file = multipartRequest.getFile(fileNames.next());
                // 文件输入流
                InputStream is = null;
                try {
                    is = file.getInputStream();
                    // 调用工具类解析上传的Excel文件
                    List<ItemDimensionsPageInfo> infos = new ExcelUtil(ItemDimensionsPageInfo.class)
                            .importExcel(ItemDimensionsPageInfo.DEFAULT_EXCEL_FILE_NAME, ItemDimensionsPageInfo.DEFAULT_SHEET_NAME + "0", is);
                    // 执行导入，并获得当前导入的配置信息对应的平台ID
                    currentPlatformId = service.importItemDimensionsConf(infos);
                    importResult = true;
                } catch (ItemSizeException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错\n" + e.getMessage();
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        ResponseData responseData = new ResponseData(importResult);
        responseData.setMessage(importResult ? (currentPlatformId + "") : message);
        return responseData;
    }
}
