package com.hand.hap.atp.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name AtpInvSourceInfoController
 * @description  Atp寻源路径信息
 * @date 2017/6/19
 */

import com.hand.hap.atp.dto.AtpInvSourceInfo;
import com.hand.hap.atp.service.IAtpInvSourceInfoService;
import com.hand.hap.core.IRequest;
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
public class AtpInvSourceInfoController extends BaseController {

    @Autowired
    private IAtpInvSourceInfoService service;

    /**
     * 寻源信息查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/inv/source/info/query")
    @ResponseBody
    public ResponseData query(AtpInvSourceInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.selectSourceInfo(page, pageSize,dto));
    }

    /**
     * 提交，保存，更新综合类
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/atp/inv/source/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpInvSourceInfo> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData=new ResponseData();
        List<AtpInvSourceInfo> atpInvSourceInfoList;
        try{
            atpInvSourceInfoList=service.batchUpdateSourceInfo(requestCtx, dto);
            responseData.setRows(atpInvSourceInfoList);
            responseData.setSuccess(true);
        }catch(Exception e){
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    @RequestMapping(value = "/hap/atp/inv/source/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AtpInvSourceInfo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

}