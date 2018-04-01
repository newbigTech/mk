package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Brand;
import com.hand.hmall.mst.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 品牌对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class BrandController extends BaseController {

    @Autowired
    private IBrandService service;


    @RequestMapping(value = "/hmall/mst/brand/query")
    @ResponseBody
    public ResponseData query(Brand dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/brand/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Brand> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/brand/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Brand> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}