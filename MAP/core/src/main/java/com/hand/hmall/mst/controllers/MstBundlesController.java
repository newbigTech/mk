package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.ExcelUtil;
import com.hand.hmall.mst.dto.MstBundles;
import com.hand.hmall.mst.service.IMstBundlesService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author heng.zhang04@hand-china.com
 * @version 0.1
 * @name ImAtpBomSourceController
 * @description 商品套装controller
 * @date 2017/08/31
 */
@Controller
public class MstBundlesController extends BaseController {

    @Autowired
    private IMstBundlesService service;


    @RequestMapping(value = "/hmall/mst/bundles/query")
    @ResponseBody
    public ResponseData query(MstBundles dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/bundles/endUsing")
    @ResponseBody
    public ResponseData endUsing(@RequestBody List<MstBundles> MstBundlesList, BindingResult result, HttpServletRequest request) {
        getValidator().validate(MstBundlesList, result);
        ResponseData rsd = new ResponseData();
        boolean flag = true;
        if (result.hasErrors()) {
            rsd.setMessage(getErrorMessage(result, request));
            rsd.setSuccess(false);
            return rsd;
        }
        /**
         * 将状态更新为停用，并将数据同步到远端接口
         */
        try {
            service.batchEndUsing(MstBundlesList);
        } catch (Exception e) {
            flag = false;
            rsd.setMessage(e.getMessage());
        }
        rsd.setSuccess(flag);
        return rsd;
    }


    /**
     * 启用套装
     *
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/bundles/startUsing")
    @ResponseBody
    public ResponseData start(@RequestBody List<MstBundles> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        ResponseData rsd = new ResponseData();
        boolean flag = true;
        if (result.hasErrors()) {
            rsd.setMessage(getErrorMessage(result, request));
            rsd.setSuccess(false);
            return rsd;
        }
        /**
         * 将状态更新为停用，并将数据同步到远端接口
         */
        try {
            service.batchstartUsing(dto);
        } catch (Exception e) {
            flag = false;
            rsd.setMessage(e.getMessage());
        }
        rsd.setSuccess(flag);
        return rsd;

    }

    /**
     * 套裝头信息的修改
     *
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/bundles/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MstBundles> dto, BindingResult result, HttpServletRequest request) {
        ResponseData rsd = new ResponseData();
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            rsd.setSuccess(false);
            rsd.setMessage(getErrorMessage(result, request));
            return rsd;
        }
        try {
            dto = service.batchUpdateData(dto);
            rsd.setSuccess(true);
            rsd.setRows(dto);
            rsd.setMessage("提交成功");
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }

        return rsd;


    }

    @RequestMapping(value = "/hmall/mst/bundles/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstBundles> dto) {
        ResponseData rsd = new ResponseData();

        try {
            service.batchDeleteData(dto);
            rsd.setSuccess(true);
            rsd.setMessage("刪除成功！");
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }

        return rsd;
    }

    /**
     * 查找计算商品套装数据
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/bundles/queryData")
    @ResponseBody
    public ResponseData queryData(MstBundles dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectMappingData(requestContext, dto));
    }

    /***
     *
     * 模板下载
     * 2017/08/31
     * @param response
     * @param request
     * @throws IOException
     * @author hengzhang04@hand-china.com
     */
    @RequestMapping(value = {"/hmall/mst/bundles/downloadBundles"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String sheetName = "捆绑套装导入";//sheet名
        String fileName = "捆绑套装导入.xlsx";//文件名
        String[] nameArray = new String[]{"套件编码", "套件名称", "套件描述", "套件优先级", "是否叠加", "套件价格", "套件组成", "套件数量",
                "状态"};//字段数据，模板数据
        int[] lengthArray = new int[]{20, 20, 20, 20, 20, 20, 20, 20, 20};//表格中对应字段宽度
        /**简单下拉框的生成**/
        HashMap<Integer, String[]> dropDownMap = new HashMap();
        String statusRelation[] = new String[]{"ACTIVITY"};//下拉框内容
        String isOverLayRelation[] = new String[]{"Y", "N"};
        dropDownMap.put(8, statusRelation);//map中下拉框生成位置下标作为key，下拉内容作为value
        dropDownMap.put(4, isOverLayRelation);
        /***生成excel模板***/
        ExcelUtil.creatExecelTemplate(sheetName, fileName, nameArray, lengthArray, dropDownMap, response, request);
    }

    /**
     * 文件上传
     *
     * @param request
     * @param files
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @author hengzhang04@hand-china.com
     * <p>
     * 2017/8/30
     */
    @RequestMapping(value = {"/hmall/mst/bundles/uploadBundles"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        IRequest requestContext = this.createRequestContext(request);
        ResponseData responseData1 = new ResponseData();

        try {
            //调用excel工具类对传入文件进行处理
            ArrayList excelList = ExcelUtil.FilesAnalysis(files);
            if (excelList == null) {
                responseData1.setMessage(files.getOriginalFilename() + "是一个空文件！");
                responseData1.setSuccess(false);
            } else {
                this.service.insertAllValue(requestContext, excelList);
                responseData1.setSuccess(true);
            }

        } catch (RuntimeException e1) {
            e1.printStackTrace();
            responseData1.setMessage("导入失败！出现异常错误:" + e1.getLocalizedMessage());
            responseData1.setSuccess(false);
        } catch (Exception e) {
            responseData1.setMessage(e.getMessage());
            responseData1.setSuccess(false);

        }

        return responseData1;
    }


}