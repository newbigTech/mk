package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamAuditUser;
import com.hand.hap.mam.service.IMamAuditUserService;
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
/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name MamAuditUserController
 * @description 审核组用户关系controller层
 * @date 2017/10/26
 */
@Controller
public class MamAuditUserController extends BaseController {

    @Autowired
    private IMamAuditUserService service;


    /**
     * 查询审核组用户关系
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/audit/user/query")
    @ResponseBody
    public ResponseData query(MamAuditUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectAuditUser(requestContext, dto, page, pageSize));
    }

    /**
     * 审核组用户关系新建更新
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/audit/user/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MamAuditUser> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        List<MamAuditUser> MamAuditUserList;
        ResponseData responseData = new ResponseData();
        try{
            MamAuditUserList=service.batchUpdateAccountLines(requestCtx, dto);
            responseData.setRows(MamAuditUserList);
            responseData.setSuccess(true);
        }catch(Exception e){
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 审核组用户关系删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/audit/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamAuditUser> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}