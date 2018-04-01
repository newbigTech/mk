package com.hand.hmall.fnd.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.fnd.dto.FndGlobalvariant;
import com.hand.hmall.fnd.service.IFndGlobalvariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class FndGlobalvariantController extends BaseController {

    @Autowired
    private IFndGlobalvariantService service;


    /**
     *  查询全局变量表 hmall_fnd_globalvariant
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/fnd/globalvariant/query")
    @ResponseBody
    public ResponseData query(FndGlobalvariant dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    /**
     * 修改全局变量表 hmall_fnd_globalvariant行数据
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/fnd/globalvariant/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FndGlobalvariant> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 删除全局变量表 hmall_fnd_globalvariant 行数据
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/fnd/globalvariant/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<FndGlobalvariant> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}