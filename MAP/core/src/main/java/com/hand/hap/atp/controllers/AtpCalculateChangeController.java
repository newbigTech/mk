package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpCalculateChange;
import com.hand.hap.atp.service.IAtpCalculateChangeService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name AtpCalculateChangeController
 * @description atp交期修改controller
 * @date 2017/11/05
 */
@Controller
public class AtpCalculateChangeController extends BaseController {

    @Autowired
    private IAtpCalculateChangeService service;

    /**
     * 查找交期修改数据
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/calculate/change/queryData")
    @ResponseBody
    public ResponseData query(AtpCalculateChange dto,  HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryData(requestContext,dto));
    }


    /**
     * 保存交期修改数据
     * @param atpCalculateChange
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/calculate/change/submitData")
    @ResponseBody
    public ResponseData submitData(@RequestBody AtpCalculateChange atpCalculateChange, BindingResult result, HttpServletRequest request) {

        IRequest requestCtx = createRequestContext(request);

        if(atpCalculateChange.getChangeId() != null){
            atpCalculateChange.set__status(DTOStatus.UPDATE);
            atpCalculateChange = service.updateByPrimaryKeySelective(requestCtx,atpCalculateChange);
        }else{
            atpCalculateChange.set__status(DTOStatus.ADD);
            atpCalculateChange.setObjectVersionNumber(1L);
            atpCalculateChange = service.insert(requestCtx,atpCalculateChange);
        }
        List<AtpCalculateChange> atpCalculateChanges = new ArrayList<>();
        atpCalculateChanges.add(atpCalculateChange);

        return new ResponseData(atpCalculateChanges);
    }
}