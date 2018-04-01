package com.hand.hap.mam.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MamPinVcodeController
 * @description  pin,vcode对照表controller
 * @date 2017/5/27
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamPinVcode;
import com.hand.hap.mam.service.IMamPinVcodeService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MamPinVcodeController extends BaseController {

    @Autowired
    private IMamPinVcodeService service;

    /**
     * pin,vcode查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/pin/vcode/query")
    @ResponseBody
    public ResponseData query(MamPinVcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * pin ,vode数据提交
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/pin/vcode/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MamPinVcode> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
}