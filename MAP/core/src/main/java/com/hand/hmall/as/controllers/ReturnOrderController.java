package com.hand.hmall.as.controllers;


import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.dto.ReturnOrder;
import com.hand.hmall.as.service.IReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;


/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ReturnOrderController
 * @description 退货单详情 Controller类
 * @date 2017/7/17
 */
@Controller
public class ReturnOrderController extends BaseController {

    @Autowired
    private IReturnOrderService service;

    @RequestMapping(value = "/hmall/as/returnOrder/query")
    @ResponseBody
    public ResponseData query(ReturnOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/returnOrder/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ReturnOrder> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/returnOrder/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ReturnOrder> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 根据退货单id查询对应的退货单
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/returnOrder/selectReturnOrderById")
    @ResponseBody
    public ResponseData selectReturnOrderById(ReturnOrder dto) {
        List<ReturnOrder> returnOrders = service.selectReturnOrderById(dto);
        return new ResponseData(service.selectReturnOrderById(dto));
    }

    /**
     * 保存退貨单信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/returnOrder/saveReturnOrder")
    @ResponseBody
    public String saveRetrieveOrder(HttpServletRequest request, @RequestBody List<ReturnOrder> dto) {
        IRequest iRequest = this.createRequestContext(request);
        ReturnOrder returnOrder;
        if (dto.get(0).getReceiptOrderId() != null) {
            List<ReturnOrder> returnOrderList = service.selectReturnOrderById(dto.get(0));
            //更新
            if (returnOrderList != null && returnOrderList.size() == 1) {
                returnOrder = service.updateByPrimaryKeySelective(iRequest, dto.get(0));
                return returnOrder.getReceiptOrderId() + "-" + returnOrder.getCode();
            }
        }//新增
        else {
            //暂时写死
            dto.get(0).setReceiptType("SR01");
            Random r = new Random();
            dto.get(0).setCode((r.nextInt(999999) + 1) + "");
            returnOrder = service.insertSelective(iRequest, dto.get(0));
            Long receiptOrderId = returnOrder.getReceiptOrderId();
            return returnOrder.getReceiptOrderId() + "-" + returnOrder.getCode();
        }
        return "fail";
    }

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/returnOrder/selectUserInfoByOrderId")
    @ResponseBody
    public ResponseData selectUserInfoByOrderId(RetrieveOrder dto) {
        return new ResponseData(service.selectUserInfoByOrderId(dto));
    }

}
