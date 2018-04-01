package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamSoApproveHis;
import com.hand.hap.mam.service.IMamSoApproveHisService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.PaginatedList;
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

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name MamSoApproveHisController
 * @description 审核记录CRUD的controller类
 * @date 2017/8/09
 */
@Controller
public class MamSoApproveHisController extends BaseController {

    @Autowired
    private IMamSoApproveHisService service;


    @RequestMapping(value = "/hap/mam/so/approve/his/query")
    @ResponseBody
    public ResponseData query(MamSoApproveHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryHistory(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/so/approve/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MamSoApproveHis> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/mam/so/approve/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamSoApproveHis> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hap/mam/so/approve/his/queryHistory")
    @ResponseBody
    public ResponseData queryHistory(MamSoApproveHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        if ("ALL".equals(dto.getBrand())) {
            dto.setBrand(null);
        }
        PaginatedList<MamSoApproveHis> list = service.queryHistory(requestContext, dto, page, pageSize);
        // 解决BaseDTO中的creationDate字段标记了@JsonIgnore，导致创建时间字段无法传递给页面的问题
        for (MamSoApproveHis his : (List<MamSoApproveHis>) list.getRows()) {
            his.setCreationTime(his.getCreationDate());
        }
        return new ResponseData(list);
    }

    /**
     * 获取审核次数
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/so/approve/his/getReviewNum")
    @ResponseBody
    public ResponseData getReviewNum(MamSoApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getAll(requestContext, dto));
    }
}