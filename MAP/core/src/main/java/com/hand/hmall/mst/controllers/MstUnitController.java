package com.hand.hmall.mst.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.MstUnit;
import com.hand.hmall.mst.service.IMstUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @author xiaoyu.ran
 * @version 0.1
 * @name:
 * @Description: 商品单位维护对象的Controller层
 * @date 2017/9/5 14:54
 */
@Controller
public class MstUnitController extends BaseController {

    @Autowired
    private IMstUnitService service;


    @RequestMapping(value = "/hmall/mst/unit/query")
    @ResponseBody
    public ResponseData query(MstUnit dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/unit/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MstUnit> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/unit/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstUnit> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}