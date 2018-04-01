package com.markor.order.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.common.exception.InvalidDataException;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.oms.art241.order.dto.ArtOrder;
import com.markor.map.oms.art241.order.dto.ArtOrderEntry;
import com.markor.map.oms.art241.order.dto.OrderEntryUpdateDto;
import com.markor.map.oms.art241.order.service.IARTOrderServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ArtOrderController
 * @description ART订单Controller, 提供页面查询功能
 * @date 2018/2/28
 */
@RestController
public class ArtOrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ArtOrderController.class);
    @Autowired
    private IARTOrderServiceProvider iARTOrderServiceProvider;

    /**
     * ART订单详情查询
     *
     * @param poNumber
     * @return ART订单列表ResponseData
     */
    @RequestMapping(value = "/markor/art241/order/queryArtOrderByMkPoNumber")
    @ResponseBody
    public ResponseData queryArtOrderByMkPoNumber(HttpServletRequest request, @RequestParam("poNumber") String poNumber) {
        List<ArtOrder> list = new ArrayList<ArtOrder>();
        PaginatedList<ArtOrder> paginatedList = new PaginatedList();
        ArtOrder artOrder = iARTOrderServiceProvider.getArtOrderByPoNumber(poNumber);
        list.add(artOrder);
        paginatedList.setRows(list);
        return new ResponseData(paginatedList);
    }

    /**
     * 根据ART订单ID查询订单行信息
     *
     * @param orderId
     * @return ART订单列表ResponseData
     */
    @RequestMapping(value = "/markor/art241/order/queryEntryByOrderId")
    @ResponseBody
    public ResponseData queryEntryByOrderId(@RequestParam("orderId") String orderId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        PaginatedList<ArtOrderEntry> artOrderEntryPaginatedList = iARTOrderServiceProvider.queryArtOrderEntries(orderId, page, pageSize);
        return new ResponseData(artOrderEntryPaginatedList);
    }

    /**
     * ART订单取消确认
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/markor/art241/order/confirmCancel")
    @ResponseBody
    public ResponseData confirmCancel(@RequestParam("orderId") String orderId) {
        ResponseData responseData = new ResponseData();
        try {
            iARTOrderServiceProvider.confirmCancel(orderId);
            responseData.setSuccess(true);
        } catch (BusinessException e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * ART订单取消确撤回
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/markor/art241/order/withdrawCancel")
    @ResponseBody
    public ResponseData withdrawCancel(@RequestParam("orderId") String orderId) {
        ResponseData responseData = new ResponseData();
        try {
            iARTOrderServiceProvider.withdrawCancel(orderId);
            responseData.setSuccess(true);
        } catch (BusinessException e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        } catch (InvalidDataException e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        } catch (IOException e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
    /**
     * 修改交期
     *
     * @param coreCode
     * @return
     */
    @RequestMapping(value = "/markor/art241/order/updateDelivery")
    @ResponseBody
    public ResponseData updateDelivery(@RequestParam("coreCode") String coreCode,@RequestBody List<OrderEntryUpdateDto> list) {
        ResponseData responseData = new ResponseData();
        try {
            iARTOrderServiceProvider.updateProductETADate(coreCode, list);
            responseData.setSuccess(true);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 修改价格
     *
     * @param coreCode
     * @return
     */
    @RequestMapping(value = "/markor/art241/order/updatePrice")
    @ResponseBody
    public ResponseData updatePrice(@RequestParam("coreCode") String coreCode) {
        ResponseData responseData = new ResponseData();
        try {
            iARTOrderServiceProvider.updateMKSalesPrice(coreCode);
            responseData.setSuccess(true);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     *重新执行订单流程
     *
     * @param mkOrderId
     * @return
     */
    @RequestMapping(value = "/markor/art241/order/restartOrderProcess")
    @ResponseBody
    public ResponseData  restartOrderProcess(@RequestParam("mkOrderId") String mkOrderId) {
        ResponseData responseData = new ResponseData();
        try {
            iARTOrderServiceProvider.restartOrderProcess(mkOrderId);
            responseData.setSuccess(true);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}

