package com.hand.hap.im.dto;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;

/**
 * @author wuyougui@hand-china.com
 * @version 1.0
 * @name ItemExcel
 * @description
 * @date 2017/5/16
 */
public class ItemExcel {
    private ByteArrayOutputStream byteArrayOutputStream=null;

        private XSSFWorkbook wb;

        private int cellNumber;

        private CellStyle fieldCellStyle;

        public static int defaultRow=500;

        public ItemExcel(){
            wb=new XSSFWorkbook();
            fieldCellStyle=wb.createCellStyle();
            fieldCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            fieldCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            fieldCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            fieldCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            fieldCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            fieldCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            fieldCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            fieldCellStyle.setVerticalAlignment(XSSFCellStyle.ALIGN_CENTER);
            fieldCellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中
        }

    /**
     * 创建头
     * @param sheet
     * @param tableHeader
     */
        private void createTableHeader(XSSFSheet sheet, String[] tableHeader){
            for (int o = 0; o < 1 ;o++){
            if(o==0) {
                //创建field行
                XSSFRow fieldRow = sheet.createRow(o);
                fieldRow.setHeightInPoints(20);
                cellNumber = tableHeader.length;
                for (int i = 0; i < cellNumber; i++) {
                    XSSFCell fieldCell = fieldRow.createCell(i);
                    fieldCell.setCellStyle(fieldCellStyle);
                    fieldCell.setCellValue(tableHeader[i]);
                    //设置cell的宽度
                    sheet.setColumnWidth(i, 4000);
                }
            }else {
                XSSFRow fieldRow = sheet.createRow(o);
                cellNumber = tableHeader.length;
                for (int i = 0; i < cellNumber; i++) {
                    XSSFCell fieldCell = fieldRow.createCell(i);
                    XSSFDataFormat format = wb.createDataFormat();
                    XSSFCellStyle cellStyle2 = wb.createCellStyle();
                    cellStyle2.setDataFormat(format.getFormat("@"));
                    fieldCell.setCellStyle(cellStyle2);
                    sheet.setColumnWidth(i, 4000);
                }
            }
        }
        }

    /**
     * excel写入
     * @return
     * @throws Exception
     */
    public ByteArrayOutputStream excelWriter() throws Exception{

            XSSFSheet sheet=wb.createSheet("物料属性配置");
            sheet.setHorizontallyCenter(true);
            sheet.setVerticallyCenter(true);

            String[] tableHeader={"客户端","ECN号","发布号","项目号","状态","物料号","物料类型","删除标识符","工厂","物料描述（短文本）","物料描述（短文本）"
                    ,"基本计量单位","旧物料号","物料组","产品组","跨工厂物料状态","外部物料组","产品层次","重量单位","业务量","体积单位"
                    ,"大小/量纲","毛重","净重","可配置的物料","文档号码(无文档管理系统)","包装尺寸","件箱转换系数","写入日期","写入时间","旧物料号","原发布号"
                    ,"是否重复发送 是：X","面料材质","交接尺寸","虚拟物料 是：X","材质","包装材积","拼版尺寸(长 *宽*厚)","净料尺寸(长 *宽*厚)","毛坯件（X件）","要做备件","MC"
                    ,"备货点","材型","零件毛坯组件","区分通用件、专用件","组装模式（区分KD或组死）","拼花","交接用法","拼版用法","是否外购","是否委外","是否散装物料"
                    ,"领用工厂","配方号","树脂木芯","滚涂颜色","虚拟调色漆","长","宽","高","备货类型","套件标识"};
            createTableHeader(sheet,tableHeader);//设置sheet头}

            /*if(dataTypeList!=null&&dataTypeList.length>0) {
                //属性类型
                DataValidation cell_validation_2 = setValidateDropList(sheet, 1, defaultRow, 2, 2, dataTypeList);
                sheet.addValidationData(cell_validation_2);
            }
            if(enabledFlagList!=null&&enabledFlagList.length>0) {
                //启用标记
                DataValidation cell_validation_3 = setValidateDropList(sheet, 1, defaultRow, 3, 3, enabledFlagList);
                sheet.addValidationData(cell_validation_3);
            }
            if(editableFlagList!=null&&editableFlagList.length>0) {
                //可编辑标记
                DataValidation cell_validation_4 = setValidateDropList(sheet, 1, defaultRow, 4, 4, editableFlagList);
                sheet.addValidationData(cell_validation_4);
            }
            if(requiredFlagList!=null&&requiredFlagList.length>0) {
                //必输标记
                DataValidation cell_validation_5 = setValidateDropList(sheet, 1, defaultRow, 5, 5, requiredFlagList);
                sheet.addValidationData(cell_validation_5);
            }
            if(defaultValueTypeList!=null&&defaultValueTypeList.length>0) {
                //默认值类型
                DataValidation cell_validation_7 = setValidateDropList(sheet, 1, defaultRow, 7, 7, defaultValueTypeList);
                sheet.addValidationData(cell_validation_7);
            }
            if(widgetList!=null&&widgetList.length>0) {
                //控件类型
                DataValidation cell_validation_8 = setValidateDropList(sheet, 1, defaultRow, 8, 8, widgetList);
                sheet.addValidationData(cell_validation_8);
            }
            if(widgetWidthList!=null&&widgetWidthList.length>0) {
                //控件位置
                DataValidation cell_validation_14 = setValidateDropList(sheet, 1, defaultRow, 14, 14, widgetWidthList);
                sheet.addValidationData(cell_validation_14);
            }

            //数据精度，数据长度，控件位置（序列）一定为数字 11,12,13
            DataValidation cell_validation_11=setValidateNumber(sheet,1,defaultRow,11,11);
            sheet.addValidationData(cell_validation_11);

            DataValidation cell_validation_12=setValidateNumber(sheet,1,defaultRow,12,12);
            sheet.addValidationData(cell_validation_12);

            DataValidation cell_validation_13=setValidateNumber(sheet,1,defaultRow,13,13);
            sheet.addValidationData(cell_validation_13);*/

            byteArrayOutputStream=new ByteArrayOutputStream();
            wb.write(byteArrayOutputStream);
            return byteArrayOutputStream;
        }

