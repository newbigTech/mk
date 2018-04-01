package com.hand.hmall.as.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.Dispatchorder;
import com.hand.hmall.as.service.IDispatchorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auth shoupeng.wei
 * @Description:派工单详情界面对应的Controller类
 * @Create at 2017-7-17 11:41:15
 *
 */
@Controller
public class DispatchorderController extends BaseController {

    @Autowired
    private IDispatchorderService service;

    @RequestMapping(value = "/hmall/as/dispatchorder/saveDispatchOrderInfo")
    @ResponseBody
    public ResponseData saveDispatchOrderInfo(HttpServletRequest request, @RequestBody List<Dispatchorder> dto) {
        IRequest requestCtx = createRequestContext(request);
        if(dto != null && dto.size() > 0){
            return new ResponseData(service.saveDispatchOrederInfo(requestCtx, dto.get(0)));
        }else{
            return new ResponseData(false);
        }

    }

    @RequestMapping(value = "/hmall/as/dispatchorder/selectDispatchOrderEntry")
    @ResponseBody
    public ResponseData selectDispatchOrderEntry(@RequestParam("serviceOrderId") Long serviceOrderId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectDispatchOrderEntry(requestContext,serviceOrderId,page,pageSize));
    }


    @RequestMapping(value = "/hmall/as/dispatchorder/queryDispatchorderById")
    @ResponseBody
    public ResponseData queryDispatchorderById(HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        Dispatchorder dispatchorder = new Dispatchorder();
        dispatchorder.setReceiptOrderId(Long.parseLong(request.getParameter("receiptOrderId")));
        List<Dispatchorder> dispatchorderList = new ArrayList<Dispatchorder>();
        dispatchorderList.add(service.selectByPrimaryKey(requestCtx, dispatchorder));
        return new ResponseData(dispatchorderList);
    }

    @RequestMapping(value = "/hmall/as/dispatchorder/query")
    @ResponseBody
    public ResponseData query(Dispatchorder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/dispatchorder/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Dispatchorder> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/dispatchorder/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Dispatchorder> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

}