package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.CategoryMapping;
import com.hand.hmall.mst.dto.ProductCategory;
import com.hand.hmall.mst.dto.SubCategory;
import com.hand.hmall.mst.dto.SyncData;
import com.hand.hmall.mst.service.ICategoryMappingService;
import com.hand.hmall.mst.service.IProductCategoryService;
import com.hand.hmall.mst.service.ISubcategoryService;
import com.hand.hmall.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhangyanan
 * @version 0.1
 * @name ProductCategoryController
 * @description 商品类别Controller
 * @date 2017/5/22
 */
@Controller
public class ProductCategoryController extends BaseController {

    @Autowired
    private IProductCategoryService service;
    @Autowired
    private ISubcategoryService subcategoryService;
    @Autowired
    private ICategoryMappingService categoryMappingService;


    /**
     * 查询商品类别列表(模糊查询)
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/mst/product/category/query")
    @ResponseBody
    public ResponseData query(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.getProductCategoryList(dto, page, pageSize));
    }

    /**
     * 根据商品和类别的映射关系查询类别
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/product/category/query")
    @ResponseBody
    public ResponseData queryMapping(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.selectProductCategoryList(dto, page, pageSize));
    }

    /**
     * 查询超类别列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mst/product/category/querySuperType")
    @ResponseBody
    public ResponseData querySuperType(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.querySuperType(dto, page, pageSize));
    }

    /**
     * 查询子类别列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mst/product/category/querySubType")
    @ResponseBody
    public ResponseData querySubType(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.querySubType(dto, page, pageSize));
    }

    /**
     * 根据类别ID查询商品列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mst/product/category/queryProductByCategoryId")
    @ResponseBody
    public ResponseData queryProductByCategoryId(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryProductByCategoryId(dto, page, pageSize));
    }

    /**
     * 根据类别ID查询查询超类别子类别都没有的其他类别
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mst/product/category/queryTypeNotInSuperAndSub")
    @ResponseBody
    public ResponseData queryTypeNotInSuperAndSub(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryTypeNotInSuperAndSub(dto, page, pageSize));
    }

    /**
     * @description 保存添加类别
     */
    @RequestMapping(value = "/mst/product/category/addType")
    @ResponseBody
    public String addType(HttpServletRequest request, @RequestParam String categoryId, @RequestParam String TypeId, @RequestParam String flag) {
        IRequest iRequest = this.createRequestContext(request);
        //类别ID
        String[] typeIdArray = TypeId.split(",");
        //超类别
        if (Constants.ONE.equals(flag)) {
            for (int i = 0; i < typeIdArray.length; i++) {
                try {
                    SubCategory sub = new SubCategory();
                    sub.setCategoryId(Long.parseLong(typeIdArray[i]));
                    sub.setSubCategoryId(Long.parseLong(categoryId));
                    subcategoryService.insertSelective(iRequest, sub);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //子类别
        else if (Constants.TWO.equals(flag)) {
            for (int i = 0; i < typeIdArray.length; i++) {
                try {
                    SubCategory sub = new SubCategory();
                    sub.setSubCategoryId(Long.parseLong(typeIdArray[i]));
                    sub.setCategoryId(Long.parseLong(categoryId));
                    subcategoryService.insertSelective(iRequest, sub);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }

    /**
     * 删除类别
     *
     * @param request
     * @param categoryId 当前类别ID
     * @param selectId   选中的类别ID
     * @param flag       1-超类别 2-子类别
     * @return
     */
    @RequestMapping(value = "/mst/product/category/delCategory")
    @ResponseBody
    public String delCategory(HttpServletRequest request, @RequestParam String categoryId, @RequestParam String selectId, @RequestParam String flag) {
        //类别ID
        String[] selectIds = selectId.split(",");
        //超类别
        if (Constants.ONE.equals(flag)) {
            List<SubCategory> list = subcategoryService.queryListByCategoryIdAndSubCategoryId(selectIds, categoryId);
            subcategoryService.batchDelete(list);
        }
        //子类别
        else if (Constants.TWO.equals(flag)) {
            List<SubCategory> list = subcategoryService.queryListBySubCategoryIdAndCategoryId(selectIds, categoryId);
            subcategoryService.batchDelete(list);
        }
        return "success";
    }

    /**
     * 删除商品
     *
     * @param request
     * @param categoryId 当前类别ID
     * @param selectId   选中的类别ID
     * @return
     */
    @RequestMapping(value = "/mst/product/category/delProduct")
    @ResponseBody
    public String delProduct(HttpServletRequest request, @RequestParam String categoryId, @RequestParam String selectId) {
        IRequest iRequest = this.createRequestContext(request);
        //类别ID
        String[] selectIds = selectId.split(",");
        List<CategoryMapping> list = categoryMappingService.queryListByProductIdAndCategoryId(selectIds, categoryId);
        categoryMappingService.batchDelete(list);
        return "success";

    }

    @RequestMapping(value = "/mst/product/category/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ProductCategory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 保存类别信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/mst/product/category/saveCategory")
    @ResponseBody
    public String saveCategory(HttpServletRequest request, @RequestBody List<ProductCategory> dto) {
        IRequest iRequest = this.createRequestContext(request);
        Long categoryId = dto.get(0).getCategoryId();
        ProductCategory obj = null;
        ProductCategory sobj = null;
        if (!"".equals(categoryId) && categoryId != null) {
            sobj = service.queryCategoryByCategoryIdAndVersion(dto.get(0));
            if (sobj == null) {
                obj = service.updateByPrimaryKeySelective(iRequest, dto.get(0));
                return obj.getCategoryId().toString();
            } else {
                return "fail";
            }
        } else {
            sobj = service.queryCategoryByCategoryIdAndVersion(dto.get(0));
            if (sobj == null) {
                obj = service.insertSelective(iRequest, dto.get(0));
                return obj.getCategoryId().toString();
            } else {
                return "fail";
            }
        }
    }

    /**
     * 根据标志位保存类别信息 并添加类别关系
     *
     * @param request
     * @param dto
     * @param flag    1-超类别 2-子类别
     * @return
     */
    @RequestMapping(value = "/mst/product/category/saveCategoryByFlag")
    @ResponseBody
    public String saveCategoryByFlag(HttpServletRequest request, @RequestBody ProductCategory dto, String flag) {
        IRequest iRequest = this.createRequestContext(request);
        if (dto.getCategoryId() != null) {
            //添加超类别
            if (Constants.ONE.equals(flag)) {
                long subCategoryId = dto.getCategoryId();
                ProductCategory obj = service.insertSelective(iRequest, dto);
                if (obj != null) {
                    //新增的类别ID
                    long categoryId = obj.getCategoryId();
                    //保存子类别映射表信息
                    SubCategory sub = new SubCategory();
                    sub.setSubCategoryId(subCategoryId);
                    sub.setCategoryId(categoryId);
                    subcategoryService.insertSelective(iRequest, sub);
                }
            }
            //添加子类别
            else if (Constants.TWO.equals(flag)) {
                long categoryId = dto.getCategoryId();
                ProductCategory obj = service.insertSelective(iRequest, dto);
                if (obj != null) {
                    //新增的类别ID
                    long subCategoryId = obj.getCategoryId();
                    //保存子类别映射表信息
                    SubCategory sub = new SubCategory();
                    sub.setSubCategoryId(subCategoryId);
                    sub.setCategoryId(categoryId);
                    subcategoryService.insertSelective(iRequest, sub);
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/mst/product/category/remove")
    @ResponseBody
    public ResponseData delete(@RequestBody List<ProductCategory> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    /**
     * 商品类别Excel导入
     */
    @RequestMapping(value = "/mst/product/category/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        List<ProductCategory> list = null;
        try {
            list = new ExcelUtil<ProductCategory>(ProductCategory.class).importExcel(file.getOriginalFilename(), "商品列表", file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
        }
        for (ProductCategory ProductCategory : list) {
            try {
                service.insertSelective(iRequest, ProductCategory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseData;
    }

    /**
     * 下载商品类别excel模板
     */
    @RequestMapping(value = "/mst/product/category/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        new ExcelUtil(ProductCategory.class).downloadExcelModel(request, response, "商品列表导入模板.xlsx", "商品列表");
    }

    @RequestMapping(value = "/mst/product/category/insert")
    @ResponseBody
    public ProductCategory insert(HttpServletRequest request, @RequestBody List<ProductCategory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return service.insert(requestCtx, dto.get(0));
    }

    @RequestMapping(value = "/mst/product/category/update")
    @ResponseBody
    public ProductCategory updateByKey(HttpServletRequest request, @RequestBody List<ProductCategory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return service.updateByPrimaryKey(requestCtx, dto.get(0));
    }

    /**
     * 根据类别ID查询未包含的商品
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mst/product/category/queryProductNotItself")
    @ResponseBody
    public ResponseData queryProductNotItself(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryProductNotItself(dto, page, pageSize));
    }

    /**
     * 保存商品信息 并添加商品关系
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/mst/product/category/saveProduct")
    @ResponseBody
    public String saveProduct(HttpServletRequest request, String categoryId, String productId) {
        IRequest iRequest = this.createRequestContext(request);
        String[] productIdArray = productId.split(",");
        if (categoryId != null) {
            for (int i = 0; i < productIdArray.length; i++) {
                //保存产品类别映射表信息
                CategoryMapping cateMapping = new CategoryMapping();
                cateMapping.setCategoryId(Long.parseLong(categoryId));
                cateMapping.setProductId(Long.parseLong(productIdArray[i]));
                categoryMappingService.insertSelective(iRequest, cateMapping);
            }
        }
        return "success";
    }

    /**
     * 类别同步
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/category/sync")
    @ResponseBody
    public ResponseData sync(HttpServletRequest request, @RequestBody List<SyncData> dto)
    {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.sync(requestContext, dto));
    }
}