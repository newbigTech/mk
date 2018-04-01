package com.hand.hmall.controller;

import com.hand.hmall.dto.RefundOrder;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ServiceOrder;
import com.hand.hmall.exception.AfterSaleException;
import com.hand.hmall.servie.IServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @name ServiceOrderController
 * @Describe 提供根据订单编码查询全部服务单信息功能
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@RestController
public class ServiceOrderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IServiceOrderService service;


    /**
     * 事后促销使用
     * @return
     */
    @PostMapping(value = "/serviceOrder/queryByOrderId")
    public ResponseData queryByOrderId(@RequestParam("orderId") Long orderId) {
        List<RefundOrder> byCondition = service.findByCondition(orderId);
        ResponseData data  = new ResponseData();
        data.setResp(byCondition);
        return data;
    }



    /**
     * 创建服务单
     *
     * @param serviceOrder
     * @return
     */
    @PostMapping(value = "/serviceOrder/create")
    public ResponseData createServiceOrder(@RequestBody ServiceOrder serviceOrder) {
        ResponseData response = new ResponseData();
        try {
            service.createServiceOrder(serviceOrder);
            response.setMsgCode("SUCCESS");
            response.setMsg("创建服务单成功");
        } catch (AfterSaleException e) {
            logger.error("创建服务单信息失败：" + e.getMessage());
            response.setSuccess(false);
            response.setMsgCode(e.getMsgCode());
            response.setMsg(e.getMessage());
        }
        return response;
    }

    /**
     * 根据订单编号查询服务单及相关售后单信息
     *
     * @param escOrderCode - esc订单编号
     * @param webSite      - 网站名称 TEST | TMALL
     * @param conditions_
     *     服务单行	退货单	退货单行	退款单	退款单行	服务销售单	服务销售单行	媒体信息
     *     SOE	    RT	    RTE	    RF	    RFE	    SS	        SSE	        M
     * @return
     */
    @GetMapping(value = "/serviceOrder/query")
    public ResponseData queryByOrderCode(@RequestParam("orderCode") String escOrderCode
            , @RequestParam(name = "webSite", defaultValue = "ZEST") String webSite
            , @RequestParam(name = "conditions", defaultValue = "SOE,RT,RTE,RF,RFE,SS,SSE,M") String conditions_) {

        List<String> conditions = Arrays.asList(conditions_.toUpperCase().split(","));
        List<ServiceOrder> serviceOrders;
        try {
            serviceOrders = service.queryServiceOrders(escOrderCode, webSite, conditions);
        } catch (AfterSaleException e) {
            ResponseData response = new ResponseData(false);
            response.setMsgCode(e.getMsgCode());
            response.setMsg(e.getMessage());
            return response;
        }
        if (serviceOrders.isEmpty()) {
            ResponseData response = new ResponseData(false);
            response.setMsgCode("not.found.serviceOrder");
            response.setMsg("根据订单编号[" + escOrderCode + "]和网站[" + webSite + "]查不到服务单");
            return response;
        } else {

            ResponseData response = new ResponseData(serviceOrders);
            response.setMsgCode("SUCCESS");
            response.setMsg("查询服务单成功");
            response.setTotal(serviceOrders.size());
            return response;

        }

    }

}
