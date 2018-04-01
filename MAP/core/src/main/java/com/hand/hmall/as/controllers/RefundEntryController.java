package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.as.dto.RefundEntry;
import com.hand.hmall.as.service.IRefundEntryService;
import com.hand.hmall.dto.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * author: zhangzilong
 * name: RefundEntryController.java
 * discription: 退款单行Controller
 * date: 2017/8/7
 * version: 0.1
 */
@Controller
public class RefundEntryController extends BaseController {

    @Autowired
    IRefundEntryService service;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送退款单行信息至HPAY
     *
     * @param request
     * @param dto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hmall/as/refundEntry/sendToHPAY")
    public ResponseData sendToHPAY(HttpServletRequest request, RefundEntry dto) {
        ResponseData rpd = new ResponseData();
        try {
            HashMap result = service.sendToHPAY(dto);
            rpd.setSuccess((boolean)result.get("success"));
            rpd.setMsg(result.get("msg").toString());
        } catch (Exception e) {
            rpd.setSuccess(false);
            rpd.setMsg("系统异常,"+e);
        }
        return rpd;
    }

    /**
     * 根据退款单ID查询对应的退款单行
     *
     * @param request
     * @param asRefundId
     * @param page
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hmall/as/refundEntry/selectEntryByRefundId")
    public com.hand.hap.system.dto.ResponseData selectEntryByRefundId(HttpServletRequest request, @RequestParam(value = "asRefundId") Long asRefundId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize ) {
        IRequest requestContext = createRequestContext(request);
        return new com.hand.hap.system.dto.ResponseData(service.selectRefundOrderEntry(requestContext, asRefundId, page, pageSize));
    }

    @ResponseBody
    @RequestMapping(value="/hmall/as/refundEntry/saveEntryInfo",method = RequestMethod.POST)
    public com.hand.hap.system.dto.ResponseData saveEntryInfo(HttpServletRequest request, @RequestBody RefundEntry dto){
        com.hand.hap.system.dto.ResponseData rd = new com.hand.hap.system.dto.ResponseData();
        try {
            dto.setRefundHeaderCode(null);
            if(service.updateRefundEntry(dto) == 1)
                rd.setSuccess(true);
            else
                rd.setSuccess(false);
        } catch (Exception e) {
            rd.setSuccess(false);
            rd.setMessage("保存信息行时抛出异常");
            logger.error("抛出异常",e);
        }
        return rd;
    }

}
