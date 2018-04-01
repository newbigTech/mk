package com.hand.hap.im.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import java.util.*;

/**
 * @author yougui.wu@hand-china.com
 * @version 1.0
 * @name ItemAttrExcelReadUtils
 * @description  excel读取工具类
 * @date 2017/5/17
 */
public class ItemExcelUtils {

        Logger logger = LoggerFactory.getLogger(ItemExcelUtils.class);
        //文件类型
        private String filetype;
        //文件二进制输入流
        private InputStream is=null;
        //当前的Sheet,默认从第0页开始
        private int currSheet=0;
        //当前的位置，默认第0行
        private int currPosition=1;
        //Sheet数量
        private int numOfSheets;
        //HSSFWorkbook
        private XSSFWorkbook workbook=null;

        public int getCurrSheet() {
            return currSheet;
        }

        //设置excel头的列明
        String[] tableHeader={"客户端","ECN号","发布号","项目号","状态","物料号","物料类型","删除标识符","工厂","物料描述（短文本）","物料描述（短文本）"
                ,"基本计量单位","旧物料号","物料组","产品组","跨工厂物料状态","外部物料组","产品层次","重量单位","业务量","体积单位"
                ,"大小/量纲","毛重","净重","可配置的物料","文档号码(无文档管理系统)","包装尺寸","件箱转换系数","写入日期","写入时间","旧物料号","原发布号"
                ,"是否重复发送 是：X","面料材质","交接尺寸","虚拟物料 是：X","材质","包装材积","拼版尺寸(长 *宽*厚)","净料尺寸(长 *宽*厚)","毛坯件（X件）","要做备件","MC"
                ,"备货点","材型","零件毛坯组件","区分通用件、专用件","组装模式（区分KD或组死）","拼花","交接用法","拼版用法","是否外购","是否委外","是否散装物料"
                ,"领用工厂","配方号","树脂木芯","滚涂颜色","虚拟调色漆","长","宽","高","备货类型","套件标识"};

        //设置item value 表Excel头的列名
        String[] excelHeader={"物料编码","选配值标记","选配项标记","必选项标记"};


        /**
         * 设置从第几页开始，默认0
         * @param currSheet
         */
        public void setCurrSheet(int currSheet) {
            this.currSheet = currSheet;
        }

        public int getCurrPosition() {
            return currPosition;
        }

        /**
         * 设置从第几行开始，默认0
         * @param currPosition
         */
        public void setCurrPosition(int currPosition) {
            this.currPosition = currPosition;
        }

        public ItemExcelUtils(String filename, InputStream is) throws IOException,Exception{
            //判断参数为空或没有意义
            if(filename==null||filename.trim().equals("")){
                throw new IOException("no input file specified");
            }
            //取得文件名的后缀名赋值给filetype
            this.filetype= filename.substring(filename.lastIndexOf(".")+1);
            //创建文件输入流
            this.is=is;
            //判断文件格式
            if(this.filetype.equalsIgnoreCase("xlsx")){
                //如果是Excel文件则创建XSSFWorkbook读取\
                workbook= new XSSFWorkbook(is);
                //设置Sheet数
                numOfSheets=workbook.getNumberOfSheets();
            }else{
                throw new Exception("文件类型不支持");
            }
        }

