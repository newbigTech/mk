package com.hand.hmall.om.controllers;

import com.hand.hmall.om.dto.BalanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmBalance;
import com.hand.hmall.om.service.IOmBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

@Controller
public class OmBalanceController extends BaseController {

    @Autowired
    private IOmBalanceService service;

    private Logger logger = LoggerFactory.getLogger(OmBalanceController.class);

    /**'
     * 手动对账时，选择好匹配的财务数据和支付/退款数据后，生成一条对账单数据
     * @param accountId     财务表hmall_om_accounts主键
     * @param infoId        支付信息表hmall_om_paymentinfo表主键，或者退款信息表hmall_as_refundinfo表主键
     * @param type           第三个参数来源，如果是支付信息表hmall_om_paymentinfo表，为1，如果退款信息表hmall_as_refundinfo表，为2
     * @return  对账后台处理后的返回结果
     */
    @RequestMapping(value = "/hmall/om/balance/addBalance")
    @ResponseBody
    public ResponseData addBalance(@RequestParam("accountId") String accountId, @RequestParam("infoId") String infoId, @RequestParam("type") String type) {
        ResponseData rd = new ResponseData();
        try {
            int i = service.insertBalances(accountId, infoId, type);
            if(i == 1){
                rd.setSuccess(true);
                rd.setMessage("手动对账成功");
            }else {
                rd.setSuccess(false);
                rd.setMessage("手动对账失败");
            }
        } catch (Exception e) {
            logger.info(this.getClass().getName()+"手动对账发生异常");
        }
        return rd;
    }

    /**
     * 界面列表显示数据查询，根据界面上方填写的搜索条件查询数据
     * @param request   请求对象
     * @param dto   请求参数实体对象
     * @param page  页面显示页数
     * @param pageSize      每页显示数据条数
     * @return  查询出的数据
     */
    @RequestMapping(value = "/hmall/om/balance/query")
    @ResponseBody
    public ResponseData query(BalanceInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectBalances(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/balance/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List <OmBalance> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/balance/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List <OmBalance> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}