package com.hand.hap.rm.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImItemRelateRest;
import com.hand.hap.intergration.annotation.HapInbound;
import com.hand.hap.rm.dto.RmItemRelateB;
import com.hand.hap.rm.service.IRmItemRelateBService;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.util.ExcelUtil;
import com.hand.hap.util.ResponseMesg;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
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
import java.util.*;

/**
 * @author heng.zhang@hand-china.com
 * @version 0.1
 * @name RmItemRelateBController
 * @description 选配关系维护类
 * @date 2017/05/25
 */
@Controller
public class RmItemRelateBController extends BaseController {

    @Autowired
    private IRmItemRelateBService service;

    /**
     * 选配关系维护自带查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/rm/item/relate/query")
    @ResponseBody
    public ResponseData query(RmItemRelateB dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectRelationHeader(requestContext, dto, page, pageSize));
    }


    /***
     * 关系保存
     * @param request
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hap/rm/item/relate/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<RmItemRelateB> dto) throws Exception {
        IRequest requestCtx = createRequestContext(request);
        boolean flag = true;
        ResponseData rsd = new ResponseData();
        List<RmItemRelateB> itemRelateBs = new ArrayList<RmItemRelateB>();
        try {
            itemRelateBs = service.batchSubmit(requestCtx, dto);
        } catch (Exception exp) {
            rsd.setMessage(exp.getMessage());
            flag = false;
        }
        if (flag == true) {
            rsd.setMessage("保存成功");
        }
        rsd.setSuccess(flag);
        rsd.setRows(itemRelateBs);
        return rsd;

    }


    /***
     *
     * 模板下载
     * 2017/5/30
     * @param response
     * @param request
     * @throws IOException
     * @author hengzhang04@hand-china.com
     */
    @RequestMapping(value = {"/hap/rm/item/relate/download"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String sheetName = "选配约束关系导入";//sheet名
        String fileName = "选配关系结果导入.xlsx";//文件名
        String[] nameArray = new String[]{"平台编码", "可配置包编码", "配置属性代码", "配置属性值代码", "关联配置包编码",
                "关联配置属性代码", "关联配置属性值代码", "关系类型代码"};//字段数据，模板数据
        int[] lengthArray = new int[]{20, 20, 20, 20, 20, 20, 20, 10};//表格中对应字段宽度
        /**简单下拉框的生成**/
        HashMap<Integer, String[]> dropDownMap = new HashMap();
        String relation[] = new String[]{"CAN", "NOT", "AUTO"};//下拉框内容
        dropDownMap.put(nameArray.length - 1, relation);//map中下拉框生成位置下标作为key，下拉内容作为value

        /***生成excel模板***/
        ExcelUtil.creatExecelTemplate(sheetName, fileName, nameArray, lengthArray, dropDownMap, response, request);
    }


    /**
     * 导出选配关系数据至M3D（增量导出）
     *
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/08
     */
    @RequestMapping(value = "/hap/im/bom/inface/exportItemRelate")
    @ResponseBody
    public ResponseData wsExportImBom(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);

        try {
            service.exportItemRelate(requestCtx);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }


    /**
     * 查询选配关系头（关联）(旧)
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/20
     */
    @RequestMapping(value = "/hap/rm/item/relate/queryRelationHeader")
    @ResponseBody
    public ResponseData queryRelationHeader(RmItemRelateB dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto.setItemId(null);
        return new ResponseData(service.selectRelationHeader(requestContext, dto, page, pageSize));
    }

    /**
     * 查询选配关系头（关联）
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @author heng.zhang04@hand-china.com
     * @date 2017/08/17
     */
    @RequestMapping(value = "/hap/rm/item/relate/queryRelationHeaderB")
    @ResponseBody
    public ResponseData queryRelationHeaderB(RmItemRelateB dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectRelationHeader(requestContext, dto, page, pageSize));
    }


