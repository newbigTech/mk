package com.hand.hmall.mst.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.AtpProductInvInfoExport;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.SyncData;
import com.hand.hmall.mst.service.IProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ProductController
 * @description 商品列表查询入口
 * @date 2017/5/26
 */
@Controller
public class ProductController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IProductService service;

    /**
     * 自动生成的查询商品列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/query")
    @ResponseBody
    public ResponseData query(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/product/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }

    /**
     * 用于促销的商品查询接口
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/sale/mst/product/query")
    @ResponseBody
    public com.hand.hmall.dto.ResponseData queryForSale(Product dto, @RequestBody Map map, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        int page = (Integer) map.get("page");
        int pageSize = (Integer) map.get("pageSize");
        List<Product> products = service.select(requestContext, dto, page, pageSize);
        Page productPage = (Page) products;
        com.hand.hmall.dto.ResponseData responseData = new com.hand.hmall.dto.ResponseData();
        responseData.setTotal((int) productPage.getTotal());
        return responseData;
    }

    /**
     * 根据基础商品Id查询商品列表，用于促销
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/sale/mst/product/queryProductByIds")
    @ResponseBody
    public com.hand.hmall.dto.ResponseData queryProductForsale(Product dto, @RequestBody Map map, HttpServletRequest request) {
        JSONObject mapObj = JSON.parseObject(JSON.toJSONString(map));
        com.alibaba.fastjson.JSONArray productIds = mapObj.getJSONArray("productIds");
        int page = (Integer) map.get("page");
        int pageSize = (Integer) map.get("pageSize");
        logger.info("----baseProductIds-----" + JSON.toJSONString(productIds) + "---page---" + page + "--------pageSize----" + pageSize);
        List respList = new ArrayList();
        for (int i = 0; i < productIds.size(); i++) {
            Product productDto = new Product();
            productDto.setProductId(productIds.getLong(i));

            Product productResp = service.selectByPrimaryKey(productDto, page, pageSize);
            respList.add(productResp);
        }
        return new com.hand.hmall.dto.ResponseData(respList);
    }

    /**
     * 根据基础商品Id查询商品列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/queryProduct")
    @ResponseBody
    public ResponseData queryProduct(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        Long baseProductId = Long.parseLong(request.getParameter("baseProductId"));
        return new ResponseData(service.queryProductList(baseProductId, dto, page, pageSize));
    }

    /**
     * @param dto      查询实体类
     * @param page
     * @param pageSize
     * @description 商品列表查询入口
     */
    @RequestMapping(value = "/hmall/mst/product/selectProductList")
    @ResponseBody
    public ResponseData selectProductList(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        List<Product> list = service.selectProductList(dto, page, pageSize);
        return new ResponseData(list);
    }

    /**
     * @description 商品列表中获取图片按钮，获取数据库中满足条件的商品
     */
    @RequestMapping(value = "/hmall/mst/product/selectProductForFetchMedia")
    @ResponseBody
    public ResponseData selectProductForFetchMedia() {
        List<Product> list = service.selectProductForFetchMedia();
        return new ResponseData(list);
    }

    /**
     * @param
     * @param page
     * @param pageSize
     * @description
     */
    @RequestMapping(value = "/hmall/mst/product/selectProductListByServiceCode")
    @ResponseBody
    public ResponseData selectProductListByServiceCode(String serviceOrderId, String excludeProductIds, String code, String name, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        Product dto = new Product();
        dto.setServiceOrderId(Long.valueOf(serviceOrderId));
        dto.setCode(code);
        dto.setName(name);
        List<String> exclude = new ArrayList<>();
        if (!excludeProductIds.equals("")) {
            String[] ids = excludeProductIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                exclude.add(ids[i]);
            }
            dto.setExcludeProductIds(exclude);
        }

        List<Product> list = service.selectProductListByServiceCode(dto, page, pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/hmall/mst/product/submit")
    @ResponseBody

    public ResponseData update(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/product/insert")
    @ResponseBody
    public Product insert(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        return service.insert(requestCtx, dto.get(0));
    }

    @RequestMapping(value = "/hmall/mst/product/update")
    @ResponseBody
    public Product updateByKey(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        return service.updateByPrimaryKey(requestCtx, dto.get(0));
    }

    @RequestMapping(value = "/hmall/mst/product/save")
    @ResponseBody
    public ResponseData save(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        List<Product> list = new ArrayList<Product>();
        if (dto != null && dto.size() > 0) {
            Product product = dto.get(0);
            //查询编码和版本组合是否重复
            Product p = new Product();
            p.setCode(product.getCode());
            p.setCatalogversionId(product.getCatalogversionId());
            List<Product> pList = service.selectByCodeAndCatalogVersion(p);
            if (product.getProductId() != null) {
                if (pList != null && pList.size() > 0) {
                    if (pList.get(0).getProductId().longValue() != product.getProductId().longValue()) {
                        ResponseData rd = new ResponseData(false);
                        Locale locale = RequestContextUtils.getLocale(request);
                        rd.setMessage(getMessageSource().getMessage("hmall.product.duplicate", null, locale));
                        return rd;
                    }
                }
                list.add(service.updateByPrimaryKeySelective(requestCtx, product));
            } else {
                if (pList != null && pList.size() > 0) {
                    ResponseData rd = new ResponseData(false);
                    Locale locale = RequestContextUtils.getLocale(request);
                    rd.setMessage(getMessageSource().getMessage("hmall.product.duplicate", null, locale));
                    return rd;
                }
                list.add(service.insertSelective(requestCtx, product));
            }
        }
        return new ResponseData(list);
    }

    @RequestMapping(value = "/hmall/mst/product/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Product> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 导出订单列表excel
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/exportData", method = RequestMethod.GET)
    public void exportData(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @RequestParam String name, @RequestParam String catalogversion, @RequestParam String startTime, @RequestParam String endTime) {
        Product dto = new Product();
        dto.setCode(code);
        dto.setName(name);
        dto.setCatalogversion(catalogversion);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //开始时间
        String sTime = "";
        if (!"".equals(startTime)) {
            Date sDate = new Date(startTime);
            sTime = sdf.format(sDate);
            dto.setCreationDateFrom(sTime);
        }
        //结束时间
        String eTime = "";
        if (!"".equals(endTime)) {
            Date eDate = new Date(endTime);
            eTime = sdf.format(eDate);
            dto.setCreationDateTo(eTime);
        }
        List<Product> list = service.selectProductList(dto);
        new ExcelUtil(Product.class).exportExcel(list, "商品列表", list.size(), request, response, "商品列表.xlsx");
    }

    /**
     * @description 商品同步
     */
    @RequestMapping(value = "/hmall/mst/product/synchronous")
    @ResponseBody
    public ResponseData synchronousProduct(@RequestParam String productId) {
        return new ResponseData();
    }

    /**
     * @description 商品失效
     */
    @RequestMapping(value = "/hmall/mst/product/unabled")
    @ResponseBody
    public ResponseData unabledProduct(@RequestParam String productId) {
        String[] productIdArray = productId.split(",");
        int a = service.unabledProduct(productIdArray);
        return new ResponseData();
    }


    /**
     * 下载商品excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(Product.class).downloadExcelModel(request, response, "template/product.xlsx");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 商品Excel导入
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        List<Product> list = null;
        try {
            list = new ExcelUtil<Product>(Product.class).importExcel(file.getOriginalFilename(), "商品列表", file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
        }
        if (CollectionUtils.isNotEmpty(list)) {
            try {
                String result = service.checkProduct(iRequest, list);
                if (StringUtils.isNotEmpty(result)) {
                    responseData.setSuccess(false);
                    responseData.setMessage(result);
                    return responseData;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            }

        }
        return responseData;
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @description 查询当前商品可选套件
     */
    @RequestMapping(value = "/hmall/mst/product/selectSuitProduct")
    @ResponseBody
    public ResponseData selectSuitProduct(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        List<Product> list = service.selectSuitProduct(dto, page, pageSize);
        return new ResponseData(list);
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @description 查询当前商品可选补件
     */
    @RequestMapping(value = "/hmall/mst/product/selectPatchProduct")
    @ResponseBody
    public ResponseData selectPatchProduct(Product dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        List<Product> list = service.selectPatchProduct(dto, page, pageSize);
        return new ResponseData(list);
    }


    @RequestMapping(value = "/hmall/mst/product/deleteRelation")
    @ResponseBody
    public ResponseData deleteRelation(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        if (dto != null && dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                service.deleteRelation(dto.get(i));
            }
        }
        return new ResponseData();
    }

    /**
     * 删除商品按钮
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/deleteProduct")
    @ResponseBody
    public ResponseData deleteProduct(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.deleteProduct(dto));
    }


    @RequestMapping(value = "/hmall/mst/product/selectCount")
    @ResponseBody
    public ResponseData selectCount(HttpServletRequest request, @RequestBody List<Product> dto) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectCount(requestContext, dto));
    }

    /**
     * 商品同步
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/sync")
    @ResponseBody
    public ResponseData sync(HttpServletRequest request, @RequestBody List<SyncData> dto) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.sync(requestContext, dto));
    }

    /**
     * 全量同步
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/batchSync")
    @ResponseBody
    public ResponseData batchSync(HttpServletRequest request, @RequestBody List<SyncData> dto) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.batchSync(requestContext, dto));
    }

    /**
     * 强制同步
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/forceSync")
    @ResponseBody
    public ResponseData forceSync(HttpServletRequest request, @RequestBody List<SyncData> dto) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.forceSync(requestContext, dto));
    }

    /**
     * 自动上架
     *
     * @param productList
     */
    @RequestMapping(value = "/hmall/mst/product/autoUpShelf")
    @ResponseBody
    public ResponseData autoUpShelf(@RequestBody List<Product> productList, @RequestParam(value = "odtype") String odtype) {
        ResponseData rd = new ResponseData();
        try {
            String resultInfo = service.autoUpShelf(productList, odtype);
            rd.setMessage(resultInfo);
        } catch (Exception e) {
            rd.setMessage(e.getMessage());
            rd.setSuccess(false);
            logger.error("自动上架失败：" + e.getMessage());
            e.printStackTrace();
        }
        return rd;
    }

    /**
     * 自动下架
     *
     * @param productList
     */
    @RequestMapping(value = "/hmall/mst/product/autoDownShelf")
    @ResponseBody
    public ResponseData autoDownShelf(@RequestBody List<Product> productList, @RequestParam(value = "odtype") String odtype) {
        ResponseData rd = new ResponseData();
        try {
            String resultInfo = service.autoDownShelf(productList, odtype);
            rd.setMessage(resultInfo);
        } catch (Exception e) {
            rd.setMessage(e.getMessage());
            rd.setSuccess(false);
            logger.error("自动下架失败：" + e.getMessage());
            e.printStackTrace();
        }
        return rd;
    }

    /**
     * 上架
     *
     * @param productList
     */
    @RequestMapping(value = "/hmall/mst/product/upShelf")
    @ResponseBody
    public ResponseData upShelf(@RequestBody List<Product> productList, @RequestParam(value = "odtype") String odtype) {
        ResponseData rd = new ResponseData();
        try {
            String resultInfo = service.upShelf(productList, odtype);
            rd.setMessage(resultInfo);
        } catch (Exception e) {
            rd.setMessage(e.getMessage());
            rd.setSuccess(false);
            logger.error("上架失败：" + e.getMessage());
            e.printStackTrace();
        }
        return rd;
    }

    /**
     * 下架
     *
     * @param productList
     */
    @RequestMapping(value = "/hmall/mst/product/downShelf")
    @ResponseBody
    public ResponseData downShelf(@RequestBody List<Product> productList, @RequestParam(value = "odtype") String odtype) {
        ResponseData rd = new ResponseData();
        try {
            String resultInfo = service.downShelf(productList, odtype);
            rd.setMessage(resultInfo);
        } catch (Exception e) {
            rd.setMessage(e.getMessage());
            rd.setSuccess(false);
            logger.error("下架失败：" + e.getMessage());
            e.printStackTrace();
        }
        return rd;
    }

    /**
     * 导出库存excel
     */
    @RequestMapping(value = "/hmall/mst/product/exportInvInfo", method = RequestMethod.GET)
    public void exportInvInfo(HttpServletRequest request, HttpServletResponse response) {
        List<AtpProductInvInfoExport> list = service.selectProcuctInvInfo();
        new ExcelUtil(AtpProductInvInfoExport.class).exportExcel(list, "可用库存", list.size(), request, response, "可用库存.xlsx");
    }

}