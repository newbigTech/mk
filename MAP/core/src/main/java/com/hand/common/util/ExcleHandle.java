package com.hand.common.util;

import com.hand.hmall.exception.ExcleHandleException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 关于ExcleHandle操作
 * 导出Excle表格已“.xls”结尾
 *
 * @author tiantao
 */
public class ExcleHandle {

    /**
     * 将Map中的日期格式转换为字符串格式，如果是数字类型，那么直接转换为字符串
     *
     * @param list
     * @return
     */
    public static List<Map<String, String>> formatMap(List<Map<String, Object>> list) {
        List<Map<String, String>> listStr = new ArrayList<Map<String, String>>();
        com.hand.hmall.util.DateUtil.formDate(list);
        for (Map<String, Object> map : list) {
            Map<String, String> mapStr = new LinkedHashMap<String, String>();
            for (String key : map.keySet()) {
                if (map.get(key) == null) {
                    mapStr.put(key, "");
                } else {
                    mapStr.put(key, map.get(key).toString());
                }
            }
            listStr.add(mapStr);
        }
        return listStr;
    }


    /**
     * 导出Excle
     *
     * @param list  要导出的数据
     * @param title 该Excle的标题
     * @param path  要导出的路径
     * @throws ExcleHandleException
     */
    public static ResponseEntity<byte[]> export(List<Map<String, String>> list, String title) throws ExcleHandleException {

        if (list == null || list.size() <= 0) {
            throw new ExcleHandleException(ExcleHandleException.NEED_List_Not_Empty);
        }

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet   
        HSSFSheet sheet = wb.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  

        Map<String, String> mapTitle = list.get(0);

        //设置标题（在第一行显示）
        CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) mapTitle.keySet().size() - 1); //合并单元格
        sheet.addMergedRegion(region1);                     //指定要在sheet中合并单元格
        HSSFCell cell_title = row.createCell((short) 0);     //创建单元格对象
        cell_title.setCellValue(title);                     //在该单元格中设值
        HSSFCellStyle style_title = wb.createCellStyle();     //创建标题样式
        style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //设置居中

        HSSFFont font = wb.createFont();          //设置字体
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 18);//设置字体大小
        style_title.setFont(font);
        cell_title.setCellStyle(style_title);


        row = sheet.createRow((int) 1);

        int title_i = 0;
        HSSFCell cell;
        for (String key : mapTitle.keySet()) {
            cell = row.createCell((short) title_i++);     //将该单元格放到row的哪一行中
            cell.setCellValue(key);                    //设置该单元个的内容
            cell.setCellStyle(style);                 //设置该单元格的样式
        }

        //5.写入实体数据
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);          //获取一条要插入Excle表格的数据

            //创建下一行
            row = sheet.createRow(i + 2);

            int value_i = 0;
            for (String key : map.keySet()) {
                //创建单元格
                cell = row.createCell(value_i++);
                cell.setCellValue(map.get(key));
                cell.setCellStyle(style);                 //设置该单元格的样式
            }
        }


        HttpHeaders headers = new HttpHeaders();
        String fileName;
        try {

            fileName = new String((title + ".xls").getBytes("UTF-8"), "iso-8859-1");
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.CREATED);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }


}