        public Map<String,Object> readExcel() throws IOException {
            logger.info("解析开始================");
            logger.info("总页数:"+numOfSheets);
            List<List<Object>> excelList=new ArrayList<List<Object>>();
            //错误记录list,抛至前台显示
            List<String> errorList=new ArrayList<>();

            for(int i=currSheet;i<numOfSheets;i++){
                Sheet sheet=workbook.getSheetAt(i);
                int rowNum=sheet.getLastRowNum()+1;
                int columnsNum=sheet.getRow(currPosition-1).getLastCellNum();
                logger.info("第"+(i+1)+"页行数:"+rowNum);
                for(int j=currPosition;j<rowNum;j++){
                    Cell attrCell=null;
                    //获得当前行
                    Row row = sheet.getRow(j);
                    if(row!=null&&!(row.equals(""))){
                        List<Object> rowList=new ArrayList<Object>();

                        //取得第0列,属性编码
                        attrCell=row.getCell(0,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var3=getCellValue(attrCell);
                            rowList.add(var3);
                        }else{
                            errorList.add(getErrorMessage(j+1,0,tableHeader[0]));
                            rowList.add("");
                        }

                        //取得第1列,属性名称
                        attrCell=row.getCell(1);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var1=getCellValue(attrCell);
                            rowList.add(var1);
                        }else{
                            errorList.add(getErrorMessage(j+1,1,tableHeader[1]));
                        }

                        //取得第2列,属性名称
                        attrCell=row.getCell(2);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var2=getCellValue(attrCell);
                            rowList.add(var2);
                        }else{
                            errorList.add(getErrorMessage(j+1,2,tableHeader[2]));
                        }

                        //取得第3列,属性名称
                        attrCell=row.getCell(3);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var3=getCellValue(attrCell);
                            rowList.add(var3);
                        }else{
                            errorList.add(getErrorMessage(j+1,3,tableHeader[3]));
                        }

                        //取得第4列,属性名称
                        attrCell=row.getCell(4,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var4=getCellValue(attrCell);
                            rowList.add(var4);
                        }else{
                            rowList.add("");
                        }

                        //取得第5列,属性名称
                        attrCell=row.getCell(5);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            String var5= String.valueOf(getCellValue(attrCell));
                            rowList.add(var5);
                        }else{
                            errorList.add(getErrorMessage(j+1,5,tableHeader[5]));
                        }

