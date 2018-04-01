package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Odtype;
import com.hand.hmall.mst.service.IOdtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
 * @Description: 频道对象controller
 * @date 2017/8/11 17:33
 */
@Controller
public class OdtypeController extends BaseController {

    @Autowired
    private IOdtypeService service;


    @RequestMapping(value = "/hmall/mst/odtype/query")
    @ResponseBody
    public ResponseData query(Odtype dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/odtype/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Odtype> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/odtype/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Odtype> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}