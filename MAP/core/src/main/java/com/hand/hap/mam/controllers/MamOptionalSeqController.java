package com.hand.hap.mam.controllers;
/**
 * @author qiang.wang01@hand-china.com
 * @version 0.1
 * @name MamOptionalSeqController
 * @description 选配顺序维护
 * @date 2017/7/19
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamOptionalSeq;
import com.hand.hap.mam.service.IMamOptionalSeqService;
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
public class MamOptionalSeqController extends BaseController {

    @Autowired
    private IMamOptionalSeqService service;

    /**
     * 序列查询函数
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/optional/seq/query")
    @ResponseBody
    public ResponseData query(MamOptionalSeq dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectOptionalSeq(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/optional/seq/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MamOptionalSeq> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData=new ResponseData();
        try{
            responseData.setRows(service.optionalSeqbatchUpdate(requestCtx, dto));
            responseData.setSuccess(true);
        }catch(RuntimeException e){
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/hap/mam/optional/seq/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamOptionalSeq> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查找选配顺序，模拟选配显示
     * @author yanjie.zhang@hand-china.com
     * @date 2017/07/24
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/optional/seq/selectOption")
    @ResponseBody
    public ResponseData selectOption(MamOptionalSeq dto,HttpServletRequest request){
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectOption(requestContext,dto));
    }
}