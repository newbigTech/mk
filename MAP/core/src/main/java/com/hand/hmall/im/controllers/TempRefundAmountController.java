package com.hand.hmall.im.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 临时Controller，作为修复历史数据的入口
 * Created by qinzhipeng on 2017/9/25.
 */
@Controller
public class TempRefundAmountController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/api/public/hmall/im/updateRefundAmount")
    @ResponseBody
    public ResponseData getProductInfo(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest iRequest = this.createRequestContext(request);
        Order order = new Order();
        List<Order> orderList = orderService.select(iRequest, order, 1, Integer.MAX_VALUE);
        for (Order orderInfo : orderList) {
            orderInfo.setRefundAmount(orderService.getTotalRefundAmount(iRequest, orderInfo).getRefundAmount());
            orderService.updateByPrimaryKeySelective(iRequest, orderInfo);
        }
        responseData.setResp(orderList);
        return responseData;

    }


}
