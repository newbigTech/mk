package com.hand.hap.im.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImItemInfaceController
 * @description 接口表导入操作controller
 * @date 2017/5/27
 */

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.common.util.BeanUtilsExtends;
import com.hand.hap.core.IRequest;
import com.hand.hap.excel.dto.ColumnInfo;
import com.hand.hap.excel.dto.ExportConfig;
import com.hand.hap.excel.service.IExportService;
import com.hand.hap.im.dto.ImItemDto;
import com.hand.hap.im.dto.ImItemInface;
import com.hand.hap.im.service.IImBomInfaceService;
import com.hand.hap.im.service.IImItemInfaceService;
import com.hand.hap.im.service.IItemInfaceImpExpService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class ImItemInfaceController extends BaseController {

    @Autowired
    IExportService excelService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IImBomInfaceService iImBomInfaceService;
    @Autowired
    private IImItemInfaceService iImItemInfaceService;
    @Autowired
    private IItemInfaceImpExpService itemImpExpService;

    //生成文件目录命名规则
    private static String getSavePathByDate() {
        Calendar cal = Calendar.getInstance();
        String path = cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DAY_OF_MONTH);
        return path;
    }

    /**
     * 将数据写入文件并返回相对路径
     *
     * @param data
     * @param dataType
     * @return
     * @throws IOException
     */
    public static String getSaveDataPath(String data, String dataType, String partNumber, Integer dataSize) throws IOException {
        //文件路径格式/年/月/日/dataType_uuid.txt
        String systemPath = ResourceBundle.getBundle("bussiness", Locale.getDefault()).getString("batchItemBom.errorMessage.path");
        String filePath = systemPath + File.separator + getSavePathByDate() + File.separator + dataType + File.separator + partNumber + "_" + dataSize + "_" + UUID.randomUUID().toString().replace("-", "") + ".txt";
        System.out.println(filePath);
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        IOUtils.write(data, new FileOutputStream(file));
        return filePath;
    }

    /**
     * 物料数据接口查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/item/inface/query")
    @ResponseBody
    public ResponseData query(ImItemInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(iImItemInfaceService.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 物料接口数据提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/im/item/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImItemInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(iImItemInfaceService.batchUpdate(requestCtx, dto));
    }

    /**
     * item接口Excel上传
     *
     * @param request
     * @param file
     * @return
     * @author yougui.wu@hand-china.com
     */
    @RequestMapping(value = "/hap/im/item/inface/itemUploadExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = createRequestContext(request);
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            Map<String, Object> res = itemImpExpService.importItemExcel(iRequest, inputStream, fileName);
            boolean flag = (boolean) res.get("success");
            if (flag) {
                return new ResponseData(true);
            } else {
                List<String> errer = (List<String>) res.get("data");
                ResponseData rd = new ResponseData(false);
                rd.setRows(errer);
                return rd;
            }
        } catch (Exception e) {
            //HmdmBaseController只能捕获ajax请求第异常，这里手动捕获
            e.printStackTrace();
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    /**
     * item接口Excel下载
     *
     * @param request
     * @param response
     * @author yougui.wu@hand-china.com
     */
    @RequestMapping(value = "/hap/im/item/inface/itemDownloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcelTmp(HttpServletRequest request, HttpServletResponse response) {
        IRequest iRequest = createRequestContext(request);
        String fileName = "MAP_ITEM_ATTR.xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            OutputStream os = response.getOutputStream();
            ByteArrayOutputStream batyos = itemImpExpService.exportItemExcel(iRequest, fileName);
            batyos.writeTo(os);
            os.flush();
            os.close();
            batyos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出主数据到M3D（增量导出）
     *
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/08
     */
    @RequestMapping(value = "/hap/im/item/inface/wsExportItem")
    @ResponseBody
    public ResponseData wsExportItem(HttpServletRequest request) {

        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);

        try {
            iImItemInfaceService.wsExportItem(requestCtx);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }


        return responseData;
    }

    /**
     * bomexcel导出
     *
     * @param request
     * @param config
     * @param httpServletResponse
     * @throws IOException
     * @author xiaowei.zhang@hand-china.com
     * @date 2017/06/21
     */
    @RequestMapping(value = "/hap/im/item/inface/export")
    public void createXLS(HttpServletRequest request, @RequestParam String config, HttpServletResponse httpServletResponse) throws IOException {
        IRequest requestContext = this.createRequestContext(request);
        JavaType type = this.objectMapper.getTypeFactory().constructParametrizedType(ExportConfig.class, ExportConfig.class, new Class[]{ImItemInface.class, ColumnInfo.class});
        ExportConfig exportConfig = (ExportConfig) this.objectMapper.readValue(config, type);
        PaginatedList<ImItemInface> imBomInfaceList = iImItemInfaceService.queryList(requestContext, (ImItemInface) exportConfig.getParam(), 0, 0);
        List<ImItemDto> items = BeanUtilsExtends.copyListProperties(imBomInfaceList.getRows(), ImItemDto.class);
        new com.hand.common.util.ExcelUtil(ImItemDto.class).exportExcel(items, "物料接口数据导出", 0, request, httpServletResponse, "物料接口数据导出.xlsx");
    }

    /**
     * 导入item_value Excel下载
     *
     * @param request
     * @param response
     * @author yougui.wu@hand-china.com
     */
    @RequestMapping(value = "/hap/im/item/inface/itemValueDownloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcelItemValue(HttpServletRequest request, HttpServletResponse response) {
        IRequest iRequest = createRequestContext(request);
        String fileName = "MAP_ITEM_VALUE.xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            OutputStream os = null;
            os = response.getOutputStream();
            ByteArrayOutputStream batyos = itemImpExpService.exportItemValueExcel(iRequest, fileName);
            batyos.writeTo(os);
            os.flush();
            os.close();
            batyos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * item接口Excel上传
     *
     * @param request
     * @param file
     * @return
     * @author yougui.wu@hand-china.com
     */
    @RequestMapping(value = "/hap/im/item/inface/itemValueUploadExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadItemValueExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = createRequestContext(request);
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            Map<String, Object> res = itemImpExpService.importItemValueExcel(iRequest, inputStream, fileName);
            boolean flag = (boolean) res.get("success");
            if (flag) {
                return new ResponseData(true);
            } else {
                List<String> errer = (List<String>) res.get("data");
                ResponseData rd = new ResponseData(false);
                rd.setRows(errer);
                return rd;
            }
        } catch (Exception e) {
            //HmdmBaseController只能捕获ajax请求第异常，这里手动捕获
            e.printStackTrace();
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }
}