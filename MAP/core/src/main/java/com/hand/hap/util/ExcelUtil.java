package com.hand.hap.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by heng.zhang04@hand-china.com
 * 16:58 2017/6/1.
 *
 * @description excelUtil
 * hap3.0 excel导入导出 工具类
 */
public class ExcelUtil {
    /**
     * 下载的方法
     *
     * @param byteArrayOutputStream 流
     * @param response              相应
     * @param request               请求
     * @param returnName            文件名称
     * @throws IOException
     * @author hengzhang04@hand-china.com
     * <p>
     * 2017/5/30
     */
    public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response,
                                HttpServletRequest request, String returnName) throws IOException {
        // 处理浏览器差异导致的下载文件名称乱码的问题
        String userAgent = request.getHeader("USER-AGENT");
        String enocodeType;
        if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
            enocodeType = "iso8859-1";
        } else {
            enocodeType = "utf8";
        }

        // response.setContentType("application/octet-stream;charset=utf-8");
        // response.setContentType("application/vnd.ms-excel");//application/octet-stream
        response.setContentType("application/x-download");
        returnName = response.encodeURL(new String(returnName.getBytes(), enocodeType)); // 保存的文件名,必须和页面编码一致,否则乱码
        response.addHeader("Content-Disposition", "attachment;filename=" + returnName);
        response.setContentLength(byteArrayOutputStream.size());

        ServletOutputStream outputstream = response.getOutputStream(); // 取得输出流
        byteArrayOutputStream.writeTo(outputstream); // 写到输出流
        byteArrayOutputStream.close(); // 关闭
        outputstream.flush(); // 刷数据
    }

    /**
     * 下拉
     *
     * @param sheet        sheet对象
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol      范围
     * @param listOfValues 下拉 的内容
     * @param errorStr     错误提示信息
     * @return
     * @author hengzhang04@hand-china.com
     * <p>
     * 2017/5/30
     */
    public static DataValidation setDropDownList(XSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol,
                                                 String[] listOfValues, String errorStr) {
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint constraint = helper.createExplicitListConstraint(listOfValues);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation dropDownList = helper.createValidation(constraint, regions);
        // 设置填写不正确的提示信息
        dropDownList.createErrorBox("error", errorStr);
        dropDownList.setShowErrorBox(true);
        dropDownList.setSuppressDropDownArrow(true);
        return dropDownList;
    }


    /**
     * 样式
     *
     * @param wb
     * @param nStyle     样式
     * @param nFont      字体
     * @param colorValue 背景颜色
     * @return
     * @author hengzhang04@hand-china.com
     * <p>
     * 2017/5/30
     */
    public static CellStyle title(Workbook wb, CellStyle nStyle, Font nFont, short colorValue) {

        nFont.setFontName("宋体");
        nFont.setFontHeightInPoints((short) 10);
        nFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        nStyle.setFillPattern(XSSFCellStyle.FINE_DOTS);
        nStyle.setFillForegroundColor(colorValue);// 设置背景颜色
        nStyle.setFillBackgroundColor(colorValue);
        nStyle.setAlignment(CellStyle.ALIGN_CENTER); // 横向居中
        nStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 纵向居中

        // 表格线
        nStyle.setBorderTop(CellStyle.BORDER_THIN); // 上细线
        nStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下细线
        nStyle.setBorderLeft(CellStyle.BORDER_THIN); // 左细线
        nStyle.setBorderRight(CellStyle.BORDER_THIN); // 右细线

        nStyle.setFont(nFont);
        return nStyle;
    }

    /**
     * 对传入的文件进行解析，并返回List
     * 默认都是String类型的
     * 2017/7/11
     *
     * @param files
     * @return
     */
    public static ArrayList FilesAnalysis(MultipartFile files) throws IOException {
        String fileName = files.getOriginalFilename();
        XSSFWorkbook wb = new XSSFWorkbook(files.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);

        XSSFRow nRow = null;
        Cell nCell = null;
        byte beginRowNo = 1;//定义拿取数据的起始行0开始
        int endRowNo = sheet.getLastRowNum();//拿到文件中数据总条数
        nRow = sheet.getRow(0);
        short endColNo = nRow.getLastCellNum();
        ArrayList excelList;
        if (endRowNo == 0) {
            wb.close();
            return null;
        } else {
            excelList = new ArrayList();
            for (int rData = beginRowNo; rData <= endRowNo; ++rData) {
                nRow = sheet.getRow(rData);
                ArrayList fileList = new ArrayList();
                for (int j = 0; j <= endColNo; ++j) {
                    nCell = nRow.getCell(j);
                    if (nCell != null) {
                        nCell.setCellType(1);
                        if (StringUtils.isNotBlank(nCell.getStringCellValue())) {
                            fileList.add(nCell.getStringCellValue());//拿到文件单行数据
                        } else {
                            fileList.add("");
                        }
                    } else {
                        fileList.add("");
                    }
                }

                excelList.add(fileList);//拿到文件中所有的值
            }
            wb.close();

        }
        return excelList;
    }

    /**
     * 生成execl导入模板
     *
     * @param sheetName   sheet名称
     * @param fileName    文件名（后缀.xlsx）
     * @param nameArray   字段数据，模板数据
     * @param lengthArray 表格中对应字段宽度
     * @param dropDownMap 下拉框数据 map中下拉框生成位置下标作为key，下拉内容作为value
     */
    public static void creatExecelTemplate(String sheetName, String fileName, String[] nameArray, int[] lengthArray, HashMap<Integer, String[]> dropDownMap,
                                           HttpServletResponse response, HttpServletRequest request) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);//sheet名称
        XSSFRow nRow = null;
        Cell nCell = null;
        XSSFCellStyle nStyle = wb.createCellStyle();
        XSSFFont nFont = wb.createFont();
        byte rowNo = 0;
        int colNo = 0;
        nRow = sheet.createRow(rowNo);
        short colorYello = IndexedColors.YELLOW.getIndex();
        for (int os = 0; os < nameArray.length; ++os) {
            sheet.setColumnWidth(os, lengthArray[os] * 272);
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(nameArray[os]);
            nCell.setCellStyle(ExcelUtil.title(wb, nStyle, nFont, colorYello));
            //生成下拉框
            if (dropDownMap.containsKey(os)) {
                DataValidation dropDownList = ExcelUtil.setDropDownList(sheet, 1, 500, os, os, dropDownMap.get(os), "请在下拉框中选择正确的值!");
                sheet.addValidationData(dropDownList);
            }

        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        ExcelUtil.download(byteArrayOutputStream, response, request, fileName);

    }

    /**
     * 校验空行，如果全为空，则返回真
     *
     * @param colList
     * @return
     */
    public static boolean chekckNullCol(List<String> colList) {
        Boolean flag = true;
        for (String str : colList) {
            if (str != null && !"".equals(str)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 校验读取中的excel文件，如果此行为空，则返回真
     *
     * @param nRow
     * @return
     */
    public static boolean ifNullCol(XSSFRow nRow) {
        boolean flag = true;
        int n = nRow.getPhysicalNumberOfCells();
        for (int i = 0; i < n; i++) {
            if (nRow.getCell(i) != null && StringUtils.isNotBlank(getCellStringValue(nRow.getCell(i)))) {
                //取关联序号
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static String getCellStringValue(XSSFCell cell) {
        int cellType = cell.getCellType();
        String value = null;
        switch (cellType) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //  如果是date类型则 ，获取该cell的date值
                    value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                } else { // 纯数字
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
        }
        return value;
    }
}
