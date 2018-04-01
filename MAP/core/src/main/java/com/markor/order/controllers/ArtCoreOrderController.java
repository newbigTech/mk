package com.markor.order.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.oms.base.salesorder.entities.SalesOrder;
import com.markor.map.oms.base.salesorder.entities.SalesOrderEntry;
import com.markor.map.oms.base.salesorder.entities.SalesOrderPojo;
import com.markor.map.oms.base.salesorder.service.ISalesOrderEntryServiceProvider;
import com.markor.map.oms.base.salesorder.service.ISalesOrderServiceProvider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ArtOrderController
 * @description ART核心订单列表Controller, 提供页面查询功能
 * @date 2018/2/11
 */
@RestController
public class ArtCoreOrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ArtCoreOrderController.class);
    @Autowired
    private ISalesOrderServiceProvider iSalesOrderServiceProvider;
    @Autowired
    private ISalesOrderEntryServiceProvider iSalesOrderEntryServiceProvider;

    /**
     * 核心订单列表查询
     *
     * @param maps
     * @return ART订单列表ResponseData
     */
    @RequestMapping(value = "/markor/art241/core/order/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestParam Map maps) {
        List<SalesOrder> list = new ArrayList<SalesOrder>();
        PaginatedList<SalesOrder> partList = new PaginatedList();
        PaginatedList<SalesOrder> paginatedList = new PaginatedList();
        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        //订单状态
        JSONArray orderStatus_ = status.getJSONArray("orderStatus_");
        String[] strOrderStatus = new String[orderStatus_.size()];
        for (int i = 0; i < orderStatus_.size(); i++) {
            strOrderStatus[i] = orderStatus_.get(i).toString();
        }
        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        //订单编号
        String code = null;
        if (data.get("code") != null) {
            code = data.get("code").toString();
        }
        //平台订单编号
        String escOrderCode = null;
        if (data.get("escOrderCode") != null) {
            escOrderCode = data.get("escOrderCode").toString();
        }
        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pagesize = 0;
        if (data.get("pagesize") != null) {
            pagesize = Integer.parseInt(data.get("pagesize").toString());
        }
        SalesOrder salesOrder = new SalesOrder();
        if (code != null) {
            salesOrder.setCode(code);
        }
        if (escOrderCode != null) {
            salesOrder.setEscOrderCode(escOrderCode);
        }
        if (strOrderStatus.length != 0) {
            for (int i = 0; i < strOrderStatus.length; i++) {
                salesOrder.setOrderStatus(strOrderStatus[i]);
                partList = iSalesOrderServiceProvider.getOrderList(
                        salesOrder, page, pagesize);
                List<SalesOrder> rows = partList.getRows();
                for (SalesOrder row : rows) {
                    list.add(row);
                }
            }
            paginatedList.setRows(list);
        } else {
            paginatedList = iSalesOrderServiceProvider.getOrderList(
                    salesOrder, page, pagesize);
        }

        return new ResponseData(paginatedList);
    }

    /**
     * 根据核心订单ID查询订单详情
     *
     * @param orderId
     * @return ART订单列表ResponseData
     */
    @RequestMapping(value = "/markor/art241/core/order/queryByOrderId")
    @ResponseBody
    public ResponseData queryByOrderId(HttpServletRequest request, @RequestParam("orderId") String orderId) {
        PaginatedList<SalesOrder> paginatedList = new PaginatedList();
        List list = new ArrayList<SalesOrder>();
        SalesOrder salesOrder = iSalesOrderServiceProvider.getOrderByOrderId(orderId);
        list.add(salesOrder);
        paginatedList.setRows(list);
        return new ResponseData(paginatedList);
    }


    /**
     * 根据核心订单ID查询订单行列表
     *
     * @param orderId
     * @return ART订单列表ResponseData
     */
    @RequestMapping(value = "/markor/art241/core/order/queryEntryByOrderId")
    @ResponseBody
    public ResponseData queryEntryByOrderId(@RequestParam("orderId") String orderId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        PaginatedList<SalesOrderEntry> orderEntryList = iSalesOrderEntryServiceProvider.getOrderEntryList(orderId, page, pageSize);
        return new ResponseData(orderEntryList);
    }

    /**
     * 根据核心订单ID更新收货人信息和发货信息
     *
     * @param salesOrderList
     * @param request
     * @return
     */
    @RequestMapping(value = "/markor/art241/core/order/updateReceiverAndInvoiceInfo")
    @ResponseBody
    public ResponseData updateReceiverAndInvoiceInfo(@RequestBody List<SalesOrder> salesOrderList, HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        SalesOrderPojo salesOrderPojo = new SalesOrderPojo();
        salesOrderPojo.setOrderId(salesOrderList.get(0).getOrderId());
        salesOrderPojo.setReceiverName(salesOrderList.get(0).getReceiverName());
        salesOrderPojo.setReceiverMobile(salesOrderList.get(0).getReceiverMobile());
        salesOrderPojo.setReceiverPhone(salesOrderList.get(0).getReceiverPhone());
        salesOrderPojo.setReceiverCountry(salesOrderList.get(0).getReceiverCountry());
        salesOrderPojo.setReceiverState(salesOrderList.get(0).getReceiverState());
        salesOrderPojo.setReceiverCity(salesOrderList.get(0).getReceiverCity());
        salesOrderPojo.setReceiverDistrict(salesOrderList.get(0).getReceiverDistrict());
        salesOrderPojo.setReceiverAddress(salesOrderList.get(0).getReceiverAddress());
        salesOrderPojo.setReceiverZip(salesOrderList.get(0).getReceiverZip());
        salesOrderPojo.setInvoiceType(salesOrderList.get(0).getInvoiceType());
        salesOrderPojo.setInvoiceName(salesOrderList.get(0).getInvoiceName());
        salesOrderPojo.setInvoiceNumber(salesOrderList.get(0).getInvoiceNumber());
        try {
            iSalesOrderServiceProvider.updateReceiverAndInvoiceInfo(salesOrderPojo);
        } catch (BusinessException e) {
            logger.error("业务校验失败:", e);
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
        return responseData;
    }
}

