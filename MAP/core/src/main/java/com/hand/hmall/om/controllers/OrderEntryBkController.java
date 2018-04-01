package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OrderEntryBk;
import com.hand.hmall.om.service.IOrderEntryBkService;
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
 * @date 2017/10/14 19:09
 */
@Controller
public class OrderEntryBkController extends BaseController {

    @Autowired
    private IOrderEntryBkService service;

    /**
     * @param dto
     * @param request
     * @return
     * @description 订单详情页面中查询订单行信息，不分页，查询所有
     */
    @RequestMapping(value = "/hmall/om/order/entry/bk/queryAllInfo")
    @ResponseBody
    public ResponseData queryAllInfo(OrderEntryBk dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, 1, Integer.MAX_VALUE));
    }

    /**
     * 发货单备份对应的发货单行查询
     * @param dto   请求参数，发货单ID
     * @return  发货单对应发货单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/bk/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(OrderEntryBk dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }
}
