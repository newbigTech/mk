package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.ConsignmentBk;
import com.hand.hmall.om.service.IConsignmentBkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author shoupeng.wei
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/10/14 18:45
 */
@Controller
public class ConsignmentBkController extends BaseController {

    @Autowired
    private IConsignmentBkService service;

    /**
     * 查询发货单详细信息
     *
     * @param dto      发货单
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "hmall/om/consignment/bk/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(ConsignmentBk dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }

    /**
     * 通过ID查询发货单快照信息
     * @param dto
     * @return
     */
    @RequestMapping(value = "hmall/om/consignment/bk/queryById")
    @ResponseBody
    public ResponseData queryById(ConsignmentBk dto) {
        return new ResponseData(Arrays.asList(service.queryById(dto.getConsignmentId())));
    }
}
