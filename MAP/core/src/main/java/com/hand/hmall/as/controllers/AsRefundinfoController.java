package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsRefundinfo;
import com.hand.hmall.as.service.IAsRefundinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shoupeng.wei
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/11/3 9:08
 */
@RequestMapping(value = "/hmall/as/refundinfo")
@Controller
public class AsRefundinfoController extends BaseController {

    @Autowired
    private IAsRefundinfoService service;

    @RequestMapping(value = "/getInfoForBalance")
    @ResponseBody
    public ResponseData getInfoForBalance(HttpServletRequest request, AsRefundinfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize){
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getInfoForBalance(requestContext, dto, page, pageSize));
    }
}
