package com.hand.hap.mdm.item.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.item.dto.Lookup;
import com.hand.hap.mdm.item.dto.LookupCode;
import com.hand.hap.mdm.item.dto.LookupType;
import com.hand.hap.mdm.item.dto.MdmItemValue;
import com.hand.hap.mdm.item.service.ILookupCodeService;
import com.hand.hap.mdm.item.service.ILookupTypeService;
import com.hand.hap.mdm.item.service.IMdmItemValueService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhongxi
 * @version 0.1
 * @name LookupController
 * @description 属性维护界面入口Controller
 * @date 2017/5/24
 */
@Controller
public class LookupController extends BaseController {

    // 属性头service
    @Autowired
    private ILookupTypeService typeService;

    // 属性行service
    @Autowired
    private ILookupCodeService codeService;

    // 属性值service
    @Autowired
    private IMdmItemValueService itemService;


    /**
     * 属性头查询入口
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemType/query")
    @ResponseBody
    public ResponseData query(LookupType dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(typeService.queryAll(requestContext, dto, page, pageSize));
    }

    /**
     * 属性行查询入口
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemCode/queryCode")
    @ResponseBody
    public ResponseData queryCode(LookupCode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(codeService.queryAll(requestContext, dto, page, pageSize));
    }

    /**
     * 属性头更新入口
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemType/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LookupType> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(typeService.batchUpdateType(requestCtx, dto));
    }

    /**
     * 属性头删除入口（标志位删除）
     *
     * @param request
     * @param dto      由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @param page     由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @param pageSize 由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemType/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LookupType> dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        typeService.batchDeleteItem(dto);

        return new ResponseData(typeService.queryAll(requestCtx, new LookupType(), page, pageSize));
    }

    /**
     * 属性头删除CHECK入口
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemType/queryCheckUsed")
    @ResponseBody
    public ResponseData queryCheckUsed(HttpServletRequest request, @RequestBody List<LookupType> dto) {
        IRequest requestCtx = createRequestContext(request);

        // 属性值check结果String
        StringBuffer sbf = new StringBuffer();
        for (LookupType lkt : dto) {
            if (lkt.getHeaderId() != null) {
                List<MdmItemValue> mivList = itemService.queryUsed(lkt.getLookupType());
                if (mivList.size() > 0) {
                    sbf.append(lkt.getLookupType());
                    sbf.append(",");
                }
            }
        }
        // 返回结果编辑
        ResponseData rd = new ResponseData();
        if (!"".equals(sbf.toString())) {
            rd.setSuccess(false);
            rd.setMessage(sbf.toString().substring(0, sbf.toString().length() - 1));
        } else {
            rd.setSuccess(true);
        }
        return rd;
    }

    /**
     * 属性行更新入口
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemCode/submitCode")
    @ResponseBody
    public ResponseData updateCode(HttpServletRequest request, @RequestBody List<LookupCode> dto) {
        IRequest requestCtx = createRequestContext(request);
        // TODO 添加动态生成代码
        for (LookupCode lut : dto) {
            if ("add".equals(lut.get__status())) {
                lut.setLookupCode(lut.getLookupCode().replace("XXXX", ""));
            }
        }
        return new ResponseData(codeService.batchUpdateCode(requestCtx, dto));
    }

    /**
     * 属性行删除入口（标志位删除）
     *
     * @param request
     * @param dto      由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @param page     由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @param pageSize 由于是标志位删除，所以删除后，需要再查询一次，保证正确显示信息，需要查询的相关参数
     * @return
     */
    @RequestMapping(value = "/markor/hand/hap/itemCode/removeCode")
    @ResponseBody
    public ResponseData deleteCode(HttpServletRequest request, @RequestBody List<LookupCode> dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        for (LookupCode lut : dto) {
            lut.setDeleteFlag("Y");
            lut.set__status("update");
        }
        codeService.batchUpdate(requestCtx, dto);

        return new ResponseData(codeService.queryAll(requestCtx, new LookupCode(), page, pageSize));
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "/markor/hand/hap/itemType/downloadExcel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<Lookup> list = typeService.queryForExport();
        new ExcelUtil(Lookup.class).exportExcel(list, "属性及属性值列表", list.size(), request, response, "属性及属性值列表.xlsx");
    }


    /***
     *
     * 模板下载
     * 2017/7/05
     * @param response
     * @param request
     * @throws IOException
     * @author hengzhang04@hand-china.com
     */
    @RequestMapping(value = {"/markor/hand/hap/itemCode/download"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("配置属性值导入");
        String[] nameArray = new String[]{"属性头代码", "属性值名称", "属性值含义", "属性值描述", "属性值生效日期",
                "属性值失效日期"};//字段数据
        int[] lengthArray = new int[]{20, 20, 20, 20, 20, 20};//表格中对应字段宽度
        XSSFRow nRow = null;
        Cell nCell = null;
        XSSFCellStyle nStyle = wb.createCellStyle();
        XSSFFont nFont = wb.createFont();
        byte rowNo = 0;
        int colNo = 0;
        int var15 = rowNo + 1;
        nRow = sheet.createRow(rowNo);
        short colorYello = IndexedColors.YELLOW.getIndex();


        for (int os = 0; os < nameArray.length; ++os) {
            sheet.setColumnWidth(os, lengthArray[os] * 272);
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(nameArray[os]);
            nCell.setCellStyle(com.hand.hap.util.ExcelUtil.title(wb, nStyle, nFont, colorYello));
        }

        ByteArrayOutputStream var16 = new ByteArrayOutputStream();
        wb.write(var16);
        com.hand.hap.util.ExcelUtil.download(var16, response, request, "配置属性值导入.xlsx");
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
     * 2017/7/05
     */
    @RequestMapping(value = {"/markor/hand/hap/itemCode/upload"}, method = {RequestMethod.POST})
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

                for (int j = 0; j <= endColNo; ++j) {
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

            ResponseData var24 = new ResponseData();

            ResponseData var25;
            try {
                codeService.insertAllValue(requestContext, excelList);
                return new ResponseData(true);
            } catch (RuntimeException var21) {
                var24.setMessage("导入失败！出现异常错误:" + var21.getLocalizedMessage());
                var24.setSuccess(false);
                var25 = var24;
            } catch (Exception var22) {
                var22.printStackTrace();
                var24.setMessage(var22.getMessage());
                var24.setSuccess(false);
                var25 = var24;
                return var25;
            } finally {
                wb.close();
            }

            return var25;
        }
    }

}