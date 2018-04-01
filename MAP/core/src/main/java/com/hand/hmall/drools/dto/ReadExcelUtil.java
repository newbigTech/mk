package com.hand.hmall.drools.dto;

import com.hand.hmall.dto.Common;
import com.hand.hmall.mst.service.IProductService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shoupeng.wei
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/9/22 16:24
 */
public class ReadExcelUtil {

    @Autowired
    private IProductService productService;

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
                    HSSFCell productCode = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;

                    if (hssfRow.getCell(0) != null) {
                        productCode = hssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【商品编码为空】");
                    }

                    String productCodeStr = (productCode != null) ? String.valueOf(getValue(productCode)) : "";
                    map.put("productCode", productCodeStr);
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
                    XSSFCell productCode = null;
                    StringBuilder errorMsg = new StringBuilder();
                    boolean flag = true;

                    if (xssfRow.getCell(0) != null) {
                        productCode = xssfRow.getCell(0);
                    } else {
                        flag = false;
                        errorMsg.append("【商品编码为空】");
                    }

                    String productCodeStr = (productCode != null) ? String.valueOf(getValue(productCode)) : "";
                    map.put("productCode", productCodeStr);
                    map.put("isSuccess", flag ? "Y" : "N");
                    map.put("errorMsg", errorMsg.toString());

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
