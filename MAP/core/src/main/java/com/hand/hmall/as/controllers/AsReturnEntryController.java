package com.hand.hmall.as.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.service.IAsReturnEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangmeng
 * @version 0.1
 * @name AsReturnEntryController
 * @description 退货单行
 * @date 2017/8/31
 */
@Controller
public class AsReturnEntryController extends BaseController {

    @Autowired
    private IAsReturnEntryService asReturnEntryService;

    /**
     * 根据订单ID查询订单行中未创建发货单数量大于零的订单行
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/entry/queryOrderEntrynotReturnQuantity")
    @ResponseBody
    public ResponseData queryOrderEntrynotReturnQuantity(Long asReturnId) {
        ResponseData responseData = new ResponseData();
        if (asReturnId == null) {
            responseData.setSuccess(false);
            responseData.setMessage("退货单id不能为空");
            return responseData;
        }
        AsReturnEntry asReturnEntry = new AsReturnEntry();
        asReturnEntry.setAsReturnId(asReturnId);
        return new ResponseData(asReturnEntryService.queryOrderEntrynotReturnQuantity(asReturnEntry));
    }

    /**
     * 根据退货单id查询退货单行
     *
     * @param asReturnId
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/entry/queryReturnEntryById")
    @ResponseBody
    public ResponseData queryReturnEntryById(Long asReturnId) {
        ResponseData responseData = new ResponseData();
        if (asReturnId == null) {
            responseData.setSuccess(false);
            responseData.setMessage("退货单id不能为空");
            return responseData;
        }
        AsReturnEntry asReturnEntry = new AsReturnEntry();
        asReturnEntry.setAsReturnId(asReturnId);
        return new ResponseData(asReturnEntryService.queryReturnEntryById(asReturnEntry));
    }
}