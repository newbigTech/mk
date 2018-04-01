package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.Receipt;
import com.hand.hmall.as.service.IReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ReceiptController
 * @date 2017/7/19
 */
@Controller
public class ReceiptController extends BaseController {

    @Autowired
    private IReceiptService service;

    /**
     * 根据服务单CODE查询售后单信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/receipt/queryReceiptByServiceOrderId")
    @ResponseBody
    public ResponseData queryReceiptByServiceOrderId(Receipt dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryReceiptByServiceOrderId(dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/receipt/query")
    @ResponseBody
    public ResponseData query(Receipt dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/receipt/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Receipt> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/receipt/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Receipt> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}