package com.hand.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 * 7.下载导入模板方法
 * 本工具类以后可能还会加功能,请关注我的博客: http://blog.csdn.net/lk_blog
 */
public class ExcelUtil<T> {
    public static final int MAX_ROWS = 65536;
    Class<T> clazz;
    private List<String> errMsg = new ArrayList<String>();
    private String psw;

    private Integer startRow = 1;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 根据cell类型获取列值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                DecimalFormat df = new DecimalFormat("");
                df.setGroupingUsed(false);
                cellValue = df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula() + "";
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public boolean hasErr() {
        return errMsg.size() > 0;
    }

    public List<String> getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(List<String> errMsg) {
        this.errMsg = errMsg;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    /**
     * 当自定义开始抓取数据的行数时，调用此方法
     *
     * @param sheetName 为空时抓第一页
     * @param input     InputStream 字节流
     * @param startRow  sheet页中抓取数据开始的行数
     * @return
     * @throws Exception
     * @returnType List<T>
     */
    public List<T> importExcel(String path, String sheetName, InputStream input, Integer startRow) throws Exception {
        if (startRow != null) {
            this.startRow = startRow;
        }
        return importExcel(path, sheetName, input);
    }

    /**
     * Excel数据导入得到List列表
     *·
     * @param sheetName Sheet页名称 为空时抓第一页
     * @param input    ·
     * @throws Exception
     */
    public List<T> importExcel(String path, String sheetName, InputStream input) throws Exception {
        // List存储
        List<T> list = new ArrayList<T>();
        Workbook book = null;

        if (isExcel2003(path)) {
            book = new HSSFWorkbook(input);
        } else if (isExcel2007(path)) {
            book = new XSSFWorkbook(input);
        }
        Sheet sheet = null;

        sheet = (sheetName != null && !sheetName.trim().isEmpty())
                ? book.getSheet(sheetName) // 如果指定sheet名,则取指定sheet中的内容.
                : book.getSheetAt(0); // 如果传入的sheet名不存在则默认指向第1个sheet.

        if (sheet == null) {
            errMsg.add(!sheetName.trim().equals("") ? sheetName : "第一页" + "sheet页为空");
        }
        if (sheet != null) {
            int rows = sheet.getLastRowNum();// 得到数据的行数
            if (rows > 0) {// 有数据时才处理
                Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
                for (Field field : allFields) {
                    // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                        ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                        int col = 0;
                        if (ExcelVOAttribute.LAST_COL.equals(attr.column()))
                            col = getLastExcelCol(sheet);// 获得列号
                        else {
                            col = getExcelCol(attr.column());// 获得列号
                        }
                        field.setAccessible(true);// 设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                for (int i = startRow; i <= rows; i++) {// 从第2行开始取数据,默认第一行是表头.
                    Row cells = sheet.getRow(i);// 得到一行中的所有单元格对象.
                    T entity = null;
                    for (int j = 0; j < cells.getLastCellNum(); j++) {
                        Cell cell = cells.getCell(j);// 单元格中的内容.
                        if (cell == null) {
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
                        Field field = fieldsMap.get(j);// 从map中得到对应列的field.
                        // 取得类型,并根据对象类型设置值.
                        if (field != null)
                            this.setEntity(field, cell, entity);
                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        }
        book.close();
        return list;
    }

    /**
     * 对每一个cell进行类型定义
     *
     * @param field 字段
     * @param cell  excel表格单元格(数据来源)
     * @return cell 设置好的表格单元格
     */
    protected XSSFCell setCellType(Field field, XSSFCell cell) {
        Class<?> fieldType = field.getType();
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        } else if (String.class == fieldType) {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        } else if (Character.TYPE == fieldType) {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        } else if (java.util.Date.class == fieldType) {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        } else {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        }
        if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            XSSFCellStyle style = cell.getCellStyle();
            style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            cell.setCellStyle(style);
        }
        return cell;
    }

    /**
     * 将表格中的单元格set到实体类中
     *
     * @param field  字段
     * @param cell   excel表格单元格(数据来源)
     * @param entity 待填充的实体
     * @return entity 填充好的实体
     */
    protected T setEntity(Field field, Cell cell, T entity) {
        Class<?> fieldType = field.getType();
        //根据cell类型获取转换成String的cell值
        String cellValue = getCellValue(cell);
        try {
            if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                if (StringUtils.isNotBlank(cellValue)) {
                    field.set(entity, Integer.parseInt(cellValue));
                }
            } else if (String.class == fieldType) {
                field.set(entity, cellValue);
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                if (StringUtils.isNotBlank(cellValue)) {
                    field.set(entity, Long.parseLong(cellValue));
                }
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                if (StringUtils.isNotBlank(cellValue)) {
                    field.set(entity, Float.parseFloat(cellValue));
                }
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                if (StringUtils.isNotBlank(cellValue)) {
                    field.set(entity, Short.parseShort(cellValue));
                }
            } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                if (StringUtils.isNotBlank(cellValue)) {
                    field.set(entity, Double.parseDouble(cellValue));
                }
            } else if (Character.TYPE == fieldType) {
                if ((cellValue != null) && (cellValue.length() > 0)) {
                    field.set(entity, Character.valueOf(cellValue.charAt(0)));
                }
            } else if (java.util.Date.class == fieldType) {
                if (cell != null) {
                    field.set(entity, cell.getDateCellValue());
                }
            } else if (java.math.BigDecimal.class == fieldType) {
                if (cell != null) {
                    if (StringUtils.isNotBlank(cellValue)) {
                        field.set(entity, new BigDecimal(cellValue));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list
     * @param sheetName
     * @param sheetSize
     * @param request
     * @param response
     * @param excelName
     * @return
     */
    public boolean exportExcel(List<T> list, String sheetName, int sheetSize, HttpServletRequest request, HttpServletResponse response, String excelName/*, String psw*/) {
        if (list.size() == 0) {
            try {
                downloadNodata(response);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<Field>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                fields.add(field);
            }
        }
        XSSFWorkbook workbook = new XSSFWorkbook();// 产生工作薄对象
        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize > 65536 || sheetSize < 1) {
            errMsg.add("当前sheet长度超过65536");
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil((double) list.size() / (double) sheetSize);// 取出一共有多少个sheet.
        for (int index = 0; index < sheetNo; index++) {
            XSSFSheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            XSSFRow row;
            // 可编辑的样式
            CellStyle ss = workbook.createCellStyle();
            ss.setLocked(false);
            // 不可编辑样式
            CellStyle st = workbook.createCellStyle();
            st.setLocked(true);
            XSSFCell cell;// 产生单元格
            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                // 设置列中写入内容为String类型
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(attr.name());// 写入列名
                if (attr.ColumnWidth() != null) {
                    sheet.setColumnWidth(this.getExcelCol(attr.column()), Integer.parseInt(attr.ColumnWidth()));
                }
                if (attr.enabledEditColName() == false) {
                    cell.setCellStyle(st);
                }
                // 如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
                    cell.setCellStyle(st);
                }
                // 如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {
                    setXSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
                    cell.setCellStyle(st);
                }
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo = (T) list.get(i); // 得到导出对象.
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            setCellType(field, cell);
                            if (field.get(vo) == null) {
                                cell.setCellValue("");
                            } else if (java.util.Date.class == field.getType()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat(attr.dateFormat());
                                cell.setCellValue(dateFormat.format((Date) field.get(vo)));
                            } else {
                                cell.setCellValue(String.valueOf(field.get(vo)));
                            }
                            cell.setCellStyle(ss);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!(psw == null || psw.trim().equals(""))) {
                // 开启excel保护
                sheet.protectSheet(psw);
            }
        }

        try {
            download(workbook, response, request, excelName);
            workbook.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public int getExcelCol(String col) {

        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 获得当前sheet页中最后列(此处是采用第一行作为标题的最后一个)
     *
     * @return
     * @date 2016年9月1日 下午5:30:14
     * @author dezhi.shen@hand-china.com
     * @returnType int
     */
    public int getLastExcelCol(Sheet sheet) {
        return sheet.getRow(0).getLastCellNum() - 1;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    public XSSFSheet setXSSFPrompt(XSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                                   int firstCol, int endCol) {
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        XSSFDataValidation data_validation_view = (XSSFDataValidation) helper.createValidation(constraint, regions);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    public XSSFSheet setXSSFValidation(XSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol,
                                       int endCol) {
        // 加载下拉列表内容
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        XSSFDataValidation data_validation_list = (XSSFDataValidation) helper.createValidation(constraint, regions);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 用于判断Excel是03的还是07的
     *
     * @param filePath Excel完整路径
     * @return
     * @author jianping.huo@hand-china.com
     * @date 2017/1/10 16:32
     */
    public boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 用于判断Excel是03的还是07的
     *
     * @param filePath Excel完整路径
     * @return
     * @author jianping.huo@hand-china.com
     * @date 2017/1/10 16:32
     */
    public boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 输出到浏览器
     *
     * @param response   输出流
     * @param response
     * @param request
     * @param returnName 文件名称
     * @throws IOException
     */
    public void download(Workbook workbook, HttpServletResponse response,
                         HttpServletRequest request, String returnName) throws IOException {
        // 处理浏览器差异导致的下载文件名称乱码的问题
        String userAgent = request.getHeader("USER-AGENT");
        String enocodeType = "utf8";
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

        ServletOutputStream outputstream = response.getOutputStream(); // 取得输出流
        workbook.write(outputstream);
        outputstream.flush(); // 刷数据
    }

    public void downloadNodata(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("<script>alert('暂无需要下载的数据');</script>");
        out.println("  </BODY>");
        out.println("</HTML>");
        //最后要记得清空缓存区，并且关闭。
        out.flush();
        out.close();
    }

    /**
     * 样式
     *
     * @param wb
     * @param nStyle     样式
     * @param nFont      字体
     * @param colorValue 背景颜色
     * @return
     */
    public CellStyle title(Workbook wb, CellStyle nStyle, Font nFont, short colorValue) {
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
     * 下载导入模板
     *
     * @param request
     * @param response
     * @param excelName
     * @param sheetName
     */
    public void downloadExcelModel(HttpServletRequest request, HttpServletResponse response, String excelName, String sheetName) {
        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<Field>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                if (attr.isExport()) {//判断是否导出列标识
                    fields.add(field);
                }
            }
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFFont nFont = workbook.createFont();
        short colorYello = IndexedColors.YELLOW.getIndex();
        XSSFRow row;
        // 不可编辑样式
        CellStyle st = workbook.createCellStyle();
        st.setLocked(true);
        XSSFCell cell;// 产生单元格
        row = sheet.createRow(0);// 产生一行
        for (Field field : fields) {
            ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
            int col = getExcelCol(attr.column());// 获得列号
            cell = row.createCell(col);// 创建列
            // 设置列中写入内容为String类型
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(attr.name());// 写入列名
            if (attr.ColumnWidth() != null) {
                sheet.setColumnWidth(this.getExcelCol(attr.column()), Integer.parseInt(attr.ColumnWidth()));
            }
            if (attr.enabledEditColName() == false) {
                cell.setCellStyle(title(workbook, st, nFont, colorYello));
            }
            // 如果设置了提示信息则鼠标放上去提示.
            if (!attr.prompt().trim().equals("")) {
                setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
                cell.setCellStyle(title(workbook, st, nFont, colorYello));
            }
            // 如果设置了combo属性则本列只能选择不能输入
            if (attr.combo().length > 0) {
                setXSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
                cell.setCellStyle(title(workbook, st, nFont, colorYello));
            }
        }
        if (!(psw == null || psw.trim().equals(""))) {
            // 开启excel
            sheet.protectSheet(psw);
        }
        try {
            download(workbook, response, request, excelName);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @param resource 模板路径
     * @throws IOException
     * @description 从resource中下载固定的模板excel文件
     */
    public void downloadExcelModel(HttpServletRequest request, HttpServletResponse response, String resource) throws IOException {
        // 获取文件的绝对路径
        ServletContext context = request.getSession().getServletContext();
        String url = context.getRealPath("/resources/" + resource);
        File file = new File(url);
        FileInputStream fis = new FileInputStream(file);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        // 处理浏览器差异导致的下载文件名称乱码的问题
        String userAgent = request.getHeader("USER-AGENT");
        String enocodeType = null;

        if (userAgent.indexOf("Mozilla") != -1) {// google,火狐浏览器
            enocodeType = "iso8859-1";
        } else {
            enocodeType = "utf8";
        }

        response.setContentType("application/x-download");
        String returnName = response.encodeURL(new String(resource.getBytes(), enocodeType)); // 保存的文件名,必须和页面编码一致,否则乱码
        response.addHeader("Content-Disposition", "attachment;filename=" + returnName);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
    }

}
