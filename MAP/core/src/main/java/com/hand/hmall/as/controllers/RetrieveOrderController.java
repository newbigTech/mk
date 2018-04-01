package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.service.IRetrieveOrderService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name RetrieveOrderController
 * @description 取回单 Controller类
 * @date 2017/7/19
 */
@Controller
public class RetrieveOrderController extends BaseController {

    @Autowired
    private IRetrieveOrderService service;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/retrieveOrder/selectUserInfoByOrderId")
    @ResponseBody
    public ResponseData selectUserInfoByOrderId(RetrieveOrder dto) {
        return new ResponseData(service.selectUserInfoByOrderId(dto));
    }

    /**
     * 查询取回单详细信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/retrieveOrder/selectRetrieveOrderById")
    @ResponseBody
    public ResponseData selectRetrieveOrderById(RetrieveOrder dto) {
        return new ResponseData(service.selectRetrieveOrderById(dto));
    }

    /**
     * 保存取回单信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/retrieveOrder/saveRetrieveOrder")
    @ResponseBody
    public String saveRetrieveOrder(HttpServletRequest request, @RequestBody List<RetrieveOrder> dto) {
        IRequest iRequest = this.createRequestContext(request);
        RetrieveOrder retrieveOrder;
        if (dto.get(0).getReceiptOrderId() != null) {
            List<RetrieveOrder> retrieveOrderList = service.selectRetrieveOrderById(dto.get(0));
            //更新
            if (retrieveOrderList != null && retrieveOrderList.size() == 1) {
                retrieveOrder = service.updateByPrimaryKeySelective(iRequest, dto.get(0));
                return retrieveOrder.getReceiptOrderId()+"";
            }
        }//新增
        else {
            dto.get(0).setReceiptType("SR07");
            dto.get(0).setCode(sequenceGenerateService.getNextAsCode());
            retrieveOrder = service.insertSelective(iRequest, dto.get(0));
            return retrieveOrder.getReceiptOrderId() + "";
        }
        return "fail";
    }
}