    /***
     * 选配关系头保存
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/21
     * @param request
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hap/rm/item/relate/submitRelation")
    @ResponseBody
    public ResponseData submitRelation(HttpServletRequest request, @RequestBody List<RmItemRelateB> dto) throws Exception {
        IRequest requestCtx = createRequestContext(request);
        boolean flag = true;
        ResponseData rsd = new ResponseData();
        List<RmItemRelateB> itemRelateBs = new ArrayList<RmItemRelateB>();
        try {
            itemRelateBs = service.batchSubmit(requestCtx, dto);
        } catch (Exception exp) {
            rsd.setMessage(exp.getMessage());
            flag = false;
        }
        if (flag == true) {
            rsd.setMessage("保存成功");
        }
        rsd.setSuccess(flag);
        rsd.setRows(itemRelateBs);
        return rsd;
    }


    /**
     * 物料关系文件上传
     *
     * @param request
     * @param files
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/22
     */
    @RequestMapping(value = {"/hap/rm/item/relate/uploadRelation"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData uploadRelation(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        IRequest requestContext = this.createRequestContext(request);
        String fileName = files.getOriginalFilename();
        XSSFWorkbook wb = new XSSFWorkbook(files.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);
        ResponseData responseData = new ResponseData();
        XSSFRow nRow = null;
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
            //map存值，根据序号分组存选配关系，序号一样的存一组
            Map<String, List<List<String>>> excelMap = new HashMap<>();

            //存平台编码和对应的关系组map
            Map<String, Set<String>> platformMap = new HashedMap();

            for (int rData = beginRowNo; rData <= endRowNo; ++rData) {
                nRow = sheet.getRow(rData);
                //校验文件中的空行，如果全为空，则跳出这次循环
                if (ExcelUtil.ifNullCol(nRow)) {
                    continue;
                }
                //关联序号
                String relationNum;
                if (nRow.getCell(4) == null) {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行序号不能为空，请确认导入数据!");
                    return rd;
                }
                nRow.getCell(4).setCellType(1);
                if (StringUtils.isNotBlank(nRow.getCell(4).getStringCellValue())) {
                    //取关联序号
                    relationNum = nRow.getCell(4).getStringCellValue();
                } else {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行序号不能为空，请确认导入数据!");
                    return rd;
                }

                //平台编码
                String platformCode;
                if (nRow.getCell(0) == null) {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行平台编码不能为空，请确认导入数据!");
                    return rd;
                }
                nRow.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(nRow.getCell(0).getStringCellValue())) {
                    //取平台编码
                    platformCode = nRow.getCell(0).getStringCellValue();
                } else {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行平台编码不能为空，请确认导入数据!");
                    return rd;
                }

                //选配关系组
                String relateGroup;
                if (nRow.getCell(10) == null) {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行选配关系组不能为空，请确认导入数据!");
                    return rd;
                }
                nRow.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(nRow.getCell(10).getStringCellValue())) {
                    //取选配关系组
                    relateGroup = nRow.getCell(10).getStringCellValue();
                } else {
                    ResponseData rd = new ResponseData();
                    rd.setSuccess(false);
                    rd.setMessage("第" + rData + "行选配关系组不能为空，请确认导入数据!");
                    return rd;
                }

                //如果存在的话做新增
                if (platformMap.containsKey(platformCode)) {
                    Set<String> relationGroupSet = platformMap.get(platformCode);
                    relationGroupSet.add(relateGroup);
                    platformMap.put(platformCode, relationGroupSet);
                } else {
                    Set<String> relationGroupSet = new HashSet<>();
                    relationGroupSet.add(relateGroup);
                    platformMap.put(platformCode, relationGroupSet);
                }

                //判断该序号是否已经存在，存在的话在该序号上做新增
                if (excelMap.containsKey(relationNum)) {
                    List<List<String>> relationList = excelMap.get(relationNum);
                    List<String> colList = new ArrayList();
                    //获取该行所有列数据集合
                    colList = insertColList(nRow, endColNo);

                    relationList.add(colList);
                    excelMap.put(relationNum, relationList);
                } else {
                    List<List<String>> relationList = new ArrayList<>();
                    List<String> colList = new ArrayList();
                    //获取该行所有列数据集合
                    colList = insertColList(nRow, endColNo);

                    relationList.add(colList);
                    excelMap.put(relationNum, relationList);
                }
            }

            ResponseData rd = new ResponseData();

            try {
                this.service.importExcel(requestContext, excelMap, platformMap);
                rd.setSuccess(true);
            } catch (RuntimeException e) {
                rd.setMessage("导入失败！出现异常错误:" + e.getLocalizedMessage());
                rd.setSuccess(false);
            } catch (Exception e) {
                rd.setMessage(e.getMessage());
                rd.setSuccess(false);
            } finally {
                wb.close();
            }

            return rd;
        }
    }

    /***
     * 选配关系模板下载
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/22
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = {"/hap/rm/item/relate/downloadRelate"}, method = {RequestMethod.GET})
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("选配约束关系导入");
        XSSFRow nRow = null;
        Cell nCell = null;
        XSSFCellStyle nStyle = wb.createCellStyle();
        XSSFFont nFont = wb.createFont();
        byte rowNo = 0;
        int colNo = 0;
        int var15 = rowNo + 1;
        nRow = sheet.createRow(rowNo);
        short colorYello = IndexedColors.YELLOW.getIndex();
        String[] nameArray = new String[]{"平台编码", "可配置包编码", "配置属性代码", "配置属性值代码", "关联序号", "关联配置包编码",
                "关联配置属性代码", "关联配置属性值代码", "关系类型代码", "类型代码", "选配关系组"};//字段数据
        int[] lengthArray = new int[]{20, 20, 20, 20, 20, 10, 20, 20, 20, 10, 20};//表格中对应字段宽度


        for (int os = 0; os < nameArray.length; ++os) {
            sheet.setColumnWidth(os, lengthArray[os] * 272);
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(nameArray[os]);
            nCell.setCellStyle(ExcelUtil.title(wb, nStyle, nFont, colorYello));
            if (os == nameArray.length - 3) {
                //关系类型代码生成下拉框
                String relation[] = new String[]{"CAN", "NOT", "AUTO"};//
                DataValidation dropDownList = ExcelUtil.setDropDownList(sheet, 1, 500, os, os, relation, "请选择正确的关系!");
                sheet.addValidationData(dropDownList);
            } else if (os == nameArray.length - 2) {
                //类型代码生成下拉框
                String typeCode[] = new String[]{"ONE_TO_ONE", "MANY_TO_ONE"};
                DataValidation dropDownList = ExcelUtil.setDropDownList(sheet, 1, 500, os, os, typeCode, "请选择正确的关系!");
                sheet.addValidationData(dropDownList);
            }

        }

        ByteArrayOutputStream var16 = new ByteArrayOutputStream();
        wb.write(var16);
        ExcelUtil.download(var16, response, request, "选配关系结果导入.xlsx");
    }


    /**
     * 把一行数据的需要列数据放在一个集合
     *
     * @param nRow
     * @param endColNo
     * @return
     */
    public List<String> insertColList(XSSFRow nRow, short endColNo) {
        ArrayList colList = new ArrayList();
        Cell nCell = null;

        for (int j = 0; j < endColNo; ++j) {
            nCell = nRow.getCell(j);
            if (nCell != null) {
                nCell.setCellType(1);
                if (StringUtils.isNotBlank(nCell.getStringCellValue())) {
                    colList.add(nCell.getStringCellValue());//拿到文件单行数据
                } else {
                    colList.add("");
                }
            } else {
                colList.add("");
            }
        }
        return colList;
    }

    /**
     * MAP-M3D指定平台的约束关系全量数据接口
     * heng.zhang04@hand-china.com
     * 2017/8/7
     *
     * @return
     */
    @RequestMapping(value = "api/public/hap/rm/item/relate/queryRelateData")
    @ResponseBody
    @HapInbound(
            apiName = "M3D2MAP-RELATE"
    )
    public ResponseData queryData(@RequestBody ImItemRelateRest itemRelateRest, HttpServletRequest request) {
        boolean flag = true;
        ResponseData rsd = new ResponseData();
        List<ImItemRelateRest> itemRelateRestList = null;
        try {
            itemRelateRestList = service.getRelateData(itemRelateRest);
        } catch (RuntimeException rte) {
            rsd.setCode(ResponseMesg.SYS_ABNORMALITY_CODE);//系统异常--未知异常
            rsd.setMessage(ResponseMesg.SYS_ABNORMALITY_INFO);
            flag = false;
        } catch (Exception exp) {
            rsd.setCode(ResponseMesg.PARAMETER_ABNORMALITY_CODE);//自定义异常---目前默认都是参数异常
            rsd.setMessage(ResponseMesg.PARAMETER_ABNORMALITY_INFO + ":" + exp.getMessage());
            flag = false;
        }
        rsd.setSuccess(flag);
        long n = 0L;
        if (flag == true) {
            rsd.setCode(ResponseMesg.SUCCESS_CODE);//
            rsd.setMessage(ResponseMesg.SUCCESS_INFO);
            n = itemRelateRestList.size();
            rsd.setRows(itemRelateRestList);
        }

        rsd.setTotal(n);

        return rsd;
    }
}