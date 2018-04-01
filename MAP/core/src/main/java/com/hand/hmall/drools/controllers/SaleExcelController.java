package com.hand.hmall.drools.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.util.ExcelUtil;
import com.hand.hmall.drools.dto.ReadExcelUtil;
import com.hand.hmall.dto.Common;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.ReadExcel;
import com.hand.hmall.util.Util;
import freemarker.template.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/22.
 * @desp 商品导入界面
 */
@Controller
@RequestMapping("/sale/excel")
public class SaleExcelController extends BaseController {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;
    @Value("#{configProperties['modelUri']}")
    private String modelUri;
    @Autowired
    private ICatalogversionService catalogversionService;
    @Autowired
    private IProductService productService;

    /**
     * 查询已导入的商品组
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByCondition",method = RequestMethod.POST)
    public ResponseData queryByCondition(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map){
        try{
            String url = "/h/sale/excel/queryByCondition";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 查询已导入的用户数据
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/query",method = RequestMethod.POST)
    public ResponseData userQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map){
        try{
            String url = "/h/sale/excel/user/query";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 上传用户数据
     * @param httpServletRequest
     * @param file
     * @return
     */
    @RequestMapping(value = "/user/upload",method = RequestMethod.POST)
    public ResponseData userUpload(HttpServletRequest httpServletRequest, @RequestParam(value="filename")  MultipartFile file){
        try{
            List<Map<String,Object>> userList= this.getUserList(file);
            String name=file.getOriginalFilename();

            Map<String,Object> map=new HashMap<>();
            map.put("excelName",name);
            map.put("userList",userList);

            String url = "/h/sale/excel/user/upload";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**、删除错误的导入信息（错误的用户导入信息）
     * @param httpServletRequest
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/user/deleteError",method = RequestMethod.GET)
    public ResponseData userDeleteError(HttpServletRequest httpServletRequest, @RequestParam(value="excelId") String excelId){
        try{

            String url = "/h/sale/excel/user/deleteError?excelId="+excelId;
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.GET,null, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 根据导入的用户的Excel id（主键）查询excel信息 方法已启用
     * @param httpServletRequest
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/user/selectByExcelId",method = RequestMethod.GET)
    public ResponseData userSelectByExcelId(HttpServletRequest httpServletRequest, @RequestParam(value="excelId") String excelId){
        try{

            String url = "/h/sale/excel/user/selectByExcelId?excelId="+excelId;
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.GET,null, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 获取上传的Excel中的用户数据
     * @param file
     * @return
     */
    public List<Map<String, Object>> getUserList(MultipartFile file){
        //判断文件是否为空
        if(file==null) return null;
        //获取文件名
        String name=file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(name==null || ("").equals(name) && size==0) return null;

        List<Map<String,Object>> list = new ArrayList<>();
        if(file!=null){
            try {
                String fileName = Util.getPostfix(file.getOriginalFilename());
                if(Common.OFFICE_EXCEL_2003_POSTFIX.equals(fileName)){
                    list = new ReadExcel().readXlsWithFile_Drools_User(file);
                }else if(Common.OFFICE_EXCEL_2010_POSTFIX.equals(fileName)){
                    list = new ReadExcel().readXlsxWithFile_Drools_User(file);
                }
            } catch (IOException e) {
                throw new RuntimeException("Excle文件解析失败！");
            }
        }
        return list;
    }


    /**
     * 查询从Excel中导入的用户数据
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/query",method = RequestMethod.POST)
    public ResponseData productQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map){
        try{
            String url = "/h/sale/excel/product/query";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 上传文件
     * @param httpServletRequest
     * @param file
     * @return
     */
    @RequestMapping(value = "/product/upload",method = RequestMethod.POST)
    public ResponseData productUpload(HttpServletRequest httpServletRequest,@RequestParam(value="filename")  MultipartFile file){
        try{
            List<Map<String,Object>> productList= this.getProductList(file);

            Map<String, String> paramMap = new HashMap <>();
            paramMap.put("code", "markor");
            paramMap.put("catalogversion", "online");
            Long catalogversionId = catalogversionService.getOnlineCatalogversionId(paramMap);
            Assert.notNull(catalogversionId, "Markor Online目录版本不存在");
            Product paramProduct = new Product();
            List<Product> resultProduct = null;
            paramProduct.setCatalogversionId(catalogversionId);

            Assert.notNull(productList, "Markor Online版本中不存在导入的商品.");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map<String, Object> map: productList){
                paramProduct.setCode((String)map.get("productCode"));
                resultProduct = productService.selectByCodeAndCatalogVersion(paramProduct);
                if(resultProduct.get(0).getProductId() == null || resultProduct.get(0).getProductId() <= 0){
                    map.put("isSuccess", "N");
                    map.put("errorMsg", "商品编码为："+map.get("productCode")+"的商品ID不存在");
                    continue;
                }else{
                    map.put("productId", resultProduct.get(0).getProductId());
                }
                if(resultProduct.get(0).getName() == null || ("").equals(resultProduct.get(0).getName())){
                    map.put("isSuccess", "N");
                    map.put("errorMsg", "商品编码为："+map.get("productCode")+"的商品名不存在");
                }else{
                    map.put("name", resultProduct.get(0).getName());
                }
            }
            String name=file.getOriginalFilename();
            Map<String,Object> map=new HashMap<>();
            map.put("excelName",name);
            map.put("productList",productList);

            String url = "/h/sale/excel/product/upload";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            ResponseData responseData=new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("读取数据错误"+e.getLocalizedMessage());
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     *删除商品导入的错误信息
     * @param httpServletRequest
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/product/deleteError",method = RequestMethod.GET)
    public ResponseData productDeleteError(HttpServletRequest httpServletRequest, @RequestParam(value="excelId") String excelId){
        try{

            String url = "/h/sale/excel/product/deleteError?excelId="+excelId;
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.GET,null, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 查询Excel中导入的商品信息
     * @param httpServletRequest
     * @param excelId
     * @return
     */
    @RequestMapping(value = "/product/selectByExcelId",method = RequestMethod.GET)
    public ResponseData productSelectByExcelId(HttpServletRequest httpServletRequest, @RequestParam(value="excelId") String excelId){
        try{

            String url = "/h/sale/excel/product/selectByExcelId?excelId="+excelId;
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.GET,null, com.hand.hmall.dto.ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 解析传入的商品组文件，获取商品Code集合
     *
     * @param file
     * @return
     */
    public List<Map<String, Object>> getProductList(MultipartFile file){
        //判断文件是否为空
        if(file==null) return null;
        //获取文件名
        String name=file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(name==null || ("").equals(name) && size==0) return null;

        List<Map<String,Object>> list = new ArrayList<>();
        if(file!=null){
            try {
                String fileName = Util.getPostfix(file.getOriginalFilename());
                if(Common.OFFICE_EXCEL_2003_POSTFIX.equals(fileName)){
                    list = new ReadExcelUtil().readXlsWithFile_Drools_Product(file);
                }else if(Common.OFFICE_EXCEL_2010_POSTFIX.equals(fileName)){
                    list = new ReadExcelUtil().readXlsxWithFile_Drools_Product(file);
                }
            } catch (IOException e) {
                throw new RuntimeException("Excle文件解析失败！");
            }
        }
        return list;
    }

    /**
     * 下载商品组导入模板
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = {"/product/downloadExcel"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String sheetName = "商品组商品导入";//sheet名
        String fileName = "商品组商品导入.xlsx";//文件名
        String[] nameArray = new String[]{"商品编码"};//字段数据，模板数据
        int[] lengthArray = new int[]{20};//表格中对应字段宽度

        /**简单下拉框的生成,生成模板方法中没有进行null判断，所以必须传值**/
        HashMap<Integer, String[]> dropDownMap = new HashMap();

        /***生成excel模板***/
        ExcelUtil.creatExecelTemplate(sheetName, fileName, nameArray, lengthArray, dropDownMap, response, request);
    }

}