    /**
     * excel写入值
     * @return
     * @throws Exception
     */
    public ByteArrayOutputStream excelValueWriter() throws Exception{

        XSSFSheet sheet=wb.createSheet("物料属性选配置值");
        sheet.setHorizontallyCenter(true);
        sheet.setVerticallyCenter(true);
        String[] tableHeader={"物料编码","选配值标记","选配项标记","必选项标记","前台可见标识","A0001","A0002","A0003",
                "A0004","A0005","A0006","A0007","A0008","A0009","A0010","A0011","A0012","A0013","A0014","A0015","A0016","A0017","A0018"
                ,"A0019","A0020","A0021","A0022","A0023","A0024","A0025"};
        createTableHeader(sheet,tableHeader);//设置sheet头}

        byteArrayOutputStream=new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream;
    }


        public void close(){
            if(byteArrayOutputStream!=null){
                try{
                    byteArrayOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    /**
     * 下拉框校验
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @param listOfValues
     * @return
     */
        private DataValidation setValidateDropList(XSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol,String[] listOfValues){
            XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);

            DataValidationConstraint constraint = helper.createExplicitListConstraint(listOfValues);
            CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                    lastRow, firstCol, lastCol);
            DataValidation validation=helper.createValidation(constraint, regions);
            validation.setSuppressDropDownArrow(true);
            validation.createErrorBox("输入值有误","请从下拉框选择");//设置错误提示信息
            validation.setShowErrorBox(true);
            return validation;
        }

    /**
     * 数字校验
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @return
     */
        //set data validation ,only allow number
        private DataValidation setValidateNumber(XSSFSheet sheet,int firstRow, int lastRow, int firstCol, int lastCol){
            XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
            DataValidationConstraint constraintNum=new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.INTEGER,
                    DataValidationConstraint.OperatorType.GREATER_OR_EQUAL, "1");
            CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                    lastRow, firstCol, lastCol);//列的范围
            DataValidation validationNum=helper.createValidation(constraintNum, regions);
            validationNum.createErrorBox("输入值类型出错", "数值型,请输入大于或等于1的整数值");
            validationNum.setShowErrorBox(true);
            return validationNum;
        }


}
