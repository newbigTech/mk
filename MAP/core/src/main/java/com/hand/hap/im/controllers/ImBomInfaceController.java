package com.hand.hap.im.controllers;
/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImBomInfaceController
 * @description BOM接口Controller
 * @date 2017/5/27
 */

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.common.util.BeanUtilsExtends;
import com.hand.hap.core.IRequest;
import com.hand.hap.excel.dto.ColumnInfo;
import com.hand.hap.excel.dto.ExportConfig;
import com.hand.hap.excel.service.IExportService;
import com.hand.hap.im.dto.ImBomInface;
import com.hand.hap.im.dto.ImBomInfaceDto;
import com.hand.hap.im.service.IImBomInfaceService;
import com.hand.hap.intergration.annotation.HapInbound;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.util.ExcelUtil;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImBomInfaceController extends BaseController {

    @Autowired
    IExportService excelService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private IImBomInfaceService iImBomInfaceService;

    /**
     * bom接口数据查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/bom/inface/query")
    @ResponseBody
    public ResponseData query(ImBomInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(iImBomInfaceService.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 文件上传
     *
     * @param request
     * @param files
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/08
     */
    @RequestMapping(value = {"/hap/im/item/inface/upload"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        IRequest requestContext = this.createRequestContext(request);
        String fileName = files.getOriginalFilename();
        XSSFWorkbook wb = new XSSFWorkbook(files.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);
        ResponseData responseData = new ResponseData();
        XSSFRow nRow = null;
        Cell nCell = null;
        byte beginRowNo = 1;//定义拿取数据的起始行0开始
        int endRowNo = sheet.getLastRowNum();//拿到文件中数据总条数
        nRow = sheet.getRow(0);
        short endColNo = nRow.getLastCellNum();
        if (endRowNo == 0) {
            responseData.setMessage(fileName + "是一个空文件！");
            responseData.setSuccess(false);
            wb.close();
            return responseData;
        } else {
            ArrayList excelList = new ArrayList();

            for (int rData = beginRowNo; rData <= endRowNo; ++rData) {
                nRow = sheet.getRow(rData);
                ArrayList e = new ArrayList();

                for (int j = 0; j < endColNo; ++j) {
                    nCell = nRow.getCell(j);
                    if (nCell != null) {
                        nCell.setCellType(1);
                        if (StringUtils.isNotBlank(nCell.getStringCellValue())) {
                            e.add(nCell.getStringCellValue());//拿到文件单行数据
                        } else {
                            e.add("");
                        }
                    } else {
                        e.add("");
                    }
                }

                excelList.add(e);//拿到文件中所有的值
            }

            ResponseData response = new ResponseData();

            ResponseData responseDa;
            try {
                iImBomInfaceService.insertImBom(requestContext, excelList);
                iImBomInfaceService.insertImPlatform();
                return new ResponseData(true);
            } catch (RuntimeException var21) {
                response.setMessage("导入失败！出现异常错误:" + var21.getLocalizedMessage());
                response.setSuccess(false);
                responseDa = response;
            } catch (Exception var22) {
                var22.printStackTrace();
                response.setMessage(var22.getMessage());
                response.setSuccess(false);
                responseDa = response;
                return responseDa;
            } finally {
                wb.close();
            }

            return responseDa;
        }
    }

    /**
     * 模板下载
     *
     * @param response
     * @param request
     * @throws IOException
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/08
     */
    @RequestMapping(value = {"/hap/im/item/inface/download"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {

        ExcelUtil excelUtil = new ExcelUtil();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("模板导入");
        XSSFRow nRow = null;
        Cell nCell = null;
        XSSFCellStyle nStyle = wb.createCellStyle();
        XSSFFont nFont = wb.createFont();
        byte rowNo = 0;
        int colNo = 0;
        int var15 = rowNo + 1;
        nRow = sheet.createRow(rowNo);
        short colorYello = IndexedColors.YELLOW.getIndex();
        String[] nameArray = new String[]{"ECN号", "发布号", "项目号", "状态", "物料号",
                "工厂", "BOM 用途", "可选的 BOM", "基本数量", "BOM 状态", "项目类别", "项目号", "组件（BOM）",
                "组件数量", "组件计量单位", "项目文本行1", "项目文本行 2", "部件废品(%)", "替代项目组", "优先级", "使用可能性",
                "尺寸1", "尺寸2", "大小3", "生产订单的发货地点", "排序字符串", "OD,相关性", "毛料SIZE", "写入日期", "写入时间"};//字段数据
        int[] lengthArray = new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};//表格中对应字段宽度

        for (int os = 0; os < nameArray.length; ++os) {
            sheet.setColumnWidth(os, lengthArray[os] * 272);
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(nameArray[os]);
            nCell.setCellStyle(excelUtil.title(wb, nStyle, nFont, colorYello));
        }

        ByteArrayOutputStream var16 = new ByteArrayOutputStream();
        wb.write(var16);
        excelUtil.download(var16, response, request, "主数据BOM导入模板.xlsx");
    }

    /**
     * 导出BOM数据至M3D（增量导出）
     *
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/08
     */
    @RequestMapping(value = "/hap/im/bom/inface/exportImBom")
    @ResponseBody
    @HapInbound(
            apiName = "M3D2MAP-BOM"
    )
    public ResponseData wsExportImBom(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);

        try {
            iImBomInfaceService.wsExportImBom(requestCtx);
        } catch (Exception e) {
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
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/15
     */
    @RequestMapping(value = "/hap/im/bom/inface/export")
    public void createXLS(HttpServletRequest request, @RequestParam String config, HttpServletResponse httpServletResponse) throws IOException {
        IRequest requestContext = this.createRequestContext(request);
        JavaType type = this.objectMapper.getTypeFactory().constructParametrizedType(ExportConfig.class, ExportConfig.class, new Class[]{ImBomInface.class, ColumnInfo.class});
        ExportConfig exportConfig = this.objectMapper.readValue(config, type);
        PaginatedList<ImBomInface> imBomInfaceList = iImBomInfaceService.queryList(requestContext, (ImBomInface) exportConfig.getParam(), 0, 0);
        List<ImBomInfaceDto> boms = BeanUtilsExtends.copyListProperties(imBomInfaceList.getRows(), ImBomInfaceDto.class);
        new com.hand.common.util.ExcelUtil(ImBomInfaceDto.class).exportExcel(boms, "BOM接口数据", 0, request, httpServletResponse, "BOM接口数据导出.xlsx");
    }
}

