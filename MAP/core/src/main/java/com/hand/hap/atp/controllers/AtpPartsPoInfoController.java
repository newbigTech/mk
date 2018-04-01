package com.hand.hap.atp.controllers;
/**
 * @author qiang.wang01@hand-china.com
 * @version 0.1
 * @name AtpPartsPoInfoController
 * @description po零部件信息表controller
 * @date 2017/8/10
 */

import com.hand.hap.atp.dto.AtpPartsPoInfo;
import com.hand.hap.atp.service.IAtpPartsPoInfoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AtpPartsPoInfoController extends BaseController {

    @Autowired
    private IAtpPartsPoInfoService service;

    /**
     * 查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/po/info/query")
    @ResponseBody
    public ResponseData query(AtpPartsPoInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 提交
     *
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/po/info/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AtpPartsPoInfo> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}