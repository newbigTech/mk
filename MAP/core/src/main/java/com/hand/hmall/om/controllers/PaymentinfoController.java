package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.service.IPaymentinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author peng.chen
 * @version 0.1
 * @name PaymentinfoController
 * @description 支付controller
 * @date 2017年5月25日18:53:32
 */
@Controller
public class PaymentinfoController extends BaseController {

    @Autowired
    private IPaymentinfoService service;


    @RequestMapping(value = "/hmall/om/paymentinfo/getInfoForBalance")
    @ResponseBody
    public ResponseData getInfoForBalance(Paymentinfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getInfoForBalance(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/paymentinfo/query")
    @ResponseBody
    public ResponseData query(Paymentinfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/paymentinfo/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Paymentinfo> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/paymentinfo/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Paymentinfo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}