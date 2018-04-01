package com.hand.hap.im.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImPartsInvInface;
import com.hand.hap.im.service.IImPartsInvInfaceService;
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
 * @name ImPartsInvInfaceController
 * @description 零部件可用库存controller
 * @date 2017/6/20
 */
@Controller
public class ImPartsInvInfaceController extends BaseController {

    @Autowired
    private IImPartsInvInfaceService service;


    /**
     * 零部件可用库存接口查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/parts/inv/inface/query")
    @ResponseBody
    public ResponseData query(ImPartsInvInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }
}