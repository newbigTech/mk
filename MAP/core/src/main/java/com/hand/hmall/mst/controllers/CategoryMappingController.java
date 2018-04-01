package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.CategoryMapping;
import com.hand.hmall.mst.service.ICategoryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name CategoryMappingController
 * @description 类别映射
 * @date 2017年5月26日10:52:23
 */
@Controller
public class CategoryMappingController extends BaseController {

    @Autowired
    private ICategoryMappingService service;


    @RequestMapping(value = "/hmall/mst/category/mapping/query")
    @ResponseBody
    public ResponseData query(CategoryMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/category/mapping/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<CategoryMapping> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/category/mapping/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<CategoryMapping> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 根据productID和categoryID来删除类别映射表中的映射关系
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/category/mapping/delete")
    @ResponseBody
    public ResponseData deleteCategoryMapping(HttpServletRequest request, @RequestBody List<CategoryMapping> dto) {
        if (dto != null && dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                service.deleteCategoryMapping(dto.get(i));
            }
        }
        return new ResponseData();
    }
}