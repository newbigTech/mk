package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmPromotionrule;
import com.hand.hmall.om.service.IOmPromotionruleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qinzhipeng on 2017/10/24.
 */
@Controller
public class OmPromotionruleController extends BaseController {
    @Autowired
    private IOmPromotionruleService service;

    @RequestMapping(value = "/hmall/om/promotionrule/query")
    @ResponseBody
    public ResponseData query(OmPromotionrule dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }
}