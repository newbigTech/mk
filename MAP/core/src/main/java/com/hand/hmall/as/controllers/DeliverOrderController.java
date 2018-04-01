package com.hand.hmall.as.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.DeliverOrder;
import com.hand.hmall.as.service.IDeliverOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenzhigang
 * @version 0.1
 * @name DeliverOrderController
 * @description 送货单Controller
 * @date 2017/7/30
 */
@Controller
public class DeliverOrderController extends BaseController {

    @Autowired
    private IDeliverOrderService deliverOrderService;

    /**
     * 根据订单ID查询其对应的全部商品
     * @param orderId - 订单ID
     * @return
     */
    @RequestMapping(value = "/hmall/as/deliverOrder/queryOrderProducts")
    @ResponseBody
    public ResponseData queryProductsByOrderId(@RequestParam("orderId") long orderId) {
        return new ResponseData(deliverOrderService.queryProductsByOrderId(orderId));
    }

    /**
     * 保存送货单对象
     * @param deliverOrder - 送货单对象
     * @return
     */
    @RequestMapping(value = "/hmall/as/deliverOrder/save")
    @ResponseBody
    public ResponseData save(@RequestBody DeliverOrder deliverOrder) {
        deliverOrderService.save(deliverOrder);
        return new ResponseData(true);
    }


}
