package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamApproveHis;
import com.hand.hap.mam.service.IMamApproveHisService;
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
public class MamApproveHisController extends BaseController {

    @Autowired
    private IMamApproveHisService service;


    @RequestMapping(value = "/hap/mam/approve/his/query")
    @ResponseBody
    public ResponseData query(MamApproveHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/approve/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MamApproveHis> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/mam/approve/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamApproveHis> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 获取审核次数
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/approve/his/getReviewNum")
    @ResponseBody
    public ResponseData getReviewNum(MamApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getAll(requestContext, dto));
    }

    /**
     * 查询主推审核记录
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/approve/his/queryHistory")
    @ResponseBody
    public ResponseData queryHistory(MamApproveHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryHistory(requestContext, dto, page, pageSize));
    }
}