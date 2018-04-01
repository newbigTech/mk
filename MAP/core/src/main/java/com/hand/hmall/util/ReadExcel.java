package com.hand.hmall.util;

import com.hand.hmall.dto.Common;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadExcel {
    public List<Map<String, Object>> readExcel(String path) {
        if (path == null || Common.EMPTY.equals(path)) {
            return null;
        } else {
            String postfix = Util.getPostfix(path);
            if (!Common.EMPTY.equals(postfix)) {
                try {
                    if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                        return readXls(path);
                    } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                        return readXlsx(path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(path + Common.NOT_EXCEL_FILE);
            }
        }
        return null;
    }

    public List<Map<String, Object>> readXlsx(String path) throws IOException {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell productCode = xssfRow.getCell(0);
                    map.put("productCode", String.valueOf(getValue(productCode)));

                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * Read the Excel 2003-2007
     *
     * @param path the path of the Excel
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> readXls(String path) throws IOException {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell productCode = hssfRow.getCell(0);
                    map.put("productCode", String.valueOf(getValue(productCode)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    public List<Map<String, Object>> readXlsxWithFile(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell productCode = xssfRow.getCell(0);
                    map.put("productCode", String.valueOf(getValue(productCode)));

                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析
    public List<Map<String, Object>> readXlsWithFile(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell productCode = hssfRow.getCell(0);
                    map.put("productCode", String.valueOf(getValue(productCode)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(用户界面使用)
    public List<Map<String, Object>> readXlsWithFile_User(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell userId = hssfRow.getCell(0);
                    HSSFCell isBlackList = hssfRow.getCell(1);
                    HSSFCell label = hssfRow.getCell(2);
                    HSSFCell remark = hssfRow.getCell(3);
                    map.put("userId", String.valueOf(getValue(userId)));
                    map.put("isBlackList", String.valueOf(getValue(isBlackList)));
                    map.put("label", String.valueOf(getValue(label)));
                    map.put("remark", String.valueOf(getValue(remark)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 读取导入文件的敏感词
     *
     * @param file
     * @return
     * @throws IOException
     */

    public List<Map<String, Object>> readXlsWithFile_Sensitivity_03(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell sensitivityName = hssfRow.getCell(0);
                    map.put("sensitivityName", String.valueOf(getValue(sensitivityName)));
                    list.add(map);
                }
            }
        }
        return list;
    }


    public List<Map<String, Object>> readXlsWithFile_Sensitivity_10(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell sensitivityName = xssfRow.getCell(0);
                    map.put("sensitivityName", String.valueOf(getValue(sensitivityName)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 导入excel
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Map<String, List<Map<String, Object>>> readXlsWithFile_Base_10(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = null;
        Map<String, List<Map<String, Object>>> mapSheet = new HashMap<String, List<Map<String, Object>>>();   //有多个sheet页 键是页的名字 值是每个页面的list
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            list = new ArrayList<Map<String, Object>>();
            System.out.println(xssfWorkbook.getSheetName(numSheet));
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            //得到excel表格的第一行
            XSSFRow row0 = xssfSheet.getRow(0);
            List<String> titleRow = new ArrayList<String>();
            for (int i = 0; i < row0.getLastCellNum(); i++) {
                XSSFCell title = row0.getCell(i);    //遍历这一行  然后得到行的单元格
                titleRow.add(String.valueOf(title));
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    for (int i = 0; i < row0.getLastCellNum(); i++) {
                        /*if(xssfRow.getLastCellNum()>row0.getLastCellNum()){   //如果第二行的的单元格数目大于标题的单元格
                    		titleRow.add("错误信息");
                    	}*/
                        XSSFCell sensitivityName = xssfRow.getCell(i);
                        String value = null;
                        switch (sensitivityName.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                                //如果为时间格式的内容
                                if (HSSFDateUtil.isCellDateFormatted(sensitivityName)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    value = sdf.format(HSSFDateUtil.getJavaDate(sensitivityName.
                                            getNumericCellValue())).toString();
                                    break;
                                } else {
                                    value = new DecimalFormat("0").format(sensitivityName.getNumericCellValue());
                                }
                                break;
                            case HSSFCell.CELL_TYPE_STRING: // 字符串
                                value = sensitivityName.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = sensitivityName.getBooleanCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = sensitivityName.getCellFormula() + "";
                                break;
                            case HSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                value = "未知类型";
                                break;
                        }
                        map.put(titleRow.get(i), value);
                    }
                    list.add(map);
                }
            }
            mapSheet.put(xssfWorkbook.getSheetName(numSheet), list);
        }
        return mapSheet;
    }

    public Map<String, List<Map<String, Object>>> readXlsWithFile_Base_03(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = null;
        Map<String, List<Map<String, Object>>> mapSheet = new HashMap<String, List<Map<String, Object>>>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            list = new ArrayList<Map<String, Object>>();
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            //得到excel表格的第一行
            HSSFRow row0 = hssfSheet.getRow(0);
            List<String> titleRow = new ArrayList<String>();
            for (int i = 0; i < row0.getLastCellNum(); i++) {
                HSSFCell title = row0.getCell(i);    //遍历这一行  然后得到行的单元格
                titleRow.add(String.valueOf(title));
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    for (int i = 0; i < row0.getLastCellNum(); i++) {
                    	/*if(hssfRow.getLastCellNum()>row0.getLastCellNum()){   //如果第二行的的单元格数目大于标题的单元格
                    		titleRow.add("错误信息");
                    	}*/
                        HSSFCell sensitivityName = hssfRow.getCell(i);
                        String value = null;
                        switch (sensitivityName.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                                //如果为时间格式的内容
                                if (HSSFDateUtil.isCellDateFormatted(sensitivityName)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    value = sdf.format(HSSFDateUtil.getJavaDate(sensitivityName.
                                            getNumericCellValue())).toString();
                                    break;
                                } else {
                                    value = new DecimalFormat("0").format(sensitivityName.getNumericCellValue());
                                }
                                break;
                            case HSSFCell.CELL_TYPE_STRING: // 字符串
                                value = sensitivityName.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = sensitivityName.getBooleanCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = sensitivityName.getCellFormula() + "";
                                break;
                            case HSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                value = "未知类型";
                                break;
                        }
                        map.put(titleRow.get(i), value);
                    }
                    list.add(map);
                }
            }
            mapSheet.put(hssfWorkbook.getSheetName(numSheet), list);
        }
        return mapSheet;
    }

    //传一个文件对象过来解析(用户界面使用)
    public List<Map<String, Object>> readXlsxWithFile_User(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell userId = xssfRow.getCell(0);
                    XSSFCell isBlackList = xssfRow.getCell(1);
                    XSSFCell label = xssfRow.getCell(2);
                    XSSFCell remark = xssfRow.getCell(3);
                    map.put("userId", String.valueOf(getValue(userId)));
                    map.put("isBlackList", String.valueOf(getValue(isBlackList)));
                    map.put("label", String.valueOf(getValue(label)));
                    map.put("remark", String.valueOf(getValue(remark)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(drools用户选择界面使用)
    public List<Map<String, Object>> readXlsWithFile_Drools_User(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();

                    HSSFCell userId = null;
                    HSSFCell name = null;
                    HSSFCell mobileNumber = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;
                    if (hssfRow.getCell(0) != null) {
                        userId = hssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【用户ID为空】");
                    }
                    if (hssfRow.getCell(1) != null) {
                        name = hssfRow.getCell(1);
                    } else {
                        flag = false;
                        errorMsg.append("【用户名称为空】");
                    }
                    if (hssfRow.getCell(2) != null) {
                        mobileNumber = hssfRow.getCell(2);
                    } else {
                        flag = false;
                        errorMsg.append("【用户手机号为空】");
                    }

                    String userIdStr = (userId != null) ? String.valueOf(getValue(userId)) : "";
                    String nameStr = (name != null) ? String.valueOf(getValue(name)) : "";
                    String mobileNumberStr = (mobileNumber != null) ? String.valueOf(getValue(mobileNumber)) : "";

                    map.put("userId", userIdStr);
                    map.put("name", nameStr);
                    map.put("mobileNumber", mobileNumberStr);
                    map.put("isSuccess", flag ? "Y" : "N");
                    map.put("errorMsg", errorMsg.toString());

                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(drools用户选择界面使用)
    public List<Map<String, Object>> readXlsxWithFile_Drools_User(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell userId = null;
                    XSSFCell name = null;
                    XSSFCell mobileNumber = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;
                    if (xssfRow.getCell(0) != null) {
                        userId = xssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【用户ID为空】");
                    }
                    if (xssfRow.getCell(1) != null) {
                        name = xssfRow.getCell(1);
                    } else {
                        flag = false;
                        errorMsg.append("【用户名称为空】");
                    }
                    if (xssfRow.getCell(2) != null) {
                        mobileNumber = xssfRow.getCell(2);
                    } else {
                        flag = false;
                        errorMsg.append("【用户手机号为空】");
                    }

                    String userIdStr = (userId != null) ? String.valueOf(getValue(userId)) : "";
                    String nameStr = (name != null) ? String.valueOf(getValue(name)) : "";
                    String mobileNumberStr = (mobileNumber != null) ? String.valueOf(getValue(mobileNumber)) : "";

                    map.put("userId", userIdStr);
                    map.put("name", nameStr);
                    map.put("mobileNumber", mobileNumberStr);
                    map.put("isSuccess", flag ? "Y" : "N");
                    map.put("errorMsg", errorMsg.toString());

                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(drools商品选择界面使用)
    public List<Map<String, Object>> readXlsWithFile_Drools_Product(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();

                    HSSFCell productId = null;
                    HSSFCell productCode = null;
                    HSSFCell name = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;
                    if (hssfRow.getCell(0) != null) {
                        productId = hssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【商品ID为空】");
                    }
                    if (hssfRow.getCell(1) != null) {
                        productCode = hssfRow.getCell(1);
                    } else {
                        flag = false;
                        errorMsg.append("【商品编码为空】");
                    }
                    if (hssfRow.getCell(2) != null) {
                        name = hssfRow.getCell(2);
                    } else {
                        flag = false;
                        errorMsg.append("【商品名称为空】");
                    }

                    String productIdStr = (productId != null) ? String.valueOf(getValue(productId)) : "";
                    String productCodeStr = (productCode != null) ? String.valueOf(getValue(productCode)) : "";
                    String nameStr = (name != null) ? String.valueOf(getValue(name)) : "";

                    map.put("productId", productIdStr);
                    map.put("productCode", productCodeStr);
                    map.put("name", nameStr);
                    map.put("isSuccess", flag ? "Y" : "N");
                    map.put("errorMsg", errorMsg.toString());

                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(drools用户选择界面使用)
    public List<Map<String, Object>> readXlsxWithFile_Drools_Product(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell productId = null;
                    XSSFCell productCode = null;
                    XSSFCell name = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;
                    if (xssfRow.getCell(0) != null) {
                        productId = xssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【商品ID为空】");
                    }
                    if (xssfRow.getCell(1) != null) {
                        productCode = xssfRow.getCell(1);
                    } else {
                        flag = false;
                        errorMsg.append("【商品编码为空】");
                    }
                    if (xssfRow.getCell(2) != null) {
                        name = xssfRow.getCell(2);
                    } else {
                        flag = false;
                        errorMsg.append("【商品名称为空】");
                    }

                    String productIdStr = (productId != null) ? String.valueOf(getValue(productId)) : "";
                    String productCodeStr = (productCode != null) ? String.valueOf(getValue(productCode)) : "";
                    String nameStr = (name != null) ? String.valueOf(getValue(name)) : "";

                    map.put("productId", productIdStr);
                    map.put("productCode", productCodeStr);
                    map.put("name", nameStr);
                    map.put("isSuccess", flag ? "Y" : "N");
                    map.put("errorMsg", errorMsg.toString());

                    list.add(map);
                }
            }
        }
        return list;
    }

    //联想词使用
    public List<Map<String, Object>> readXlsWithFile_AssociationWordSet(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell suggestName = hssfRow.getCell(0);
                    map.put("suggestName", String.valueOf(getValue(suggestName)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    //联想词使用
    public List<Map<String, Object>> readXlsxWithFile_AssociationWordSet(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell suggestName = xssfRow.getCell(0);
                    map.put("suggestName", String.valueOf(getValue(suggestName)));
                    list.add(map);
                }
            }
        }
        return list;
    }


    //传一个文件对象过来解析(库存界面使用)
    public List<Map<String, Object>> readXlsWithFile_Stock(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    map = new HashMap<>();
                    HSSFCell productId = hssfRow.getCell(0);
                    HSSFCell storeId = hssfRow.getCell(1);
                    HSSFCell allQuantity = hssfRow.getCell(2);
                    map.put("productId", String.valueOf(getValue(productId)));
                    map.put("storeId", String.valueOf(getValue(storeId)));
                    map.put("allQuantity", String.valueOf(getValue(allQuantity)));
                    list.add(map);
                }
            }
        }
        return list;
    }

    //传一个文件对象过来解析(库存界面使用)
    public List<Map<String, Object>> readXlsxWithFile_Stock(MultipartFile file) throws IOException {
        System.out.println(Common.PROCESSING + file.getOriginalFilename());
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    map = new HashMap<>();
                    XSSFCell productId = xssfRow.getCell(0);
                    XSSFCell storeId = xssfRow.getCell(1);
                    XSSFCell allQuantity = xssfRow.getCell(2);
                    map.put("productId", String.valueOf(getValue(productId)));
                    map.put("storeId", String.valueOf(getValue(storeId)));
                    map.put("allQuantity", String.valueOf(getValue(allQuantity)));
                    list.add(map);
                }
            }
        }
        return list;
    }


    @SuppressWarnings("static-access")
    private String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}