                        //取得第6列,属性名称
                        attrCell=row.getCell(6);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var6=getCellValue(attrCell);
                            rowList.add(var6);
                        }else{
                            errorList.add(getErrorMessage(j+1,6,tableHeader[6]));
                        }

                        //取得第7列,属性名称
                        attrCell=row.getCell(7,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var7=getCellValue(attrCell);
                            rowList.add(var7);
                        }else{
                            rowList.add("");
                        }

                        //取得第8列,属性名称
                        attrCell=row.getCell(8,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var8=getCellValue(attrCell);
                            rowList.add(var8);
                        }else{
                            rowList.add("");
                        }

                        //取得第9列,属性名称
                        attrCell=row.getCell(9);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var9=getCellValue(attrCell);
                            rowList.add(var9);
                        }else{
                            errorList.add(getErrorMessage(j+1,9,tableHeader[9]));
                        }

                        //取得第10列,属性名称
                        attrCell=row.getCell(10,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var10=getCellValue(attrCell);
                            rowList.add(var10);
                        }else{
                            rowList.add("");
                        }

                        //取得第11列,属性名称
                        attrCell=row.getCell(11,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var11=getCellValue(attrCell);
                            rowList.add(var11);
                        }else{
                            rowList.add("");
                        }

                        //取得第12列,属性名称
                        attrCell=row.getCell(12,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var12=getCellValue(attrCell);
                            rowList.add(var12);
                        }else{
                            rowList.add("");
                        }

                        //取得第13列,属性名称
                        attrCell=row.getCell(13);
                        if(attrCell!=null&&attrCell.toString()!=""){
                            Object var13=getCellValue(attrCell);
                            rowList.add(var13);
                        }else{
                            errorList.add(getErrorMessage(j+1,13,tableHeader[13]));
                        }

                        //取得第14列,属性名称
                        attrCell=row.getCell(14,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var14=getCellValue(attrCell);
                            rowList.add(var14);
                        }else{
                            rowList.add("");
                        }

                        //取得第15列,属性名称
                        attrCell=row.getCell(15,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var15=getCellValue(attrCell);
                            rowList.add(var15);
                        }else{
                            rowList.add("");
                        }

                        //取得第16列,属性名称
                        attrCell=row.getCell(16,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var16=getCellValue(attrCell);
                            rowList.add(var16);
                        }else{
                            rowList.add("");
                        }

                        //取得第17列,属性名称
                        attrCell=row.getCell(17,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var17=getCellValue(attrCell);
                            rowList.add(var17);
                        }else{
                            rowList.add("");
                        }

                        //取得第18列,属性名称
                        attrCell=row.getCell(18,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var18=getCellValue(attrCell);
                            rowList.add(var18);
                        }else{
                            rowList.add("");
                        }

                        //取得第19列,属性名称
                        attrCell=row.getCell(19,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var19=getCellValue(attrCell);
                            rowList.add(var19);
                        }else{
                            rowList.add("");
                        }

                        //取得第20列,属性名称
                        attrCell=row.getCell(20,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var20=getCellValue(attrCell);
                            rowList.add(var20);
                        }else{
                            rowList.add("");
                        }

                        //取得第21列,属性名称
                        attrCell=row.getCell(21,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var21=getCellValue(attrCell);
                            rowList.add(var21);
                        }else{
                            rowList.add("");
                        }

                        //取得第22列,属性名称
                        attrCell=row.getCell(22,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var22=getCellValue(attrCell);
                            rowList.add(var22);
                        }else{
                            rowList.add("");
                        }

                        //取得第23列,属性名称
                        attrCell=row.getCell(23,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var23=getCellValue(attrCell);
                            rowList.add(var23);
                        }else{
                            rowList.add("");
                        }

                        //取得第24列,属性名称
                        attrCell=row.getCell(24,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var24=getCellValue(attrCell);
                            rowList.add(var24);
                        }else{
                            rowList.add("");
                        }

                        //取得第25列,属性名称
                        attrCell=row.getCell(25,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var25=getCellValue(attrCell);
                            rowList.add(var25);
                        }else{
                            rowList.add("");
                        }

                        //取得第26列,属性名称
                        attrCell=row.getCell(26,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var26=getCellValue(attrCell);
                            rowList.add(var26);
                        }else{
                            rowList.add("");
                        }

                        //取得第26列,属性名称
                        attrCell=row.getCell(27,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var26=getCellValue(attrCell);
                            rowList.add(var26);
                        }else{
                            rowList.add("");
                        }

                        //取得第28列,属性名称
                        attrCell=row.getCell(28,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var28=getCellValue(attrCell);
                            rowList.add(var28);
                        }else{
                            rowList.add("");
                        }

                        //取得第29列,属性名称
                        attrCell=row.getCell(29,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var29=getCellValue(attrCell);
                            rowList.add(var29);
                        }else{
                            rowList.add("");
                        }

                        //取得第30列,属性名称
                        attrCell=row.getCell(30,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var30=getCellValue(attrCell);
                            rowList.add(var30);
                        }else{
                            rowList.add("");
                        }

                        //取得第31列,属性名称
                        attrCell=row.getCell(31,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var31=getCellValue(attrCell);
                            rowList.add(var31);
                        }else{
                            rowList.add("");
                        }

                        //取得第32列,属性名称
                        attrCell=row.getCell(32,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var32=getCellValue(attrCell);
                            rowList.add(var32);
                        }else{
                            rowList.add("");
                        }

                        //取得第33列,属性名称
                        attrCell=row.getCell(33,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var33=getCellValue(attrCell);
                            rowList.add(var33);
                        }else{
                            rowList.add("");
                        }

                        //取得第34列,属性名称
                        attrCell=row.getCell(34,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var34=getCellValue(attrCell);
                            rowList.add(var34);
                        }else{
                            rowList.add("");
                        }

                        //取得第35列,属性名称
                        attrCell=row.getCell(35,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var35=getCellValue(attrCell);
                            rowList.add(var35);
                        }else{
                            rowList.add("");
                        }

                        //取得第36列,属性名称
                        attrCell=row.getCell(36,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var36=getCellValue(attrCell);
                            rowList.add(var36);
                        }else{
                            rowList.add("");
                        }

                        //取得第37列,属性名称
                        attrCell=row.getCell(37,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var37=getCellValue(attrCell);
                            rowList.add(var37);
                        }else{
                            rowList.add("");
                        }

                        //取得第38列,属性名称
                        attrCell=row.getCell(38,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var38=getCellValue(attrCell);
                            rowList.add(var38);
                        }else{
                            rowList.add("");
                        }

                        //取得第39列,属性名称
                        attrCell=row.getCell(39,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var39=getCellValue(attrCell);
                            rowList.add(var39);
                        }else{
                            rowList.add("");
                        }

                        //取得第40列,属性名称
                        attrCell=row.getCell(40,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var40=getCellValue(attrCell);
                            rowList.add(var40);
                        }else{
                            rowList.add("");
                        }

                        //取得第41列,属性名称
                        attrCell=row.getCell(41,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var41=getCellValue(attrCell);
                            rowList.add(var41);
                        }else{
                            rowList.add("");
                        }

                        //取得第42列,属性名称
                        attrCell=row.getCell(42,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var42=getCellValue(attrCell);
                            rowList.add(var42);
                        }else{
                            rowList.add("");
                        }

                        //取得第43列,属性名称
                        attrCell=row.getCell(43,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var43=getCellValue(attrCell);
                            rowList.add(var43);
                        }else{
                            rowList.add("");
                        }

                        //取得第44列,属性名称
                        attrCell=row.getCell(44,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var44=getCellValue(attrCell);
                            rowList.add(var44);
                        }else{
                            rowList.add("");
                        }

                        //取得第45列,属性名称
                        attrCell=row.getCell(45,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var45=getCellValue(attrCell);
                            rowList.add(var45);
                        }else{
                            rowList.add("");
                        }

                        //取得第46列,属性名称
                        attrCell=row.getCell(46,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var46=getCellValue(attrCell);
                            rowList.add(var46);
                        }else{
                            rowList.add("");
                        }

                        //取得第47列,属性名称
                        attrCell=row.getCell(47,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var47=getCellValue(attrCell);
                            rowList.add(var47);
                        }else{
                            rowList.add("");
                        }

                        //取得第48列,属性名称
                        attrCell=row.getCell(48,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var48=getCellValue(attrCell);
                            rowList.add(var48);
                        }else{
                            rowList.add("");
                        }

                        //取得第49列,属性名称
                        attrCell=row.getCell(49,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var49=getCellValue(attrCell);
                            rowList.add(var49);
                        }else{
                            rowList.add("");
                        }

                        //取得第50列,属性名称
                        attrCell=row.getCell(50,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var50=getCellValue(attrCell);
                            rowList.add(var50);
                        }else{
                            rowList.add("");
                        }

                        //取得第51列,属性名称
                        attrCell=row.getCell(51,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var51=getCellValue(attrCell);
                            rowList.add(var51);
                        }else{
                            rowList.add("");
                        }

                        //取得第52列,属性名称
                        attrCell=row.getCell(52,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var52=getCellValue(attrCell);
                            rowList.add(var52);
                        }else{
                            rowList.add("");
                        }

                        //取得第53列,属性名称
                        attrCell=row.getCell(53,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var53=getCellValue(attrCell);
                            rowList.add(var53);
                        }else{
                            rowList.add("");
                        }

                        //取得第54列,属性名称
                        attrCell=row.getCell(54,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var54=getCellValue(attrCell);
                            rowList.add(var54);
                        }else{
                            rowList.add("");
                        }

                        //取得第55列,属性名称
                        attrCell=row.getCell(55,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var55=getCellValue(attrCell);
                            rowList.add(var55);
                        }else{
                            rowList.add("");
                        }

                        //取得第56列,属性名称
                        attrCell=row.getCell(56,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var56=getCellValue(attrCell);
                            rowList.add(var56);
                        }else{
                            rowList.add("");
                        }

                        //取得第57列,属性名称
                        attrCell=row.getCell(57,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var57=getCellValue(attrCell);
                            rowList.add(var57);
                        }else{
                            rowList.add("");
                        }

                        //取得第58列,属性名称
                        attrCell=row.getCell(58,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var58=getCellValue(attrCell);
                            rowList.add(var58);
                        }else{
                            rowList.add("");
                        }

                        //取得第59列,属性名称
                        attrCell=row.getCell(59,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var59=getCellValue(attrCell);
                            rowList.add(var59);
                        }else{
                            rowList.add("");
                        }

                        //取得第60列,属性名称
                        attrCell=row.getCell(60,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var60=getCellValue(attrCell);
                            rowList.add(var60);
                        }else{
                            rowList.add("");
                        }
                        //取得第61列,属性名称
                        attrCell=row.getCell(61,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var61=getCellValue(attrCell);
                            rowList.add(var61);
                        }else{
                            rowList.add("");
                        }
                        //取得第62列,属性名称
                        attrCell=row.getCell(62,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var62=getCellValue(attrCell);
                            rowList.add(var62);
                        }else{
                            rowList.add("");
                        }
                        //取得第63列,属性名称
                        attrCell=row.getCell(63,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if(attrCell!=null){
                            Object var63=getCellValue(attrCell);
                            rowList.add(var63);
                        }else{
                            rowList.add("");
                        }

                        excelList.add(rowList);
                    }
                }

            }
            logger.info("==================解析结束================");
            Map<String,Object> msg=new HashMap<>();
            if(errorList.size()!=0){
                msg.put("success",false);
                msg.put("data",errorList);
                return msg;
            }else{
                msg.put("success",true);
                msg.put("data",excelList);
                return msg;
            }
        }

        //获取列的值
        private Object getCellValue(Cell cell){
            Object cellValue=null;
            if(cell!=null){
                cell.setCellType(1);
                Object var4 = cell.getStringCellValue();
                cellValue = var4;
            }
            return  cellValue;
        }

        private String getErrorMessage(int x,int y,String field){
            String errormsg= "第" + x + "行，" +  (char)(y + 'A') + "列,"+field+"不能为空！";
            logger.error(errormsg);
            return errormsg;
        }



    public Map<String,Object> readItemValueExcel() throws IOException {
        logger.info("解析开始================");
        logger.info("总页数:"+numOfSheets);
        List<List<Object>> excelList=new ArrayList<List<Object>>();
        //错误记录list,抛至前台显示
        List<String> errorList=new ArrayList<>();

        for(int i=currSheet;i<numOfSheets;i++){
            Sheet sheet=workbook.getSheetAt(i);
            int rowNum=sheet.getLastRowNum()+1;
            int columnsNum=sheet.getRow(currPosition-1).getLastCellNum();
            logger.info("第"+(i+1)+"页行数:"+rowNum);
            for(int j=currPosition;j<rowNum;j++){
                Cell attrCell=null;
                //获得当前行
                Row row = sheet.getRow(j);
                if(row!=null&&!(row.equals(""))){
                    List<Object> rowList=new ArrayList<Object>();

                    //取得第0列,物料编码
                    attrCell=row.getCell(0,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var3=getCellValue(attrCell);
                        rowList.add(var3);
                    }else{
                        errorList.add(getErrorMessage(j+1,0,excelHeader[0]));
                    }

                    //取得第1列,选配值标记
                    attrCell=row.getCell(1);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var1=getCellValue(attrCell);
                        rowList.add(var1);
                    }else{
                        rowList.add("N");
                    }

                    //取得第2列,选配项标记
                    attrCell=row.getCell(2);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var2=getCellValue(attrCell);
                        rowList.add(var2);
                    }else{
                        rowList.add("N");
                    }

                    //取得第3列,必选项标记
                    attrCell=row.getCell(3);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var3=getCellValue(attrCell);
                        rowList.add(var3);
                    }else{
                        rowList.add("N");
                    }

                    //取得第3列,前台可见标识
                    attrCell=row.getCell(4);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var4=getCellValue(attrCell);
                        rowList.add(var4);
                    }else{
                        rowList.add("N");
                    }

                    //取得第5列,A0001
                    attrCell=row.getCell(5);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        String var5= String.valueOf(getCellValue(attrCell));
                        rowList.add(var5);
                    }else{
                        rowList.add("");
                    }

                    //取得第6列,A0002
                    attrCell=row.getCell(6);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var6=getCellValue(attrCell);
                        rowList.add(var6);
                    }else{
                        rowList.add("");
                    }

                    //取得第7列,A0003
                    attrCell=row.getCell(7,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var7=getCellValue(attrCell);
                        rowList.add(var7);
                    }else{
                        rowList.add("");
                    }

                    //取得第8列,A0004
                    attrCell=row.getCell(8,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var8=getCellValue(attrCell);
                        rowList.add(var8);
                    }else{
                        rowList.add("");
                    }

                    //取得第9列,A0005
                    attrCell=row.getCell(9);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var9=getCellValue(attrCell);
                        rowList.add(var9);
                    }else{
                        rowList.add("");
                    }

                    //取得第10列,A0006
                    attrCell=row.getCell(10,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var10=getCellValue(attrCell);
                        rowList.add(var10);
                    }else{
                        rowList.add("");
                    }

                    //取得第11列,A0007
                    attrCell=row.getCell(11,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var11=getCellValue(attrCell);
                        rowList.add(var11);
                    }else{
                        rowList.add("");
                    }

                    //取得第12列,A0008
                    attrCell=row.getCell(12,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var12=getCellValue(attrCell);
                        rowList.add(var12);
                    }else{
                        rowList.add("");
                    }

                    //取得第13列,A0009
                    attrCell=row.getCell(13);
                    if(attrCell!=null&&attrCell.toString()!=""){
                        Object var13=getCellValue(attrCell);
                        rowList.add(var13);
                    }else{
                        rowList.add("");
                    }

                    //取得第14列,A00010
                    attrCell=row.getCell(14,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var14=getCellValue(attrCell);
                        rowList.add(var14);
                    }else{
                        rowList.add("");
                    }

                    //取得第15列,A00011
                    attrCell=row.getCell(15,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var15=getCellValue(attrCell);
                        rowList.add(var15);
                    }else{
                        rowList.add("");
                    }

                    //取得第16列,A00012
                    attrCell=row.getCell(16,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var16=getCellValue(attrCell);
                        rowList.add(var16);
                    }else{
                        rowList.add("");
                    }

                    //取得第17列,A00013
                    attrCell=row.getCell(17,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var17=getCellValue(attrCell);
                        rowList.add(var17);
                    }else{
                        rowList.add("");
                    }
                    //取得第18列,A00014
                    attrCell=row.getCell(18,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var18=getCellValue(attrCell);
                        rowList.add(var18);
                    }else{
                        rowList.add("");
                    }
                    //取得第19列,A0015
                    attrCell=row.getCell(19,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var19=getCellValue(attrCell);
                        rowList.add(var19);
                    }else{
                        rowList.add("");
                    }
                    //取得第20列,A0016
                    attrCell=row.getCell(20,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var20=getCellValue(attrCell);
                        rowList.add(var20);
                    }else{
                        rowList.add("");
                    }

                    //取得第21列,A0017
                    attrCell=row.getCell(21,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var21=getCellValue(attrCell);
                        rowList.add(var21);
                    }else{
                        rowList.add("");
                    }

                    //取得第22列,A0018
                    attrCell=row.getCell(22,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var22=getCellValue(attrCell);
                        rowList.add(var22);
                    }else{
                        rowList.add("");
                    }

                    //取得第23列,A0019
                    attrCell=row.getCell(23,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var23=getCellValue(attrCell);
                        rowList.add(var23);
                    }else{
                        rowList.add("");
                    }

                    //取得第24列,A0020
                    attrCell=row.getCell(24,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var24=getCellValue(attrCell);
                        rowList.add(var24);
                    }else{
                        rowList.add("");
                    }

                    //取得第25列,A0021
                    attrCell=row.getCell(25,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var25=getCellValue(attrCell);
                        rowList.add(var25);
                    }else{
                        rowList.add("");
                    }

                    //取得第26列,A0022
                    attrCell=row.getCell(26,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var26=getCellValue(attrCell);
                        rowList.add(var26);
                    }else{
                        rowList.add("");
                    }

                    //取得第27列,A0023
                    attrCell=row.getCell(27,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var27=getCellValue(attrCell);
                        rowList.add(var27);
                    }else{
                        rowList.add("");
                    }

                    //取得第28列,A0024
                    attrCell=row.getCell(28,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var28=getCellValue(attrCell);
                        rowList.add(var28);
                    }else{
                        rowList.add("");
                    }

                    //取得第29列,A0025
                    attrCell=row.getCell(29,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(attrCell!=null){
                        Object var29=getCellValue(attrCell);
                        rowList.add(var29);
                    }else{
                        rowList.add("");
                    }
                    excelList.add(rowList);
                }
            }

        }
        logger.info("==================解析结束================");
        Map<String,Object> msg=new HashMap<>();
        if(errorList.size()!=0){
            msg.put("success",false);
            msg.put("data",errorList);
            return msg;
        }else{
            msg.put("success",true);
            msg.put("data",excelList);
            return msg;
        }
    }
}
