package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OrderCouponrule;
import com.hand.hmall.om.service.IOrderCouponruleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: shoupeng.wei@hand-china.com
 * @Create at: 2017年9月9日14:13:44
 * @Description: 订单详情页优惠信息查询
 */
@Controller
public class OmCouponruleController extends BaseController {

    @Autowired
    private IOrderCouponruleService service;


    @RequestMapping(value = "/hmall/om/couponrule/query")
    @ResponseBody
    public ResponseData query(OrderCouponrule dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

}