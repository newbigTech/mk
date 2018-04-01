package com.hand.hmall.fnd.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.fnd.dto.FndBusinessLog;
import com.hand.hmall.fnd.service.IFndBusinessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class FndBusinessLogController extends BaseController{

    @Autowired
    private IFndBusinessLogService service;

        /**
         * 操作日志标签页根据订单ID查询数据
         * @param dto   请求参数，包含订单ID
         * @param page  显示数据页数
         * @param pageSize     每页显示数据条数
         * @param request   请求体
         * @return  查询结果
         */
    @RequestMapping(value = "/hmall/fnd/business/log/query")
    @ResponseBody
    public ResponseData query(FndBusinessLog dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }

        /**
         * 系统生成的日志新建和修改使用
         */
    @RequestMapping(value = "/hmall/fnd/business/log/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FndBusinessLog> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

        /**
         * 系统生成的日志删除使用
         */
    @RequestMapping(value = "/hmall/fnd/business/log/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FndBusinessLog> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
}