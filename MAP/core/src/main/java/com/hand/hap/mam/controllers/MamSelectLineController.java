package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamSelectLine;
import com.hand.hap.mam.service.IMamSelectLineService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name MamSelectLineController
 * @description 模拟选配行CRUD的controller类
 * @date 2017/7/22
 */
@Controller
public class MamSelectLineController extends BaseController {

    @Autowired
    private IMamSelectLineService service;

    /**
     * 查找选配结果
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/line/query")
    @ResponseBody
    public ResponseData query(MamSelectLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectMamSelectLine(dto, requestContext, page, pageSize));
    }


}