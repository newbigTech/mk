/**/
package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpItemMakingInfo;
import com.hand.hap.atp.service.IAtpItemMakingInfoService;
import com.hand.hap.core.IRequest;
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
 * @name AtpItemMakingInfoController
 * @description 成品零部在制controller
 * @date 2017/6/30
 **/
@Controller
public class AtpItemMakingInfoController extends BaseController {

    @Autowired
    private IAtpItemMakingInfoService service;


    /**
     * 成品零部件信息查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/item/making/info/query")
    @ResponseBody
    public ResponseData query(AtpItemMakingInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }
}